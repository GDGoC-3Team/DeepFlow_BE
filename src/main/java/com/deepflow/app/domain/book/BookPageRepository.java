package com.deepflow.app.domain.book;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookPageRepository extends JpaRepository<BookPage, Long> {

    List<BookPage> findByBookOrderByPageNumberAsc(Book book);

    Optional<BookPage> findByBookAndPageNumber(Book book, int pageNumber);

    @Query("select coalesce(sum(bp.characterCount), 0) from BookPage bp where bp.book = :book")
    int sumCharacterCountByBook(@Param("book") Book book);
}
