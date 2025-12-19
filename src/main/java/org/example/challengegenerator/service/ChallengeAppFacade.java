package org.example.challengegenerator.service;

import org.example.challengegenerator.models.*;
import org.example.challengegenerator.patterns.decorator.MotivationDecorator;
import org.example.challengegenerator.patterns.decorator.TimerDecorator;
import org.example.challengegenerator.patterns.factory.ChallengeFactory;
import org.example.challengegenerator.patterns.strategy.BalancedTemplateStrategy;
import org.example.challengegenerator.patterns.strategy.RandomTemplateStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ChallengeAppFacade {

    private final ChallengeService challengeService;
    private final HistoryService historyService;

    private final RandomTemplateStrategy randomTemplateStrategy;
    private final BalancedTemplateStrategy balancedTemplateStrategy;

    // ВАЖНО: зависим от интерфейса (SOLID: DIP), а не от конкретного класса
    private final ChallengeFactory templateFactory;

    public ChallengeAppFacade(ChallengeService challengeService,
                              HistoryService historyService,
                              RandomTemplateStrategy randomTemplateStrategy,
                              BalancedTemplateStrategy balancedTemplateStrategy,
                              ChallengeFactory templateFactory) {
        this.challengeService = challengeService;
        this.historyService = historyService;
        this.randomTemplateStrategy = randomTemplateStrategy;
        this.balancedTemplateStrategy = balancedTemplateStrategy;
        this.templateFactory = templateFactory;
    }

    // ✅ Основной сценарий генерации, его будет дергать кнопка /generate
    @Transactional
    public DailyRecordEntity generate(ChallengeType type,
                                      Difficulty difficulty,
                                      GenerationMode mode,
                                      boolean addTimer,
                                      boolean addMotivation) {

        ChallengeTemplateEntity template = switch (mode) {
            case BALANCED_TEMPLATE -> balancedTemplateStrategy.pickTemplate(type, difficulty);
            default -> randomTemplateStrategy.pickTemplate(type, difficulty);
        };

        Challenge ch;
        if (template != null) {
            ch = templateFactory.fromTemplate(template);
        } else {
            // fallback если шаблонов нет (чтобы не ломалось)
            ch = buildFallbackChallenge(type, difficulty);
        }

        if (addTimer) ch = new TimerDecorator(ch).build();
        if (addMotivation) ch = new MotivationDecorator(ch).build();

        Challenge saved = challengeService.saveChallenge(ch);
        return historyService.saveChallengeAsRecord(saved);

    }

    // ✅ Daily кнопка (/generate-daily): не зависит от ChallengeService
    @Transactional
    public DailyRecordEntity generateDaily() {
        ChallengeType dailyType = computeDailyType();
        Difficulty dailyDifficulty = computeDailyDifficulty();

        // например: daily всегда random + timer
        return generate(dailyType, dailyDifficulty,
                GenerationMode.RANDOM_TEMPLATE,
                true,
                false);
    }

    public DailyRecordEntity getTodayRecord() {
        return historyService.getTodayRecord();
    }

    @Transactional
    public void completeTodayChallenge() {
        historyService.markTodayCompleted();
    }

    // История
    public java.util.List<DailyRecordEntity> getAllHistory() {
        return historyService.getAllHistory();
    }

    @Transactional
    public DailyRecordEntity markAsCompleted(Long recordId) {
        return historyService.markRecordAsCompleted(recordId);
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        historyService.deleteRecord(recordId);
    }

    // --- helpers ---

    private Challenge buildFallbackChallenge(ChallengeType type, Difficulty difficulty) {
        Challenge ch = new Challenge();
        ch.setType(type);
        ch.setDifficulty(difficulty);

        // простая логика, чтобы всегда что-то сгенерировать
        ch.setTitle(type + " Challenge (" + difficulty + ")");
        ch.setDescription("Generated without templates (fallback).");

        ch.setDurationMinutes(switch (difficulty) {
            case EASY -> 10;
            case MEDIUM -> 20;
            case HARD -> 30;
        });

        return ch;
    }

    private ChallengeType computeDailyType() {
        int day = LocalDate.now().getDayOfWeek().getValue();
        return switch (day) {
            case 1, 5 -> ChallengeType.HEALTH;
            case 2, 6 -> ChallengeType.STUDY;
            case 3 -> ChallengeType.PRODUCTIVITY;
            default -> ChallengeType.HABIT;
        };
    }

    private Difficulty computeDailyDifficulty() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        if (dayOfMonth <= 10) return Difficulty.EASY;
        if (dayOfMonth <= 20) return Difficulty.MEDIUM;
        return Difficulty.HARD;
    }
    public long getTotalChallengesCount() {
        return historyService.getTotalCount();
    }

    public long getCompletedChallengesCount() {
        return historyService.getCompletedCount();
    }

}
