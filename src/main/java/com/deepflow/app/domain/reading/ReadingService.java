package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.book.BookPageRepository;
import com.deepflow.app.domain.book.BookRepository;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ReadingRepository readingRepository;
    private final PageTimeRepository pageTimeRepository;
    private final BookRepository bookRepository;
    private final BookPageRepository bookPageRepository;
    private final UserService userService;

    //// ==========================
    //// 독서 세션 시작
    //// ==========================
    @Transactional
    public ReadingSession startSession(String firebaseUid, Long bookId) {
        User user = userService.getByFirebaseUid(firebaseUid);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        LocalDate todayKst = LocalDate.now(KST);
        return readingRepository.findByUserAndDate(user, todayKst)
                .orElseGet(() -> readingRepository.save(ReadingSession.start(user, book, todayKst)));
    }

    //// ==========================
    //// 페이지별 읽기 시간 기록
    //// ==========================
    @Transactional
    public PageTime recordPageTime(String firebaseUid, PageTimeRequest request) {
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = getOwnedSession(user, request.sessionId());
        int normalizedStartOffset = Math.max(0, request.startOffset());
        int normalizedEndOffset = Math.max(normalizedStartOffset, request.endOffset());
        boolean reread = pageTimeRepository.findTopBySessionOrderByIdDesc(session)
                .map(previous -> normalizedStartOffset < previous.getEndOffset() && request.elapsedSeconds() > 3)
                .orElse(false);

        session.updateProgress(normalizedEndOffset, Math.max(session.getMaxOffset(), normalizedEndOffset));
        return pageTimeRepository.save(PageTime.of(
                session,
                normalizedStartOffset,
                normalizedEndOffset,
                request.elapsedSeconds(),
                reread
        ));
    }

    //// ==========================
    //// 독서 완료료 상태 변경
    //// ==========================
    @Transactional
    public ReadingSession completeSession(String firebaseUid, Long sessionId) {

        // 1. 사용자 및 세션 소유권 검증
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = getOwnedSession(user, sessionId);

        // 2. 독서 세션 완료 처리
        session.complete();
        return session;
    }

    //// ==========================
    //// 독서 결과 분석 조회
    //// ==========================
    @Transactional(readOnly = true)
    public ReadingResultResponse result(String firebaseUid, Long sessionId) {

        // 1. 사용자 및 세션 소유권 검증
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = getOwnedSession(user, sessionId);

        // 2. 페이지별 읽기 시간 조회
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByStartOffsetAsc(session);

        // 3. 전체 평균 문자당 읽기 시간 계산
        double average = pageTimes.stream()
                .mapToDouble(this::secondsPerCharacter)
                .average()
                .orElse(0.0);

        // 4. 페이지별 독서 결과 생성
        List<ReadingResultResponse.SegmentBreakdown> segments = pageTimes.stream()
                .map(pageTime -> {
                    int characterCount = pageTime.getCharacterCount();
                    double segmentSecondsPerCharacter = characterCount == 0
                            ? 0.0
                            : (double) pageTime.getElapsedSeconds() / characterCount;
                    boolean outlier = average > 0.0 && segmentSecondsPerCharacter > average * 1.5;
                    return new ReadingResultResponse.SegmentBreakdown(
                            pageTime.getStartOffset(),
                            pageTime.getEndOffset(),
                            pageTime.getElapsedSeconds(),
                            characterCount,
                            segmentSecondsPerCharacter,
                            outlier,
                            pageTime.isReread()
                    );
                })
                .toList();

        int totalCharacterCount = bookPageRepository.sumCharacterCountByBook(session.getBook());
        double progressPercent = totalCharacterCount == 0
                ? 0.0
                : Math.min(100.0, ((double) session.getMaxOffset() / totalCharacterCount) * 100.0);

        return new ReadingResultResponse(
                session.getId(),
                session.getCurrentOffset(),
                session.getMaxOffset(),
                progressPercent,
                average,
                segments
        );
    }

    //// ==========================
    //// 독서 완료 날짜 목록 조회
    //// ==========================
    @Transactional(readOnly = true)
    public List<LocalDate> completedDates(String firebaseUid) {

        // 1. 유저 조회
        User user = userService.getByFirebaseUid(firebaseUid);

        // 2. 독서 완료 날짜 목록 반환
        return readingRepository.findCompletedDates(user);
    }

    //// ==========================
    //// 월별 독서 캘린더 조회
    //// ==========================
    @Transactional(readOnly = true)
    public ReadingCalendarResponse calendar(String firebaseUid, int year, int month) {

        // 1. 연/월 값 유효성 검증
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("year must be between 1 and 9999");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }

        // 2. 유저 조회
        User user = userService.getByFirebaseUid(firebaseUid);

        // 3. 조회 대상 월의 시작/종료 날짜 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        LocalDate today = LocalDate.now(KST);

        // 4. 해당 월의 독서 완료 날짜 집합 조회
        Set<LocalDate> completedDates = readingRepository
                .findByUserAndCompletedAtIsNotNullAndDateBetweenOrderByDateAsc(user, startDate, endDate)
                .stream()
                .map(ReadingSession::getDate)
                .collect(java.util.stream.Collectors.toSet());

        // 5. 월 전체 날짜에 대한 캘린더 응답 생성
        List<ReadingCalendarDayResponse> days = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> new ReadingCalendarDayResponse(date, completedDates.contains(date), date.equals(today)))
                .toList();

        return new ReadingCalendarResponse(year, month, today, days);
    }

    //// ==========================
    //// 날짜별 독서 기록 조회
    //// ==========================
    @Transactional(readOnly = true)
    public ReadingHistoryDateResponse historyByDate(String firebaseUid, LocalDate date) {
        
        // 1. 유저 조회
        User user = userService.getByFirebaseUid(firebaseUid);

        // 2. 해당 날짜의 완료된 독서 세션 조회
        List<ReadingHistoryBookResponse> books = readingRepository.findByUserAndDateOrderByIdAsc(user, date)
                .stream()
                .filter(session -> session.getCompletedAt() != null)
                .map(ReadingHistoryBookResponse::from)
                .toList();

        // 3. 날짜별 독서 기록 응답 생성
        return new ReadingHistoryDateResponse(date, !books.isEmpty(), books);
    }

    //// ==========================
    //// 독서 습관 기록 - 해빗 트래커
    //// ==========================
    @Transactional(readOnly = true)
    public HabbitResponse getReadingHabbit(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);
        List<LocalDate> completedDates = readingRepository.findCompletedDates(user);
        if (completedDates.isEmpty()) {
            return new HabbitResponse(0, 0);
        }

        int streakDays = 0;
        LocalDate streakBaseDate = completedDates.get(completedDates.size() - 1);
        for (int i = completedDates.size() - 1; i >= 0; i--) {
            if (completedDates.get(i).equals(streakBaseDate)) {
                streakDays++;
                streakBaseDate = streakBaseDate.minusDays(1);
            } else {
                break;
            }
        }

        return new HabbitResponse(streakDays, Math.min(streakDays, 7));
    }

    //// ==========================
    //// 독서 세션 소유권 검증
    //// ==========================
    private ReadingSession getOwnedSession(User user, Long sessionId) {
        ReadingSession session = readingRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Reading session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Reading session does not belong to user");
        }
        return session;
    }

    //// ==========================
    //// 페이지 체류시간 계산
    //// ==========================
    private double secondsPerCharacter(PageTime pageTime) {
        int characterCount = pageTime.getCharacterCount();
        if (characterCount == 0) {
            return 0.0;
        }
        return (double) pageTime.getElapsedSeconds() / characterCount;
    }

}
