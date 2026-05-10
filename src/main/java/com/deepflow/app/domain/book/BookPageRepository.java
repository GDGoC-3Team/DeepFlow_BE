package com.deepflow.app.domain.book;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPageRepository extends JpaRepository<BookPage, Long> {

    List<BookPage> findByBookOrderByPageNumberAsc(Book book);

    Optional<BookPage> findByBookAndPageNumber(Book book, int pageNumber);
}
