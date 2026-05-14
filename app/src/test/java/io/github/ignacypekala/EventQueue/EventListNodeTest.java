package io.github.ignacypekala.EventQueue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventListNodeTest {
    @Test
    void list() {
        Event eventA = new TestEvent(0);
        Event eventB = new TestEvent(1);
        EventListNode nodeA = new EventListNode(eventA);
        assertSame(eventA, nodeA.getEvent());

        EventListNode nodeB = new EventListNode(eventB);
        assertSame(eventB, nodeB.getEvent());

        nodeA.setNext(nodeB);
        assertSame(nodeB, nodeA.getNext());

        Event eventC = new TestEvent(2);
        EventListNode nodeC = new EventListNode(eventC, nodeA);
        assertSame(eventC, nodeC.getEvent());
        assertSame(nodeA, nodeC.getNext());
    }
}
