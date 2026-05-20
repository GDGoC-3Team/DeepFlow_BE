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

@Getter
@Entity
@Table(name = "highlights")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Highlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private ReadingSession session;

    private int startOffset;

    private int endOffset;

    @Column(columnDefinition = "TEXT")
    private String highlightedText;

    public static Highlight create(
            ReadingSession session,
            int startOffset,
            int endOffset,
            String highlightedText
    ) {
        Highlight highlight = new Highlight();
        highlight.session = session;
        highlight.startOffset = startOffset;
        highlight.endOffset = endOffset;
        highlight.highlightedText = highlightedText;
        return highlight;
    }
}
