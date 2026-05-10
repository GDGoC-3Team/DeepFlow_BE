package com.deepflow.app.domain.book;

import com.deepflow.app.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b
            where b.id not in (
                select rs.book.id from ReadingSession rs
                where rs.user = :user and rs.completedAt is not null
            )
            """)
    List<Book> findUnreadBooks(@Param("user") User user);
}
