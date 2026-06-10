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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        return time.equals(other.getTime());
    };
}

