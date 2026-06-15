package io.github.ignacypekala.event;

import java.util.PriorityQueue;

public class EventQueue implements Broker {
    private PriorityQueue<Event> queue;

    public EventQueue() {
        queue = new PriorityQueue<Event>();
    }

    @Override
    public void publish(Event event) {
        queue.add(event);
    }

    @Override
    public Event poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot poll an event from an empty queue.");
        }
        return queue.poll();
    }

    @Override
    public boolean hasEvents() {
        return !queue.isEmpty();
    }
}
