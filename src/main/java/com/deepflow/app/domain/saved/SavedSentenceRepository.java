package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.Sentence;
import com.deepflow.app.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedSentenceRepository extends JpaRepository<SavedSentence, Long> {

    Optional<SavedSentence> findByUserAndSentence(User user, Sentence sentence);

    Optional<SavedSentence> findByIdAndUser(Long id, User user);

    List<SavedSentence> findByUserOrderBySavedAtDesc(User user);
}
