package io.github.ignacypekala;

public class EventQueueList implements EventQueue {
    public static class EventListNode {
        Event event;
        EventListNode next;

        public EventListNode(Event event) {
            this.event = event;
        }

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

    EventListNode head;

    public EventQueueList() {
        head = null;
    }

    @Override
    public void enqueue(Event event) {
        EventListNode previous = null;
        EventListNode current = head;
        while (current != null && current.getEvent().getTime() < event.getTime()) {
            previous = current;
            current = current.getNext();
        }
        EventListNode newNode = new EventListNode(event);
        newNode.setNext(current);
        if (previous == null) {
            head = newNode;
        } else {
            previous.setNext(newNode);
        }
    }

    @Override
    public Event dequeue() {
        if (head == null) {
            throw new IllegalStateException(
                "Cannot dequeue an event from an empty queue."
            );
        }
        Event event = head.getEvent();
        head = head.getNext();
        return event;
    }

}
