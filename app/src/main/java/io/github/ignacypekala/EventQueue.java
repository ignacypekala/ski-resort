package io.github.ignacypekala;

public interface EventQueue {
    public void enqueue(Event event);
    public Event dequeue();
}
