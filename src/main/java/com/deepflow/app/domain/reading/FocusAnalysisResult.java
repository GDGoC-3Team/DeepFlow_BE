package com.deepflow.app.domain.reading;

import com.deepflow.app.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "focus_analysis_results",
        uniqueConstraints = @UniqueConstraint(name = "uk_focus_analysis_results_session", columnNames = "session_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FocusAnalysisResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private ReadingSession session;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String resultJson;

    public static FocusAnalysisResult of(ReadingSession session, String resultJson) {
        FocusAnalysisResult result = new FocusAnalysisResult();
        result.session = session;
        result.resultJson = resultJson;
        return result;
    }
}
