package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;

public class Simulation implements Clock {
    private Time time;
    private final Time endTime = new Time(15, 0, 0);

    public Simulation() {
        time = new Time(9, 0, 0);
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public Time getCurrentTime() {
        return time;
    }

    @Override
    public Time getEndTime() {
        return endTime;
    }
}
