package org.example.challengegenerator.controller;

import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.example.challengegenerator.service.ChallengeAppFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChallengeViewController {

    @Autowired
    private ChallengeAppFacade facade;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("today", facade.getTodayRecord());
        model.addAttribute("types", ChallengeType.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("records", facade.getAllHistory());
        return "today";
    }

    @PostMapping("/generate")
    public String generateToday(@RequestParam ChallengeType type,
                                @RequestParam Difficulty difficulty) {
        facade.generateTodayChallenge(type, difficulty);
        return "redirect:/";
    }

    @PostMapping("/generate-daily")
    public String generateDaily() {
        facade.generateDailyChallenge();
        return "redirect:/";
    }

    @PostMapping("/complete")
    public String completeToday() {
        facade.completeTodayChallenge();
        return "redirect:/";
    }

    // УДАЛИ ЭТОТ МЕТОД - теперь его обрабатывает HistoryController
    // @GetMapping("/history")
    // public String history(Model model) {
    //     model.addAttribute("records", facade.getAllHistory());
    //     return "history";
    // }
}