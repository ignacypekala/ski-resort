package io.github.ignacypekala.simulation;

import io.github.ignacypekala.utils.Time;

public interface Clock {
    public Time getCurrentTime();

    public Time getStartTime();

    public Time getEndTime();
}
