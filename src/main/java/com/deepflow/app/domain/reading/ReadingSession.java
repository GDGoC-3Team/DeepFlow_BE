package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reading_sessions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate date;

    private LocalDateTime completedAt;

    @Column(nullable = false)
    private int currentOffset;

    @Column(nullable = false)
    private int maxOffset;

    public static ReadingSession start(User user, Book book, LocalDate date) {
        ReadingSession session = new ReadingSession();
        session.user = user;
        session.book = book;
        session.date = date;
        session.currentOffset = 0;
        session.maxOffset = 0;
        return session;
    }

    public void complete() {
        this.completedAt = LocalDateTime.now();
    }

    public void updateProgress(int currentOffset, int maxOffset) {
        this.currentOffset = Math.max(0, currentOffset);
        this.maxOffset = Math.max(this.currentOffset, Math.max(0, maxOffset));
    }
}
