package io.github.ignacypekala.EventQueue;

public abstract class Event {
    private int time;

    protected Event(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public abstract void handle();
}
