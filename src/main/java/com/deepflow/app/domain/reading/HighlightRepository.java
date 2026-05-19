package com.deepflow.app.domain.reading;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighlightRepository extends JpaRepository<Highlight, Long> {

    List<Highlight> findBySessionOrderByStartOffsetAscIdAsc(ReadingSession session);

    Optional<Highlight> findByIdAndSession(Long id, ReadingSession session);
}
