package io.github.ignacypekala.EventQueue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventListNodeTest {
    @Test
    void list() {
        Event eventA = new TestEvent(0);
        Event eventB = new TestEvent(1);
        EventListNode nodeA = new EventListNode(eventA);
        assertSame(nodeA.getEvent(), eventA);

        EventListNode nodeB = new EventListNode(eventB);
        assertSame(nodeB.getEvent(), eventB);

        nodeA.setNext(nodeB);
        assertSame(nodeA.getNext(), nodeB);

        Event eventC = new TestEvent(2);
        EventListNode nodeC = new EventListNode(eventC, nodeA);
        assertSame(nodeC.getEvent(), eventC);
        assertSame(nodeC.getNext(), nodeA);
    }
}
