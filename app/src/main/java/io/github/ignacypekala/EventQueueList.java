package io.github.ignacypekala;

public class EventQueueList implements EventQueue {
    private class EventListNode {
        Event event;
        EventListNode next;

        public EventListNode(Event event, EventListNode next) {
            this.event = event;
            this.next = next;
        }

        public Event getEvent() {
            return event;
        }

        public EventListNode getNext() {
            return next;
        }

        public void setNext(EventListNode node) {
            next = node;
        }
    }
    EventListNode start;

    public EventQueueList() {
        start = null;
    }

    @Override
    public void enqueue(Event event) {
        EventListNode previous = null;
        EventListNode current = start;
        while (
            current != null && current.getEvent().getTime() < event.getTime()
        ) {
            previous = current;
            current = current.getNext();
        }
        EventListNode newNode = new EventListNode(event, current);
        if (previous == null) {
            start = newNode;
        } else {
            previous.setNext(newNode);
        }
    }

    @Override
    public Event dequeue() {
        Event event = start.getEvent();
        start = start.getNext();
        return event;
    }

}
