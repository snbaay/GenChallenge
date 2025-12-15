package org.example.challengegenerator.patterns.decorator;

import org.example.challengegenerator.models.Challenge;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;

public abstract class ChallengeDecorator {
    protected Challenge challenge;

    public ChallengeDecorator(Challenge challenge) {
        this.challenge = challenge;
    }

    public Challenge decorate() {
        // Создаем новый объект и устанавливаем поля
        Challenge decorated = new Challenge();
        decorated.setTitle(getEnhancedTitle());
        decorated.setDescription(getEnhancedDescription());
        decorated.setType(challenge.getType());
        decorated.setDifficulty(challenge.getDifficulty());
        decorated.setDurationMinutes(challenge.getDurationMinutes());
        decorated.setCompleted(challenge.isCompleted());

        return decorated;
    }

    protected abstract String getEnhancedTitle();
    protected abstract String getEnhancedDescription();
}