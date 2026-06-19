package io.github.ignacypekala.simulation;

import io.github.ignacypekala.utils.Time;

public class SimulationClock implements Clock {
    private final Time START_TIME = new Time(9, 0, 0);
    private final Time END_TIME = new Time(15, 0, 0);
    private Time time;

    public SimulationClock() {
        time = new Time(9, 0, 0);
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public Time getStartTime() {
        return START_TIME;
    }

    @Override
    public Time getEndTime() {
        return END_TIME;
    }

    @Override
    public boolean isTimeUp() {
        return END_TIME.compareTo(time) <= 0;
    }

    public void setTime(final Time newTime) {
        time = newTime;
    }

}
