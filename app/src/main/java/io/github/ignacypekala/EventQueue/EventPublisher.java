package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;

public interface EventPublisher {
    public void enqueue(Event event);
}
