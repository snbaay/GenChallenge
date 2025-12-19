package org.example.challengegenerator.patterns.decorator;

import org.example.challengegenerator.models.Challenge;

public class MotivationDecorator extends ChallengeDecorator {

    public MotivationDecorator(Challenge base) {
        super(base);
    }

    @Override
    public Challenge build() {
        String extra = "\nMotivation: Small steps every day!";
        base.setDescription((base.getDescription() == null ? "" : base.getDescription()) + extra);
        return base;
    }
}
