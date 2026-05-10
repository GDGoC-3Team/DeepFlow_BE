package com.deepflow.app.domain.book;

public record BookResponse(Long id, String title, String author, String coverImageUrl) {

    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getCoverImageUrl());
    }
}
