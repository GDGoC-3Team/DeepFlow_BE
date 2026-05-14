package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.book.BookPage;
import com.deepflow.app.domain.book.BookPageRepository;
import com.deepflow.app.domain.book.BookRepository;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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

    // 독서 세션 시작
    @Transactional
    public ReadingSession startSession(String firebaseUid, Long bookId) {
        User user = userService.getByFirebaseUid(firebaseUid);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        LocalDate todayKst = LocalDate.now(KST);
        return readingRepository.findByUserAndDate(user, todayKst)
                .orElseGet(() -> readingRepository.save(ReadingSession.start(user, book, todayKst)));
    }

    // 페이지 독서 시간 기록
    @Transactional
    public PageTime recordPageTime(String firebaseUid, PageTimeRequest request) {
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = getOwnedSession(user, request.sessionId());
        boolean reread = pageTimeRepository.findTopBySessionOrderByIdDesc(session)
                .map(previous -> request.pageNumber() < previous.getPageNumber() && request.elapsedSeconds() > 3)
                .orElse(false);
        return pageTimeRepository.save(PageTime.of(session, request.pageNumber(), request.elapsedSeconds(), reread));
    }

    // 독서 결과 분석
    @Transactional(readOnly = true)
    public ReadingResultResponse result(String firebaseUid, Long sessionId) {
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = getOwnedSession(user, sessionId);
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByPageNumberAsc(session);
        double average = pageTimes.stream()
                .mapToDouble(pageTime -> secondsPerCharacter(session.getBook(), pageTime))
                .average()
                .orElse(0.0);
        List<ReadingResultResponse.PageBreakdown> pages = pageTimes.stream()
                .map(pageTime -> {
                    int characterCount = characterCount(session.getBook(), pageTime.getPageNumber());
                    double secondsPerCharacter = characterCount == 0 ? 0.0 : (double) pageTime.getElapsedSeconds() / characterCount;
                    boolean outlier = average > 0.0 && secondsPerCharacter > average * 1.5;
                    return new ReadingResultResponse.PageBreakdown(
                            pageTime.getPageNumber(),
                            pageTime.getElapsedSeconds(),
                            characterCount,
                            secondsPerCharacter,
                            outlier,
                            pageTime.isReread()
                    );
                })
                .toList();
        return new ReadingResultResponse(session.getId(), average, pages);
    }

    // 독서 완료 날짜 조회
    @Transactional(readOnly = true)
    public List<LocalDate> completedDates(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);
        return readingRepository.findCompletedDates(user);
    }

    // 독서 습관 기록 - 해빗 트래커
    @Transactional(readOnly = true)
    public HabbitResponse getReadingHabbit(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);

        // 1. 사용자가 독서 완료한 날짜 목록 확인
        List<LocalDate> completedDates = readingRepository.findCompletedDates(user);

        // 2. 완료 기록이 없으면 연속 기록은 0
        if (completedDates.isEmpty()) {
            return new HabbitResponse(0, 0);
        }
        int streakDays = 0;

        // 3. 최근 날짜부터 거꾸로 순회하면서 연속 독서 여부 체크
        LocalDate streakBaseDate = completedDates.get(completedDates.size() - 1);
        for (int i = completedDates.size() - 1; i >= 0; i--) {
            if (completedDates.get(i).equals(streakBaseDate)) {
                streakDays++;
                streakBaseDate = streakBaseDate.minusDays(1);
            } else {
                break;
            }
        }

        // 4. 최종 streak 반환
        return new HabbitResponse(streakDays, Math.min(streakDays, 7));
    }

    // 독서 세션 소유권 검증
    private ReadingSession getOwnedSession(User user, Long sessionId) {
        ReadingSession session = readingRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Reading session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Reading session does not belong to user");
        }
        return session;
    }

    // 페이지 체류시간 계산
    private double secondsPerCharacter(Book book, PageTime pageTime) {
        int characterCount = characterCount(book, pageTime.getPageNumber());
        if (characterCount == 0) {
            return 0.0;
        }
        return (double) pageTime.getElapsedSeconds() / characterCount;
    }

    // 페이지 글자 수 조회
    private int characterCount(Book book, int pageNumber) {
        return bookPageRepository.findByBookAndPageNumber(book, pageNumber)
                .map(BookPage::getCharacterCount)
                .orElse(0);
    }
}
