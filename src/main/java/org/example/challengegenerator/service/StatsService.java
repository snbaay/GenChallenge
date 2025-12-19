package org.example.challengegenerator.service;

import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.models.StatsSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final HistoryService historyService;

    public StatsService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public StatsSummary buildStats() {
        List<DailyRecordEntity> allRecords = historyService.getAllHistory();

        long total = allRecords.size();
        long completed = allRecords.stream().filter(DailyRecordEntity::isCompleted).count();
        long pending = total - completed;
        double completionRate = total > 0 ? (completed * 100.0 / total) : 0;

        List<DailyRecordEntity> last30Days = getLast30DaysRecords(allRecords);
        long last30Total = last30Days.size();
        long last30Completed = last30Days.stream().filter(DailyRecordEntity::isCompleted).count();
        double last30Rate = last30Total > 0 ? (last30Completed * 100.0 / last30Total) : 0;

        Map<String, Long> byType = allRecords.stream()
                .filter(r -> r.getType() != null)
                .collect(Collectors.groupingBy(r -> r.getType().name(), Collectors.counting()));

        Map<String, Long> byDifficulty = allRecords.stream()
                .filter(r -> r.getDifficulty() != null)
                .collect(Collectors.groupingBy(r -> r.getDifficulty().name(), Collectors.counting()));

        String mostPopularType = byType.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");

        double avgDuration = allRecords.stream()
                .mapToInt(DailyRecordEntity::getDurationMinutes)
                .average()
                .orElse(0);

        return new StatsSummary(
                total, completed, pending, completionRate,
                last30Total, last30Completed, last30Rate,
                byType, byDifficulty,
                mostPopularType, avgDuration
        );
    }

    private List<DailyRecordEntity> getLast30DaysRecords(List<DailyRecordEntity> allRecords) {
        LocalDate thirtyDaysAgo = LocalDate.now().minus(30, ChronoUnit.DAYS);
        return allRecords.stream()
                .filter(record -> record.getDate() != null && !record.getDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.toList());
    }
}
