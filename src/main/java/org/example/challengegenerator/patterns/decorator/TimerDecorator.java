package org.example.challengegenerator.patterns.decorator;

import org.example.challengegenerator.models.Challenge;

public class TimerDecorator extends ChallengeDecorator {

    public TimerDecorator(Challenge base) {
        super(base);
    }

    @Override
    public Challenge build() {
        String extra = "\n Time: " + base.getDurationMinutes() + " minutes.";
        base.setDescription((base.getDescription() == null ? "" : base.getDescription()) + extra);
        return base;
    }
}
