package org.example.challengegenerator.patterns.decorator;

import org.example.challengegenerator.models.Challenge;

public class TimerChallengeDecorator extends ChallengeDecorator {

    public TimerChallengeDecorator(Challenge challenge) {
        super(challenge);
    }

    @Override
    protected String getEnhancedTitle() {
        return  challenge.getTitle();
    }

    @Override
    protected String getEnhancedDescription() {
        return challenge.getDescription() +
                "\n\nВремя на выполнение: " +
                challenge.getDurationMinutes() + " минут";
    }
}