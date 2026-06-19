package io.github.ignacypekala;

import io.github.ignacypekala.event.Event;

public class DayStart extends Event {
    private final Skier skier;

    public DayStart(final Skier skier) {
        super(skier.getStartTime());
        this.skier = skier;
    }

    @Override
    public void handle() {
        skier.decideAndRide();
    }

    @Override
    public String toString() {
        return String.format(
            "%s has arrived at %s",
            skier,
            skier.getStartPoint());
    }
}
