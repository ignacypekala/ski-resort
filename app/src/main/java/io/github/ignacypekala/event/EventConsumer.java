package io.github.ignacypekala.event;

public interface EventConsumer {
    public Event poll() throws IllegalStateException;

    public boolean hasEvents();
}
