package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;

public interface EventProducer {
    public void enqueue(Event event);
}
