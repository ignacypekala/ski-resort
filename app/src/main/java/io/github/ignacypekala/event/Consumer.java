package io.github.ignacypekala.event;

public interface Consumer {
    public Event poll() throws IllegalStateException;

    public boolean hasEvents();
}
