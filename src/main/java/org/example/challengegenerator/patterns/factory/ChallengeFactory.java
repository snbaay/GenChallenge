package org.example.challengegenerator.patterns.factory;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;

public interface ChallengeFactory {
    Challenge createChallenge(ChallengeType type, Difficulty difficulty);
}