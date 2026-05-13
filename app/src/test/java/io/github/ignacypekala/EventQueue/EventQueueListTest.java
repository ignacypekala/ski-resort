package io.github.ignacypekala.EventQueue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventQueueListTest {
    public class ConcreteEvent extends Event {
        public ConcreteEvent(int time) {
            super(time);
        }

        public void handle() {
        }

        public String toString() {
            return Integer.toString(super.getTime());
        }
    }

    @Test
    void queue() {
        EventQueueList eq = new EventQueueList();
        int eventCount = 25;
        Event[] events = new Event[eventCount];
        for (int i = 0; i < eventCount; i++) {
            events[i] = new ConcreteEvent(i);
        }
        for (int i : new int[] {
                1, 3, 7, 9, 11, 22, 13, 15, 17, 19, 21, 23, 0,
                2, 4, 5, 6, 8, 12, 10, 24, 14, 16, 18, 20,
        }) {
            eq.enqueue(events[i]);
        }
        for (int i = 0; i < eventCount; i++) {
            assertSame(eq.dequeue(), events[i]);
        }
    }

    @Test
    void list() {
        Event eventA = new ConcreteEvent(0);
        Event eventB = new ConcreteEvent(1);
        EventListNode nodeA = new EventListNode(eventA);
        assertSame(nodeA.getEvent(), eventA);

        EventListNode nodeB = new EventListNode(eventB);
        assertSame(nodeB.getEvent(), eventB);

        nodeA.setNext(nodeB);
        assertSame(nodeA.getNext(), nodeB);

        Event eventC = new ConcreteEvent(2);
        EventListNode nodeC = new EventListNode(eventC, nodeA);
        assertSame(nodeC.getEvent(), eventC);
        assertSame(nodeC.getNext(), nodeA);
    }
}
