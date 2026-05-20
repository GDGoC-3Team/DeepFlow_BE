package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.reading.ReadingSession;
import com.deepflow.app.domain.settings.FontFamily;
import com.deepflow.app.domain.settings.FontFamilyConverter;
import com.deepflow.app.domain.sentence.Sentence;
import com.deepflow.app.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ReadingSession session;

    @Column(columnDefinition = "TEXT")
    private String selectedText;

    private String imageUrl;

    @Column(columnDefinition = "ENUM('NANUM_MYEONGJO','KOPUB_BATANG','NOTO_SANS')")
    @Convert(converter = FontFamilyConverter.class)
    private FontFamily fontFamily;

    private Integer fontSize;

    private String bookTitle;

    private String author;

    private LocalDateTime savedAt;

    public static SavedSentence of(User user, Sentence sentence) {
        SavedSentence savedSentence = new SavedSentence();
        savedSentence.user = user;
        savedSentence.sentence = sentence;
        savedSentence.selectedText = sentence.getContent();
        savedSentence.imageUrl = sentence.getImageUrl();
        savedSentence.bookTitle = sentence.getBookTitle();
        savedSentence.author = sentence.getAuthor();
        savedSentence.savedAt = LocalDateTime.now();
        return savedSentence;
    }

    public static SavedSentence fromReading(
            User user,
            ReadingSession session,
            String selectedText,
            String imageUrl,
            FontFamily fontFamily,
            int fontSize
    ) {
        SavedSentence savedSentence = new SavedSentence();
        savedSentence.user = user;
        savedSentence.session = session;
        savedSentence.selectedText = selectedText;
        savedSentence.imageUrl = imageUrl;
        savedSentence.fontFamily = fontFamily;
        savedSentence.fontSize = fontSize;
        savedSentence.bookTitle = session.getBook().getTitle();
        savedSentence.author = session.getBook().getAuthor();
        savedSentence.savedAt = LocalDateTime.now();
        return savedSentence;
    }

    public String getDisplayContent() {
        return sentence != null ? sentence.getContent() : selectedText;
    }

    public String getDisplayImageUrl() {
        return sentence != null ? sentence.getImageUrl() : imageUrl;
    }

    public String getDisplayBookTitle() {
        return sentence != null ? sentence.getBookTitle() : bookTitle;
    }

    public String getDisplayAuthor() {
        return sentence != null ? sentence.getAuthor() : author;
    }
}
