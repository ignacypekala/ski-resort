package io.github.ignacypekala.event;

import java.util.PriorityQueue;

public class EventQueue implements Broker {
    private int nextPriority = 0;
    private PriorityQueue<PrioritizedEvent> queue;

    public EventQueue() {
        queue = new PriorityQueue<PrioritizedEvent>();
    }

    private class PrioritizedEvent implements Comparable<PrioritizedEvent> {
        private final Event event;
        private final int priority;

        protected PrioritizedEvent(Event event, int priority) {
            this.event = event;
            this.priority = priority;
        }

        @Override
        public int compareTo(PrioritizedEvent other) {
            int comparison = event.compareTo(other.getEvent());
            return comparison == 0 ? priority - other.getPriority() : comparison;
        }

        protected Event getEvent() {
            return event;
        }
        protected int getPriority() {
            return priority;
        }
    }

    @Override
    public void publish(Event event) {
        queue.add(new PrioritizedEvent(event, nextPriority++));
    }

    @Override
    public Event poll() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Cannot poll an event from an empty queue.");
        }
        return queue.poll().getEvent();
    }

    @Override
    public boolean hasEvents() {
        return !queue.isEmpty();
    }
}
