package org.example.challengegenerator.patterns.strategy;

import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.repository.ChallengeTemplateRepository;
import org.example.challengegenerator.repository.DailyRecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalancedTemplateStrategy implements TemplateSelectionStrategy {

    private final ChallengeTemplateRepository templateRepo;
    private final DailyRecordRepository recordRepo;
    private final Random random = new Random();

    public BalancedTemplateStrategy(ChallengeTemplateRepository templateRepo,
                                    DailyRecordRepository recordRepo) {
        this.templateRepo = templateRepo;
        this.recordRepo = recordRepo;
    }

    @Override
    public ChallengeTemplateEntity pickTemplate(ChallengeType typeIgnored, Difficulty difficulty) {

        List<DailyRecordEntity> recent = recordRepo.findAllByOrderByDateDesc()
                .stream().limit(30).toList();

        Map<ChallengeType, Long> counts = recent.stream()
                .filter(r -> r.getType() != null)
                .collect(Collectors.groupingBy(DailyRecordEntity::getType, Collectors.counting()));

        ChallengeType bestType = Arrays.stream(ChallengeType.values())
                .min(Comparator.comparingLong(t -> counts.getOrDefault(t, 0L)))
                .orElse(ChallengeType.values()[0]);

        List<ChallengeTemplateEntity> list = templateRepo.findByTypeAndDifficulty(bestType, difficulty);
        if (list.isEmpty()) return null;
        return list.get(random.nextInt(list.size()));
    }
}
