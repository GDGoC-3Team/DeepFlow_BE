package com.deepflow.app.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookPageRepository extends JpaRepository<BookPage, Long> {

    @Query("select coalesce(sum(bp.characterCount), 0) from BookPage bp where bp.book = :book")
    int sumCharacterCountByBook(@Param("book") Book book);
}
