package io.github.ignacypekala.utils;

import io.github.ignacypekala.simulation.Clock;

public class Reporter {
    private final Clock clock;

    public Reporter(final Clock clock) {
        this.clock = clock;
    }

    public void report(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }
        message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
        System.out.println(String.format("%s: %s", clock.getTime(), message));
    }
}
