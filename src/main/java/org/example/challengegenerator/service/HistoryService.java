package org.example.challengegenerator.service;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.repository.DailyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoryService {

    private final DailyRecordRepository dailyRecordRepository;

    @Autowired
    public HistoryService(DailyRecordRepository dailyRecordRepository) {
        this.dailyRecordRepository = dailyRecordRepository;
    }

    @Transactional
    public DailyRecordEntity saveChallengeAsRecord(Challenge challenge) {
        DailyRecordEntity record = new DailyRecordEntity(challenge);
        record.setDate(LocalDate.now());
        return dailyRecordRepository.save(record);
    }

    // ✅ теперь НЕ findByDate(), а берем последнюю запись за сегодня
    public DailyRecordEntity getTodayRecord() {
        LocalDate today = LocalDate.now();
        return dailyRecordRepository.findFirstByDateOrderByIdDesc(today);
    }

    @Transactional
    public void markTodayCompleted() {
        DailyRecordEntity todayRecord = getTodayRecord();
        if (todayRecord != null) {
            todayRecord.setCompleted(true);
            dailyRecordRepository.save(todayRecord);
        }
    }

    @Transactional
    public DailyRecordEntity markRecordAsCompleted(Long recordId) {
        DailyRecordEntity record = dailyRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        record.setCompleted(true);
        return dailyRecordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        dailyRecordRepository.deleteById(recordId);
    }

    public List<DailyRecordEntity> getAllHistory() {
        return dailyRecordRepository.findAllByOrderByDateDesc();
    }

    public long getTotalCount() {
        return dailyRecordRepository.count();
    }

    public long getCompletedCount() {
        return dailyRecordRepository.countByCompletedTrue();
    }

}
