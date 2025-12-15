package org.example.challengegenerator.service;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.patterns.decorator.TimerChallengeDecorator;
import org.example.challengegenerator.patterns.factory.ChallengeFactory;
import org.example.challengegenerator.patterns.strategy.ChallengeGenerationStrategy;
import org.example.challengegenerator.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeFactory challengeFactory;
    private final ChallengeGenerationStrategy templateStrategy;
    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeFactory challengeFactory,
                            ChallengeGenerationStrategy templateStrategy,
                            ChallengeRepository challengeRepository) {
        this.challengeFactory = challengeFactory;
        this.templateStrategy = templateStrategy;
        this.challengeRepository = challengeRepository;
    }

    // МЕТОД ДЛЯ ИСТОРИИ
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    // Создать и сохранить челлендж
    @Transactional
    public Challenge generateAndSaveChallenge(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeFactory.createChallenge(type, difficulty);
        challenge.setCreatedDate(LocalDateTime.now());
        challenge.setCompleted(false);
        return challengeRepository.save(challenge);
    }

    // 1. Используем только Factory + сохраняем
    @Transactional
    public Challenge generateWithFactory(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeFactory.createChallenge(type, difficulty);
        challenge.setCreatedDate(LocalDateTime.now());
        return challengeRepository.save(challenge);
    }

    // 2. Используем только Strategy + сохраняем
    @Transactional
    public Challenge generateWithStrategy(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = templateStrategy.generate(type, difficulty);
        challenge.setCreatedDate(LocalDateTime.now());
        return challengeRepository.save(challenge);
    }

    // 3. Используем Factory + Decorator + сохраняем
    @Transactional
    public Challenge generateWithDecorator(ChallengeType type, Difficulty difficulty) {
        Challenge baseChallenge = challengeFactory.createChallenge(type, difficulty);
        TimerChallengeDecorator decorator = new TimerChallengeDecorator(baseChallenge);
        Challenge decorated = decorator.decorate();
        decorated.setCreatedDate(LocalDateTime.now());
        return challengeRepository.save(decorated);
    }

    // 4. Комбинированный метод для ежедневного челленджа + сохраняем
    @Transactional
    public Challenge generateDailyChallenge() {
        ChallengeType dailyType = getDailyType();
        Difficulty dailyDifficulty = getDailyDifficulty();

        Challenge challenge = challengeFactory.createChallenge(dailyType, dailyDifficulty);
        TimerChallengeDecorator decorator = new TimerChallengeDecorator(challenge);
        Challenge decorated = decorator.decorate();
        decorated.setCreatedDate(LocalDateTime.now());
        return challengeRepository.save(decorated);
    }

    // Метод для отметки челленджа как выполненного
    @Transactional
    public Challenge markAsCompleted(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));
        challenge.setCompleted(true);
        challenge.setCompletedAt(LocalDateTime.now());
        return challengeRepository.save(challenge);
    }

    // Статистика
    public long getTotalChallenges() {
        return challengeRepository.count();
    }

    public long getCompletedChallenges() {
        return challengeRepository.countByCompleted(true);
    }

    public int getTotalMinutes() {
        return challengeRepository.findAll().stream()
                .mapToInt(Challenge::getDurationMinutes)
                .sum();
    }

    public int getSuccessRate() {
        long total = getTotalChallenges();
        long completed = getCompletedChallenges();
        return total > 0 ? (int) ((completed * 100) / total) : 0;
    }

    private ChallengeType getDailyType() {
        int day = java.time.LocalDate.now().getDayOfWeek().getValue();
        return switch (day) {
            case 1, 5 -> ChallengeType.HEALTH;
            case 2, 6 -> ChallengeType.STUDY;
            case 3 -> ChallengeType.PRODUCTIVITY;
            default -> ChallengeType.HABIT;
        };
    }

    private Difficulty getDailyDifficulty() {
        int dayOfMonth = java.time.LocalDate.now().getDayOfMonth();
        if (dayOfMonth <= 10) return Difficulty.EASY;
        else if (dayOfMonth <= 20) return Difficulty.MEDIUM;
        else return Difficulty.HARD;
    }

}