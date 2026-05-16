package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.book.BookPageRepository;
import com.deepflow.app.domain.book.BookRepository;
import com.deepflow.app.domain.user.User;
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

    @Transactional
    public ReadingSession startSession(User user, Long bookId) {
        // TODO: Daily reset: query ReadingSession by date = today (KST). Reset at 00:00 KST.
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        LocalDate todayKst = LocalDate.now(KST);
        return readingRepository.findByUserAndDate(user, todayKst)
                .orElseGet(() -> readingRepository.save(ReadingSession.start(user, book, todayKst)));
    }

    @Transactional
    public PageTime recordPageTime(User user, PageTimeRequest request) {
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

    @Transactional(readOnly = true)
    public ReadingResultResponse result(User user, Long sessionId) {
        ReadingSession session = getOwnedSession(user, sessionId);
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByStartOffsetAsc(session);

        double average = pageTimes.stream()
                .mapToDouble(this::secondsPerCharacter)
                .average()
                .orElse(0.0);

        List<ReadingResultResponse.SegmentBreakdown> segments = pageTimes.stream()
                .map(pageTime -> {
                    int characterCount = pageTime.getCharacterCount();
                    double secondsPerCharacter = characterCount == 0 ? 0.0 : (double) pageTime.getElapsedSeconds() / characterCount;
                    boolean outlier = average > 0.0 && secondsPerCharacter > average * 1.5;
                    return new ReadingResultResponse.SegmentBreakdown(
                            pageTime.getStartOffset(),
                            pageTime.getEndOffset(),
                            pageTime.getElapsedSeconds(),
                            characterCount,
                            secondsPerCharacter,
                            outlier,
                            pageTime.isReread()
                    );
                })
                .toList();

        int totalCharacterCount = totalCharacterCount(session.getBook());
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
    public List<LocalDate> completedDates(User user) {
        // TODO: Reading history calendar: group ReadingSession by date, return list of completed dates.
        return readingRepository.findCompletedDates(user);
    }

    @Transactional(readOnly = true)
    public int habitStreak(User user) {
        // TODO: Habit tracker: count consecutive reading days up to max 7. If streak > 7, show real count but cap visual tracker at 7.
        List<LocalDate> dates = readingRepository.findCompletedDates(user);
        int streak = 0;
        LocalDate cursor = LocalDate.now(KST);
        for (int i = dates.size() - 1; i >= 0; i--) {
            if (dates.get(i).equals(cursor)) {
                streak++;
                cursor = cursor.minusDays(1);
            }
        }
        return streak;
    }

    private ReadingSession getOwnedSession(User user, Long sessionId) {
        ReadingSession session = readingRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Reading session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Reading session does not belong to user");
        }
        return session;
    }

    private double secondsPerCharacter(PageTime pageTime) {
        int characterCount = pageTime.getCharacterCount();
        if (characterCount == 0) {
            return 0.0;
        }
        return (double) pageTime.getElapsedSeconds() / characterCount;
    }

    private int totalCharacterCount(Book book) {
        return bookPageRepository.sumCharacterCountByBook(book);
    }
}
