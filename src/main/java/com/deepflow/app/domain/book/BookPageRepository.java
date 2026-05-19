package com.deepflow.app.domain.book;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookPageRepository extends JpaRepository<BookPage, Long> {

    Optional<BookPage> findFirstByBookOrderByIdAsc(Book book);

    List<BookPage> findByBookOrderByIdAsc(Book book);

    @Query("select coalesce(sum(bp.characterCount), 0) from BookPage bp where bp.book = :book")
    int sumCharacterCountByBook(@Param("book") Book book);
}
