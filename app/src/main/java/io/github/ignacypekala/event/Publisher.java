package io.github.ignacypekala.event;

public interface Publisher {
    public void send(Event event);
}
