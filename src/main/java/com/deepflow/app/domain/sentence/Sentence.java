package com.deepflow.app.domain.sentence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "sentences")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private String bookTitle;

    private String author;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Sentence(String content, String imageUrl, String bookTitle, String author) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.bookTitle = bookTitle;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
}
