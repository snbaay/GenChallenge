package org.example.challengegenerator.patterns.factory;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeTemplateEntity;

public interface ChallengeFactory {
    Challenge fromTemplate(ChallengeTemplateEntity template);
}
