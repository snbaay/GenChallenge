package org.example.challengegenerator.service;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.patterns.decorator.TimerDecorator;
import org.example.challengegenerator.patterns.factory.ChallengeFactory;
import org.example.challengegenerator.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    // МЕТОД ДЛЯ ИСТОРИИ
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    // Сохранить челлендж (Facade уже подготовит объект через Strategy+Factory+Decorator)
    @Transactional
    public Challenge saveChallenge(Challenge challenge) {
        if (challenge.getCreatedDate() == null) {
            challenge.setCreatedDate(LocalDateTime.now());
        }
        return challengeRepository.save(challenge);
    }

    // Отметить выполненным
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
}
