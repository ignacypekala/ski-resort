package io.github.ignacypekala.EventQueue;

import com.google.common.annotations.VisibleForTesting;

public class EventQueueList implements EventBroker {
    EventListNode head = null;

    public EventQueueList() {}

    @Override
    public void send(Event event) {
        EventListNode previous = null;
        EventListNode current = head;
        while (
            current != null && 
            event.getTime().compareTo(current.getEvent().getTime()) > 0
        ) {
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
    public Event poll() {
        if (head == null) {
            throw new IllegalStateException(
                "Cannot dequeue an event from an empty queue."
            );
        }
        Event event = head.getEvent();
        head = head.getNext();
        return event;
    }

    @Override
    public boolean hasEvents() {
        return head != null;
    }
}
