package io.github.ignacypekala.EventQueue;

public interface EventConsumer {
    public Event poll() throws IllegalStateException;
    public boolean hasEvents();
}
