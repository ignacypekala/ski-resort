package io.github.ignacypekala.EventQueue;

public interface EventQueue {
    public void enqueue(Event event);
    public Event dequeue() throws IllegalStateException;
}
