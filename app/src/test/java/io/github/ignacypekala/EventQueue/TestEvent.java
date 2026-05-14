package io.github.ignacypekala.EventQueue;

public class TestEvent extends Event {
    public TestEvent(int time) {
        super(time);
    }

    public void handle() {
    }

    public String toString() {
        return Integer.toString(super.getTime());
    }
}
