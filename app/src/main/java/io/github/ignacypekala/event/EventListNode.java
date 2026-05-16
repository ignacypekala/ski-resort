package io.github.ignacypekala.event;

public class EventListNode {
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
