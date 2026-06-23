package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.Time;

public class TestEvent extends Event {
    public TestEvent(Time time) {
        super(time);
    }

    @Override
    public void handle() {
    }

    @Override
    public String toString() {
        return super.getTime().toString();
    }
}
