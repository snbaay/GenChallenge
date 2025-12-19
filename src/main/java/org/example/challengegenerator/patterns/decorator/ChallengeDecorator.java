package org.example.challengegenerator.patterns.decorator;

import org.example.challengegenerator.models.Challenge;

public abstract class ChallengeDecorator {
    protected final Challenge base;

    protected ChallengeDecorator(Challenge base) {
        this.base = base;
    }

    public Challenge build() {
        return base;
    }
}
