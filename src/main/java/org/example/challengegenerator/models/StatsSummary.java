package org.example.challengegenerator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class StatsSummary {
    private long total;
    private long completed;
    private long pending;
    private double completionRate;

    private long last30Total;
    private long last30Completed;
    private double last30Rate;

    private Map<String, Long> byType;
    private Map<String, Long> byDifficulty;

    private String mostPopularType;
    private double avgDuration;
}
