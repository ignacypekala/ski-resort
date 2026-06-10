package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.Time;

public abstract class Event {
    private final Time time;

    protected Event(final Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public abstract void handle();
}
