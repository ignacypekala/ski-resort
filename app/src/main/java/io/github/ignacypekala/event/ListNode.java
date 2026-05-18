package io.github.ignacypekala.event;

public class ListNode {
    Event event;
    ListNode next;

    public ListNode(Event event) {
        this.event = event;
    }

    public ListNode(Event event, ListNode next) {
        this.event = event;
        this.next = next;
    }

    public Event getEvent() {
        return event;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode node) {
        next = node;
    }
}
