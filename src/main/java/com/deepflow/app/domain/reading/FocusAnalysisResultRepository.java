package com.deepflow.app.domain.reading;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FocusAnalysisResultRepository extends JpaRepository<FocusAnalysisResult, Long> {

    Optional<FocusAnalysisResult> findBySession(ReadingSession session);
}
