package io.github.ignacypekala.lift;

import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.event.RelativeEvent;

class Departure extends RelativeEvent {
    private final Clock clock;
    private final Lift lift;

    public Departure(final Clock clock, final Lift lift) {
        super(clock, lift.getDepartureInterval());
        this.clock = clock;
        this.lift = lift;
    }

    public void handle() {
        if (!clock.isTimeUp()) {
            lift.depart();
        }
    }

    public String toString() {
        return String.format("%s has departed", lift);
    }
}
