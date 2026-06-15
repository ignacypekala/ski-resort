package io.github.ignacypekala.event;

import java.util.PriorityQueue;

public class EventQueue implements Broker {
    private int nextPriority = 0;
    private final PriorityQueue<PrioritizedEvent> queue;

    public EventQueue() {
        queue = new PriorityQueue<PrioritizedEvent>();
    }

    private class PrioritizedEvent implements Comparable<PrioritizedEvent> {
        private final Event event;
        private final int priority;

        private PrioritizedEvent(final Event event, final int priority) {
            this.event = event;
            this.priority = priority;
        }

        @Override
        public int compareTo(final PrioritizedEvent other) {
            final int comparison = event.compareTo(other.getEvent());
            return comparison == 0 ? priority - other.getPriority() : comparison;
        }

        private Event getEvent() {
            return event;
        }
        private int getPriority() {
            return priority;
        }
    }

    @Override
    public void publish(final Event event) {
        queue.add(new PrioritizedEvent(event, nextPriority++));
    }

    @Override
    public Event poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Cannot poll an event from an empty queue.");
        }
        return queue.remove().getEvent();
    }

    @Override
    public boolean hasEvents() {
        return !queue.isEmpty();
    }
}
