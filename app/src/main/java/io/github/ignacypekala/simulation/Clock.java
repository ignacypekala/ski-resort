package io.github.ignacypekala.simulation;

import io.github.ignacypekala.utils.Time;

public interface Clock {
    public Time getTime();

    public Time getStartTime();

    public Time getEndTime();

    public boolean isTimeUp();
}
