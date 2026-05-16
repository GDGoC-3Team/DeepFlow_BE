package com.deepflow.app.domain.reading;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Getter
@Entity
@Table(name = "page_times")
@Check(constraints = "end_offset >= start_offset")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private ReadingSession session;

    @Column(nullable = false)
    private int startOffset;

    @Column(nullable = false)
    private int endOffset;

    private long elapsedSeconds;

    @Column(nullable = false)
    private int characterCount;

    @Column(name = "is_reread")
    private boolean isReread;

    public static PageTime of(ReadingSession session, int startOffset, int endOffset, long elapsedSeconds, boolean reread) {
        PageTime pageTime = new PageTime();
        pageTime.session = session;
        pageTime.startOffset = startOffset;
        pageTime.endOffset = endOffset;
        pageTime.elapsedSeconds = elapsedSeconds;
        pageTime.characterCount = Math.max(0, endOffset - startOffset);
        pageTime.isReread = reread;
        return pageTime;
    }
}
