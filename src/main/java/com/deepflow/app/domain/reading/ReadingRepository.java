package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingRepository extends JpaRepository<ReadingSession, Long> {

    Optional<ReadingSession> findByUserAndDate(User user, LocalDate date);

    List<ReadingSession> findByUserAndCompletedAtIsNotNullOrderByDateDesc(User user);

    @Query("select distinct rs.date from ReadingSession rs where rs.user = :user and rs.completedAt is not null order by rs.date")
    List<LocalDate> findCompletedDates(@Param("user") User user);
}
