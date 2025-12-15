package org.example.challengegenerator.controller;

import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatsController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/stats")
    public String stats(Model model) {
        List<DailyRecordEntity> allRecords = historyService.getAllHistory();

        // Общая статистика
        long total = allRecords.size();
        long completed = allRecords.stream().filter(DailyRecordEntity::isCompleted).count();
        long pending = total - completed;
        double completionRate = total > 0 ? (completed * 100.0 / total) : 0;

        // Статистика за 30 дней
        List<DailyRecordEntity> last30Days = getLast30DaysRecords(allRecords);
        long last30Total = last30Days.size();
        long last30Completed = last30Days.stream().filter(DailyRecordEntity::isCompleted).count();
        double last30Rate = last30Total > 0 ? (last30Completed * 100.0 / last30Total) : 0;

        // Группировка по типам
        Map<String, Long> byType = allRecords.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getType().name(),
                        Collectors.counting()
                ));

        // Группировка по сложности
        Map<String, Long> byDifficulty = allRecords.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getDifficulty().name(),
                        Collectors.counting()
                ));

        // Самый популярный тип
        String mostPopularType = byType.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");

        // Среднее время выполнения
        double avgDuration = allRecords.stream()
                .mapToInt(DailyRecordEntity::getDurationMinutes)
                .average()
                .orElse(0);

        // Передаем данные в модель как числа для сравнения
        model.addAttribute("total", total);
        model.addAttribute("completed", completed);
        model.addAttribute("pending", pending);
        model.addAttribute("completionRate", completionRate); // Теперь double

        model.addAttribute("last30Total", last30Total);
        model.addAttribute("last30Completed", last30Completed);
        model.addAttribute("last30Rate", last30Rate);

        model.addAttribute("byType", byType);
        model.addAttribute("byDifficulty", byDifficulty);
        model.addAttribute("mostPopularType", mostPopularType);
        model.addAttribute("avgDuration", avgDuration);

        return "stats";
    }

    private List<DailyRecordEntity> getLast30DaysRecords(List<DailyRecordEntity> allRecords) {
        LocalDate thirtyDaysAgo = LocalDate.now().minus(30, ChronoUnit.DAYS);
        return allRecords.stream()
                .filter(record -> !record.getDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.toList());
    }
}