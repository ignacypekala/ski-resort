package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;

public abstract class Event {
    private Time time;

    protected Event(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public abstract void handle();
}
