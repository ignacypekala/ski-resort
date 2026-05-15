package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;

public interface EventPublisher {
    public void send(Event event);
}
