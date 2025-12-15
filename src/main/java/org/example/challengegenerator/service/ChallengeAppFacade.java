package org.example.challengegenerator.service;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.models.Difficulty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChallengeAppFacade {

    private final ChallengeService challengeService;
    private final HistoryService historyService;

    @Autowired
    public ChallengeAppFacade(ChallengeService challengeService, HistoryService historyService) {
        this.challengeService = challengeService;
        this.historyService = historyService;
    }

    // Все методы должны использовать DailyRecordRepository напрямую
    // или через HistoryService, но с правильной логикой

    @Transactional
    public DailyRecordEntity generateWithFactory(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeService.generateWithFactory(type, difficulty);
        return historyService.saveChallengeAsRecord(challenge);
    }

    @Transactional
    public DailyRecordEntity generateWithStrategy(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeService.generateWithStrategy(type, difficulty);
        return historyService.saveChallengeAsRecord(challenge);
    }

    @Transactional
    public DailyRecordEntity generateWithDecorator(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeService.generateWithDecorator(type, difficulty);
        return historyService.saveChallengeAsRecord(challenge);
    }

    @Transactional
    public DailyRecordEntity generateDailyChallenge() {
        Challenge challenge = challengeService.generateDailyChallenge();
        return historyService.saveChallengeAsRecord(challenge);
    }

    @Transactional
    public DailyRecordEntity generateTodayChallenge(ChallengeType type, Difficulty difficulty) {
        Challenge challenge = challengeService.generateWithFactory(type, difficulty);
        return historyService.saveChallengeAsRecord(challenge);
    }

    public DailyRecordEntity getTodayRecord() {
        return historyService.getTodayRecord();
    }

    @Transactional
    public void completeTodayChallenge() {
        historyService.markTodayCompleted();
    }

    public List<DailyRecordEntity> getAllHistory() {
        return historyService.getAllHistory();
    }

    // РЕАЛЬНЫЕ МЕТОДЫ ДЛЯ HistoryController
    @Transactional
    public DailyRecordEntity markAsCompleted(Long recordId) {
        return historyService.markRecordAsCompleted(recordId);
    }

    @Transactional
    public void deleteChallenge(Long recordId) {
        historyService.deleteRecord(recordId);
    }

    // Дополнительный метод для статистики
    public long getTotalChallengesCount() {
        return historyService.getTotalCount();
    }

    public long getCompletedChallengesCount() {
        return historyService.getCompletedCount();
    }
}