package org.example.challengegenerator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_templates")
@Data
@NoArgsConstructor
public class ChallengeTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // тип челленджа (HEALTH, STUDY и т.д.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    // уровень сложности (EASY, MEDIUM, HARD)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "default_duration_minutes", nullable = false)
    private int defaultDurationMinutes;

    public ChallengeTemplateEntity(ChallengeType type,
                                   Difficulty difficulty,
                                   String title,
                                   String description,
                                   int defaultDurationMinutes) {
        this.type = type;
        this.difficulty = difficulty;
        this.title = title;
        this.description = description;
        this.defaultDurationMinutes = defaultDurationMinutes;
    }
}
