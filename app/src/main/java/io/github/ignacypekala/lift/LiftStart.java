package io.github.ignacypekala.lift;

import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.event.Event;

class LiftStart extends Event {
    private final Lift lift;

    public LiftStart(final Lift lift, final Clock clock) {
        super(clock.getStartTime());
        this.lift = lift;
    }

    public void handle() {
        lift.depart();
    }

    public String toString() {
        return String.format("%s has started", lift);
    }
}
