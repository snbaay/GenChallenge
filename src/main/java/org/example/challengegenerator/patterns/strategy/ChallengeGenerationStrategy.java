package org.example.challengegenerator.patterns.strategy;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;

public interface ChallengeGenerationStrategy {
    Challenge generate(ChallengeType type, Difficulty difficulty);
}