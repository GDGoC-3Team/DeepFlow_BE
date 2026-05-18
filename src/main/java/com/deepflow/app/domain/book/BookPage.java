package com.deepflow.app.domain.book;

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
@Table(name = "book_pages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private int characterCount;

    public static BookPage of(Book book, String content, int characterCount) {
        BookPage bookPage = new BookPage();
        bookPage.book = book;
        bookPage.content = content;
        bookPage.characterCount = characterCount;
        return bookPage;
    }
}
