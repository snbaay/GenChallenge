package org.example.challengegenerator.controller;

import org.example.challengegenerator.models.StatsSummary;
import org.example.challengegenerator.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        StatsSummary s = statsService.buildStats();

        model.addAttribute("total", s.getTotal());
        model.addAttribute("completed", s.getCompleted());
        model.addAttribute("pending", s.getPending());
        model.addAttribute("completionRate", s.getCompletionRate());

        model.addAttribute("last30Total", s.getLast30Total());
        model.addAttribute("last30Completed", s.getLast30Completed());
        model.addAttribute("last30Rate", s.getLast30Rate());

        model.addAttribute("byType", s.getByType());
        model.addAttribute("byDifficulty", s.getByDifficulty());
        model.addAttribute("mostPopularType", s.getMostPopularType());
        model.addAttribute("avgDuration", s.getAvgDuration());

        return "stats";
    }
}
