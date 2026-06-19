package io.github.ignacypekala.event;

public class ListNode {
    Event event;
    ListNode next;

    public ListNode(final Event event) {
        this.event = event;
    }

    public ListNode(final Event event, final ListNode next) {
        this.event = event;
        this.next = next;
    }

    public Event getEvent() {
        return event;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(final ListNode node) {
        next = node;
    }
}
