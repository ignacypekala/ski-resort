package io.github.ignacypekala.EventQueue;

public interface EventConsumer {
    public Event dequeue() throws IllegalStateException;
    public boolean isEmpty();
}
