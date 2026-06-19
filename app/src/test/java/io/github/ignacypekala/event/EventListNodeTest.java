package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.Time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventListNodeTest {
    @Test
    void list() {
        Event eventA = new TestEvent(new Time(15, 0, 0));
        Event eventB = new TestEvent(new Time(15, 0, 1));
        ListNode nodeA = new ListNode(eventA);
        assertSame(eventA, nodeA.getEvent());

        ListNode nodeB = new ListNode(eventB);
        assertSame(eventB, nodeB.getEvent());

        nodeA.setNext(nodeB);
        assertSame(nodeB, nodeA.getNext());

        Event eventC = new TestEvent(new Time(15, 0, 2));
        ListNode nodeC = new ListNode(eventC, nodeA);
        assertSame(eventC, nodeC.getEvent());
        assertSame(nodeA, nodeC.getNext());
    }
}
