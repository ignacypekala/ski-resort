package io.github.ignacypekala.EventQueue;

import io.github.ignacypekala.utils.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventQueueListTest {
    @Test
    void queueEnd() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(new Time(15, 0, 0));
        eq.send(a);
        Event b = new TestEvent(new Time(15, 0, 1));
        eq.send(b);
        Event c = new TestEvent(new Time(15, 0, 2));
        eq.send(c);

        assertSame(a, eq.poll());
        assertSame(b, eq.poll());
        assertSame(c, eq.poll());
    }

    @Test
    void queueStart() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(new Time(15, 0, 2));
        eq.send(a);
        Event b = new TestEvent(new Time(15, 0, 1));
        eq.send(b);
        Event c = new TestEvent(new Time(15, 0, 0));
        eq.send(c);

        assertSame(c, eq.poll());
        assertSame(b, eq.poll());
        assertSame(a, eq.poll());
    }

    @Test
    void queueMiddle() {
        EventQueueList eq = new EventQueueList();

        Event a = new TestEvent(new Time(15, 0, 0));
        eq.send(a);
        Event b = new TestEvent(new Time(15, 0, 2));
        eq.send(b);
        Event c = new TestEvent(new Time(15, 0, 1));
        eq.send(c);

        assertSame(a, eq.poll());
        assertSame(c, eq.poll());
        assertSame(b, eq.poll());
    }

    @Test
    void dequeueEmpty() {
        EventQueueList eq = new EventQueueList();
        assertThrows(IllegalStateException.class, () -> eq.poll());
    }

    @Test
    void queueArbitrary() {
        EventQueueList eq = new EventQueueList();

        int eventCount = 25;
        Event[] events = new Event[eventCount];
        for (int i = 0; i < eventCount; i++) {
            events[i] = new TestEvent(new Time(15, 0, i));
        }

        for (int i : new int[] {
                1, 3, 7, 9, 11, 22, 13, 15, 17, 19, 21, 23, 0,
                2, 4, 5, 6, 8, 12, 10, 24, 14, 16, 18, 20,
        }) {
            eq.send(events[i]);
        }

        for (int i = 0; i < eventCount; i++) {
            assertSame(events[i], eq.poll());
        }
    }
}
