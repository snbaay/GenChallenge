package org.example.challengegenerator.patterns.strategy;

import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.repository.ChallengeTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RandomTemplateStrategy implements TemplateSelectionStrategy {

    private final ChallengeTemplateRepository repo;
    private final Random random = new Random();

    public RandomTemplateStrategy(ChallengeTemplateRepository repo) {
        this.repo = repo;
    }

    @Override
    public ChallengeTemplateEntity pickTemplate(ChallengeType type, Difficulty difficulty) {
        List<ChallengeTemplateEntity> list = repo.findByTypeAndDifficulty(type, difficulty);
        if (list.isEmpty()) return null;
        return list.get(random.nextInt(list.size()));
    }
}
