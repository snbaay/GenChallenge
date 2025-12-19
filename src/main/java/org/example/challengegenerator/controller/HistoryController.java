package org.example.challengegenerator.controller;

import org.example.challengegenerator.models.DailyRecordEntity;
import org.example.challengegenerator.service.ChallengeAppFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final ChallengeAppFacade facade;

    public HistoryController(ChallengeAppFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public String historyPage(Model model) {
        List<DailyRecordEntity> records = facade.getAllHistory();

        long total = facade.getTotalChallengesCount();
        long completed = facade.getCompletedChallengesCount();
        int successRate = total > 0 ? (int)((completed * 100) / total) : 0;

        model.addAttribute("records", records);
        model.addAttribute("total", total);
        model.addAttribute("completed", completed);
        model.addAttribute("successRate", successRate);

        return "history";
    }

    @PostMapping("/complete/{id}")
    public String completeRecord(@PathVariable("id") Long id) {
        facade.markAsCompleted(id);
        return "redirect:/history";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecord(@PathVariable("id") Long id) {
        facade.deleteRecord(id);
        return "redirect:/history";
    }
}
