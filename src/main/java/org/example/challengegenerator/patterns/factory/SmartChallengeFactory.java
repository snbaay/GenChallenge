package org.example.challengegenerator.patterns.factory;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.repository.ChallengeTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class SmartChallengeFactory implements ChallengeFactory {

    private final ChallengeTemplateRepository templateRepository;
    private final Random random = new Random();

    @Autowired
    public SmartChallengeFactory(ChallengeTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Challenge createChallenge(ChallengeType type, Difficulty difficulty) {
        // Используем стратегию выбора: сначала из шаблонов, потом дефолт
        List<ChallengeTemplateEntity> templates =
                templateRepository.findByTypeAndDifficulty(type, difficulty);

        if (templates != null && !templates.isEmpty()) {
            // Стратегия: берем случайный шаблон
            ChallengeTemplateEntity template = templates.get(
                    random.nextInt(templates.size())
            );

            // Используем сеттеры вместо конструктора
            Challenge challenge = new Challenge();
            challenge.setTitle(template.getTitle());
            challenge.setDescription(template.getDescription());
            challenge.setType(template.getType());
            challenge.setDifficulty(template.getDifficulty());
            challenge.setDurationMinutes(template.getDefaultDurationMinutes());
            challenge.setCreatedDate(LocalDateTime.now());
            challenge.setCompleted(false);

            return challenge;
        } else {
            // Стратегия: дефолтная генерация
            return createDefaultChallenge(type, difficulty);
        }
    }

    private Challenge createDefaultChallenge(ChallengeType type, Difficulty difficulty) {
        String title = "Задача: " + getTypeName(type) + " (" + difficulty + ")";
        String description = "Выполните задачу по " + getTypeName(type).toLowerCase();
        int duration = getDurationByDifficulty(difficulty);

        // Используем сеттеры вместо конструктора
        Challenge challenge = new Challenge();
        challenge.setTitle(title);
        challenge.setDescription(description);
        challenge.setType(type);
        challenge.setDifficulty(difficulty);
        challenge.setDurationMinutes(duration);
        challenge.setCreatedDate(LocalDateTime.now());
        challenge.setCompleted(false);

        return challenge;
    }

    private String getTypeName(ChallengeType type) {
        return switch (type) {
            case HEALTH -> "Здоровье";
            case STUDY -> "Учеба";
            case PRODUCTIVITY -> "Продуктивность";
            case HABIT -> "Привычка";
            case RANDOM -> "Random";
        };
    }

    private int getDurationByDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 15;
            case MEDIUM -> 30;
            case HARD -> 60;
        };
    }
}