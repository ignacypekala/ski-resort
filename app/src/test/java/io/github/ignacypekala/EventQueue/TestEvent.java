package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;

public class TestEvent extends Event {
    public TestEvent(Time time) {
        super(time);
    }

    public void handle() {}

    public String toString() {
        return super.getTime().toString();
    }
}
