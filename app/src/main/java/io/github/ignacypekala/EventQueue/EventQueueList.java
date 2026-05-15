package io.github.ignacypekala.EventQueue;

public class EventQueueList implements EventQueue {
    EventListNode head;

    public EventQueueList() {
        head = null;
    }

    @Override
    public void enqueue(Event event) {
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

    @Override
    public boolean isEmpty() {
        return head == null;
    }
}
