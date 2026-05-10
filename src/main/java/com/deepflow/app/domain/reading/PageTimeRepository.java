package com.deepflow.app.domain.reading;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageTimeRepository extends JpaRepository<PageTime, Long> {

    List<PageTime> findBySessionOrderByPageNumberAsc(ReadingSession session);

    Optional<PageTime> findTopBySessionOrderByIdDesc(ReadingSession session);
}
