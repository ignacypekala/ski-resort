package io.github.ignacypekala.event;

import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.utils.Time;

public abstract class RelativeEvent extends Event {
    public RelativeEvent(Clock clock, int delay) {
        super(Time.secondsLater(clock.getTime(), delay));
    }
}
