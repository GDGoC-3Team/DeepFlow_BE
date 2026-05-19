package com.deepflow.app.domain.book;

import com.deepflow.app.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b
            where exists (
                select 1 from BookPage bp
                where bp.book = b
            )
              and b.id not in (
                select rs.book.id from ReadingSession rs
                where rs.user = :user and rs.completedAt is not null
            )
            """)
    List<Book> findUnreadBooks(@Param("user") User user);

    @Query("""
            select distinct b from Book b
            where exists (
                select 1 from BookPage bp
                where bp.book = b
            )
              and exists (
                select 1 from SavedSentence ss
                where ss.user = :user
                  and ss.sentence.bookTitle = b.title
                  and (
                    (ss.sentence.author is null and b.author is null)
                    or ss.sentence.author = b.author
                  )
            )
              and b.id not in (
                select rs.book.id from ReadingSession rs
                where rs.user = :user and rs.completedAt is not null
            )
            """)
    List<Book> findUnreadBooksFromSavedSentences(@Param("user") User user);

    @Query("""
            select distinct b from Book b
            where b.id not in (
                select rs.book.id from ReadingSession rs
                where rs.user = :user and rs.completedAt is not null
            )
            """)
    List<Book> findUnreadBooksIncludingBooksWithoutPages(@Param("user") User user);

    @Query("""
            select distinct b from Book b
            where exists (
                select 1 from BookPage bp
                where bp.book = b
            )
            """)
    List<Book> findAllWithPages();
}
