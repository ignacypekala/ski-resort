package io.github.ignacypekala.event;

public class EventQueue implements Broker {
    private ListNode head;

    public EventQueue() {
        head = null;
    }

    @Override
    public void publish(final Event event) {
        ListNode previous = null;
        ListNode current = head;
        while (current != null &&
                event.getTime().compareTo(current.getEvent().getTime()) >= 0) {
            previous = current;
            current = current.getNext();
        }
        final ListNode newNode = new ListNode(event);
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
                    "Cannot dequeue an event from an empty queue.");
        }
        final Event event = head.getEvent();
        head = head.getNext();
        return event;
    }

    @Override
    public boolean hasEvents() {
        return head != null;
    }
}
