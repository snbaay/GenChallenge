package org.example.challengegenerator.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private int durationMinutes;

    // Переименуй поле чтобы совпадало с репозиторием
    @Column(name = "created_date") // ИЛИ просто createdDate без @Column
    private LocalDateTime createdDate; // БЫЛО: createdAt

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    private boolean completed;

    public Challenge() {
        this.createdDate = LocalDateTime.now(); // БЫЛО: createdAt
        this.completed = false;
    }
}