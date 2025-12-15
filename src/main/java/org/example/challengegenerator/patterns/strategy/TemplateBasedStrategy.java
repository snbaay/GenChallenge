package org.example.challengegenerator.patterns.strategy;

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
public class TemplateBasedStrategy implements ChallengeGenerationStrategy {

    private final ChallengeTemplateRepository templateRepository;
    private final Random random = new Random();

    @Autowired
    public TemplateBasedStrategy(ChallengeTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Challenge generate(ChallengeType type, Difficulty difficulty) {
        List<ChallengeTemplateEntity> templates =
                templateRepository.findByTypeAndDifficulty(type, difficulty);

        if (templates.isEmpty()) {
            throw new IllegalArgumentException(
                    "Нет шаблонов для типа: " + type + " и сложности: " + difficulty
            );
        }

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
    }
}