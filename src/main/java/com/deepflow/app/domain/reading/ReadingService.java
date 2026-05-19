package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.book.BookPage;
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
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ReadingRepository readingRepository;
    private final PageTimeRepository pageTimeRepository;
    private final HighlightRepository highlightRepository;
    private final BookRepository bookRepository;
    private final BookPageRepository bookPageRepository;
    private final UserService userService;
    private final ReadingSessionValidator readingSessionValidator;

    @Transactional(readOnly = true)
    public TodayReadingResponse todayReading() {
        User user = userService.getCurrentUser();

        List<Book> savedUnreadBooks = bookRepository.findUnreadBooksFromSavedSentences(user);
        if (!savedUnreadBooks.isEmpty()) {
            return buildTodayReadingResponse(randomBook(savedUnreadBooks));
        }

        List<Book> unreadBooks = bookRepository.findUnreadBooks(user);
        if (!unreadBooks.isEmpty()) {
            return buildTodayReadingResponse(randomBook(unreadBooks));
        }

        List<Book> allBooks = bookRepository.findAllWithPages();
        if (allBooks.isEmpty()) {
            throw new EntityNotFoundException("Book not found");
        }

        return buildTodayReadingResponse(randomBook(allBooks));
    }

    @Transactional
    public ReadingSession startSession(Long bookId) {
        User user = userService.getCurrentUser();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        LocalDate todayKst = LocalDate.now(KST);
        return readingRepository.findByUserAndDate(user, todayKst)
                .orElseGet(() -> readingRepository.save(ReadingSession.start(user, book, todayKst)));
    }

    @Transactional
    public PageTime recordPageTime(PageTimeRequest request) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, request.sessionId());
        OffsetRange offsetRange = resolveOffsetRange(session.getBook(), request.pageNumber());
        int normalizedStartOffset = offsetRange.startOffset();
        int normalizedEndOffset = offsetRange.endOffset();
        boolean reread = pageTimeRepository.findTopBySessionOrderByIdDesc(session)
                .map(previous -> normalizedStartOffset < previous.getEndOffset() && request.elapsedSeconds() > 3)
                .orElse(false);

        session.updateProgress(normalizedEndOffset, Math.max(session.getMaxOffset(), normalizedEndOffset));
        return pageTimeRepository.save(PageTime.of(
                session,
                request.pageNumber(),
                normalizedStartOffset,
                normalizedEndOffset,
                request.elapsedSeconds(),
                reread
        ));
    }

    @Transactional
    public ReadingSession completeSession(Long sessionId) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);
        session.complete();
        return session;
    }

    @Transactional
    public HighlightResponse createHighlight(Long sessionId, CreateHighlightRequest request) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);

        validateHighlightRange(request);

        Highlight highlight = highlightRepository.save(Highlight.create(
                session,
                request.startOffset(),
                request.endOffset(),
                request.highlightedText().trim()
        ));
        return HighlightResponse.from(highlight);
    }

    @Transactional
    public void deleteHighlight(Long sessionId, Long highlightId) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);
        Highlight highlight = highlightRepository.findByIdAndSession(highlightId, session)
                .orElseThrow(() -> new EntityNotFoundException("Highlight not found"));
        highlightRepository.delete(highlight);
    }

    @Transactional(readOnly = true)
    public List<HighlightResponse> getHighlights(Long sessionId) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);
        return highlightRepository.findBySessionOrderByStartOffsetAscIdAsc(session).stream()
                .map(HighlightResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReadingResultResponse result(Long sessionId) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByStartOffsetAsc(session);

        double average = pageTimes.stream()
                .mapToDouble(this::secondsPerCharacter)
                .average()
                .orElse(0.0);

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

    @Transactional(readOnly = true)
    public List<LocalDate> completedDates() {
        User user = userService.getCurrentUser();
        return readingRepository.findCompletedDates(user);
    }

    @Transactional(readOnly = true)
    public ReadingCalendarResponse calendar(int year, int month) {
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("year must be between 1 and 9999");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }

        User user = userService.getCurrentUser();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        LocalDate today = LocalDate.now(KST);

        Set<LocalDate> completedDates = readingRepository
                .findByUserAndCompletedAtIsNotNullAndDateBetweenOrderByDateAsc(user, startDate, endDate)
                .stream()
                .map(ReadingSession::getDate)
                .collect(java.util.stream.Collectors.toSet());

        List<ReadingCalendarDayResponse> days = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> new ReadingCalendarDayResponse(date, completedDates.contains(date), date.equals(today)))
                .toList();

        return new ReadingCalendarResponse(year, month, today, days);
    }

    @Transactional(readOnly = true)
    public ReadingHistoryDateResponse historyByDate(LocalDate date) {
        User user = userService.getCurrentUser();

        List<ReadingHistoryBookResponse> books = readingRepository.findByUserAndDateOrderByIdAsc(user, date)
                .stream()
                .filter(session -> session.getCompletedAt() != null)
                .map(ReadingHistoryBookResponse::from)
                .toList();

        return new ReadingHistoryDateResponse(date, !books.isEmpty(), books);
    }

    @Transactional(readOnly = true)
    public HabbitResponse getReadingHabbit() {
        User user = userService.getCurrentUser();
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

    private double secondsPerCharacter(PageTime pageTime) {
        int characterCount = pageTime.getCharacterCount();
        if (characterCount == 0) {
            return 0.0;
        }
        return (double) pageTime.getElapsedSeconds() / characterCount;
    }

    private TodayReadingResponse buildTodayReadingResponse(Book book) {
        BookPage bookPage = bookPageRepository.findFirstByBookOrderByIdAsc(book)
                .orElseThrow(() -> new EntityNotFoundException("Book page not found"));
        return TodayReadingResponse.from(book, bookPage);
    }

    private Book randomBook(List<Book> books) {
        return books.get(ThreadLocalRandom.current().nextInt(books.size()));
    }

    private OffsetRange resolveOffsetRange(Book book, int pageNumber) {
        List<BookPage> pages = bookPageRepository.findByBookOrderByIdAsc(book);
        if (pageNumber < 1 || pageNumber > pages.size()) {
            throw new IllegalArgumentException("pageNumber is out of range");
        }

        int startOffset = 0;
        for (int i = 0; i < pageNumber - 1; i++) {
            startOffset += pages.get(i).getCharacterCount();
        }

        int endOffset = startOffset + pages.get(pageNumber - 1).getCharacterCount();
        return new OffsetRange(startOffset, endOffset);
    }

    private void validateHighlightRange(CreateHighlightRequest request) {
        if (request.endOffset() < request.startOffset()) {
            throw new IllegalArgumentException("endOffset must be greater than or equal to startOffset");
        }
    }

    private record OffsetRange(int startOffset, int endOffset) {
    }
}
