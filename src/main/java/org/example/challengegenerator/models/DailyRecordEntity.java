package org.example.challengegenerator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_records")
@Data
@NoArgsConstructor
public class DailyRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private boolean completed = false;

    // Можно добавить конструктор с аргументами
    // В DailyRecordEntity.java должен быть этот конструктор:
    public DailyRecordEntity(Challenge challenge) {
        this.date = LocalDate.now();
        this.title = challenge.getTitle();
        this.description = challenge.getDescription();
        this.type = challenge.getType();
        this.difficulty = challenge.getDifficulty();
        this.durationMinutes = challenge.getDurationMinutes();
        this.completed = false;
    }
}
