package org.example.challengegenerator.patterns.strategy;

import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;

public interface TemplateSelectionStrategy {
    ChallengeTemplateEntity pickTemplate(ChallengeType type, Difficulty difficulty);
}
