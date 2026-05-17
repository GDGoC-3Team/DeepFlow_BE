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
    Optional<SavedSentence> findByUserAndSentenceId(User user, Long sentenceId);

    // 최신순 정렬
    List<SavedSentence> findByUserOrderBySavedAtDesc(User user);

    // 날짜순 정렬
    @Query("select ss from SavedSentence ss where ss.user = :user order by ss.savedAt asc")
    List<SavedSentence> findByUserOrderBySentenceDate(@Param("user") User user);

    // 가나다순 정렬
    @Query("select ss from SavedSentence ss where ss.user = :user order by ss.sentence.content asc")
    List<SavedSentence> findByUserOrderBySentenceContent(@Param("user") User user);

    // 문장 타입 + 최신순 조회
    @Query("""
            select ss from SavedSentence ss
            where ss.user = :user
              and (
                :type = 'all'
                or (:type = 'image' and ss.sentence.imageUrl is not null and trim(ss.sentence.imageUrl) <> '')
                or (:type = 'text' and (ss.sentence.imageUrl is null or trim(ss.sentence.imageUrl) = ''))
              )
            order by ss.savedAt desc
            """)
    List<SavedSentence> findByUserAndTypeOrderBySavedAtDesc(@Param("user") User user, @Param("type") String type);

    // 문장 타입 + 날짜순 조회
    @Query("""
            select ss from SavedSentence ss
            where ss.user = :user
              and (
                :type = 'all'
                or (:type = 'image' and ss.sentence.imageUrl is not null and trim(ss.sentence.imageUrl) <> '')
                or (:type = 'text' and (ss.sentence.imageUrl is null or trim(ss.sentence.imageUrl) = ''))
              )
            order by ss.savedAt asc
            """)
    List<SavedSentence> findByUserAndTypeOrderBySentenceDate(@Param("user") User user, @Param("type") String type);

    // 문장 타입 + 가나다순 조회
    @Query("""
            select ss from SavedSentence ss
            where ss.user = :user
              and (
                :type = 'all'
                or (:type = 'image' and ss.sentence.imageUrl is not null and trim(ss.sentence.imageUrl) <> '')
                or (:type = 'text' and (ss.sentence.imageUrl is null or trim(ss.sentence.imageUrl) = ''))
              )
            order by ss.sentence.content asc
            """)
    List<SavedSentence> findByUserAndTypeOrderBySentenceContent(@Param("user") User user, @Param("type") String type);
}
