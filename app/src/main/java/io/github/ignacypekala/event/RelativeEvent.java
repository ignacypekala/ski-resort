package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.*;

public abstract class RelativeEvent extends Event {
    public RelativeEvent(Clock clock, int delay) {
        super(Time.secondsLater(clock.getCurrentTime(), delay));
    }
}
