package com.deepflow.app.domain.sentence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {

    @Query("select s from Sentence s order by function('RAND')")
    List<Sentence> findRandom(Pageable pageable);

    @Query("select s.id from Sentence s")
    List<Long> findAllIds();
}
