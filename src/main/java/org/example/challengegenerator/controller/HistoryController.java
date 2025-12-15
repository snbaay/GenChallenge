package org.example.challengegenerator.controller;

import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.service.ChallengeAppFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/history")
public class HistoryController {

    private final ChallengeAppFacade facade;

    @Autowired
    public HistoryController(ChallengeAppFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public String getHistoryPage(Model model) {
        List<DailyRecordEntity> records = facade.getAllHistory();

        // Статистика
        int totalChallenges = records.size();
        long completedChallenges = records.stream()
                .filter(DailyRecordEntity::isCompleted)
                .count();
        int totalMinutes = records.stream()
                .mapToInt(DailyRecordEntity::getDurationMinutes)
                .sum();
        int successRate = totalChallenges > 0 ?
                (int) ((completedChallenges * 100) / totalChallenges) : 0;

        // Передаем данные в модель
        model.addAttribute("challenges", records);
        model.addAttribute("totalChallenges", totalChallenges);
        model.addAttribute("completedChallenges", completedChallenges);
        model.addAttribute("totalMinutes", totalMinutes);
        model.addAttribute("successRate", successRate);

        return "history";
    }

    @PostMapping("/complete/{id}")
    public String completeChallenge(@PathVariable Long id) {
        facade.markAsCompleted(id);
        return "redirect:/history";
    }

    @PostMapping("/delete/{id}")
    public String deleteChallenge(@PathVariable Long id) {
        facade.deleteChallenge(id);
        return "redirect:/history";
    }
}