package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.Sentence;
import com.deepflow.app.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "saved_sentences")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavedSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    private LocalDateTime savedAt;

    public static SavedSentence of(User user, Sentence sentence) {
        SavedSentence savedSentence = new SavedSentence();
        savedSentence.user = user;
        savedSentence.sentence = sentence;
        savedSentence.savedAt = LocalDateTime.now();
        return savedSentence;
    }
}
