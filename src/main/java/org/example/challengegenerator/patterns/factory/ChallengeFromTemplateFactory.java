package org.example.challengegenerator.patterns.factory;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChallengeFromTemplateFactory implements ChallengeFactory {

    @Override
    public Challenge fromTemplate(ChallengeTemplateEntity t) {
        Challenge ch = new Challenge();
        ch.setTitle(t.getTitle());
        ch.setDescription(t.getDescription());
        ch.setType(t.getType());
        ch.setDifficulty(t.getDifficulty());
        ch.setDurationMinutes(t.getDefaultDurationMinutes());
        ch.setCreatedDate(LocalDateTime.now());
        ch.setCompleted(false);
        return ch;
    }
}
