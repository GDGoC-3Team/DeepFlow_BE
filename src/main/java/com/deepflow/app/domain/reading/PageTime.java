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
@Table(name = "page_times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private ReadingSession session;

    private int pageNumber;

    private long elapsedSeconds;

    @Column(name = "is_reread")
    private boolean isReread;

    public static PageTime of(ReadingSession session, int pageNumber, long elapsedSeconds, boolean reread) {
        PageTime pageTime = new PageTime();
        pageTime.session = session;
        pageTime.pageNumber = pageNumber;
        pageTime.elapsedSeconds = elapsedSeconds;
        pageTime.isReread = reread;
        return pageTime;
    }
}
