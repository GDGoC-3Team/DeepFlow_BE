package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.Sentence;
import com.deepflow.app.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavedSentenceRepository extends JpaRepository<SavedSentence, Long> {

    Optional<SavedSentence> findByUserAndSentence(User user, Sentence sentence);

    List<SavedSentence> findByUserOrderBySavedAtDesc(User user);

    @Query("select ss from SavedSentence ss where ss.user = :user order by ss.sentence.createdAt desc")
    List<SavedSentence> findByUserOrderBySentenceDate(@Param("user") User user);

    @Query("select ss from SavedSentence ss where ss.user = :user order by ss.sentence.content asc")
    List<SavedSentence> findByUserOrderBySentenceContent(@Param("user") User user);
}
