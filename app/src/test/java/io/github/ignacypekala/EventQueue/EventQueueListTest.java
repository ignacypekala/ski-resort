package io.github.ignacypekala.EventQueue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class EventQueueListTest {
    @Test
    void queueEnd() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(0);
        eq.enqueue(a);
        Event b = new TestEvent(1);
        eq.enqueue(b);
        Event c = new TestEvent(2);
        eq.enqueue(c);

        assertSame(eq.dequeue(), a);
        assertSame(eq.dequeue(), b);
        assertSame(eq.dequeue(), c);
    }

    @Test
    void queueStart() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(2);
        eq.enqueue(a);
        Event b = new TestEvent(1);
        eq.enqueue(b);
        Event c = new TestEvent(0);
        eq.enqueue(c);

        assertSame(eq.dequeue(), c);
        assertSame(eq.dequeue(), b);
        assertSame(eq.dequeue(), a);
    }

    @Test
    void queueMiddle() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(0);
        eq.enqueue(a);
        Event b = new TestEvent(2);
        eq.enqueue(b);
        Event c = new TestEvent(1);
        eq.enqueue(c);

        assertSame(eq.dequeue(), a);
        assertSame(eq.dequeue(), c);
        assertSame(eq.dequeue(), b);
    }

    @Test
    void dequeueEmpty() {
        EventQueueList eq = new EventQueueList();
        assertThrows(IllegalStateException.class, () -> eq.dequeue());
    }

    @Test
    void queueArbitrary() {
        EventQueueList eq = new EventQueueList();

        int eventCount = 25;
        Event[] events = new Event[eventCount];
        for (int i = 0; i < eventCount; i++) {
            events[i] = new TestEvent(i);
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
}
