package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventQueueTest {
    @Test
    void queueEnd() {
        EventQueue eq = new EventQueue();

        Event a = new TestEvent(new Time(15, 0, 0));
        eq.publish(a);
        Event b = new TestEvent(new Time(15, 0, 1));
        eq.publish(b);
        Event c = new TestEvent(new Time(15, 0, 2));
        eq.publish(c);

        assertSame(a, eq.poll());
        assertSame(b, eq.poll());
        assertSame(c, eq.poll());
    }

    @Test
    void queueStart() {
        EventQueue eq = new EventQueue();

        Event a = new TestEvent(new Time(15, 0, 2));
        eq.publish(a);
        Event b = new TestEvent(new Time(15, 0, 1));
        eq.publish(b);
        Event c = new TestEvent(new Time(15, 0, 0));
        eq.publish(c);

        assertSame(c, eq.poll());
        assertSame(b, eq.poll());
        assertSame(a, eq.poll());
    }

    @Test
    void queueMiddle() {
        EventQueue eq = new EventQueue();

        Event a = new TestEvent(new Time(15, 0, 0));
        eq.publish(a);
        Event b = new TestEvent(new Time(15, 0, 2));
        eq.publish(b);
        Event c = new TestEvent(new Time(15, 0, 1));
        eq.publish(c);

        assertSame(a, eq.poll());
        assertSame(c, eq.poll());
        assertSame(b, eq.poll());
    }

    @Test
    void dequeueEmpty() {
        EventQueue eq = new EventQueue();
        assertThrows(IllegalStateException.class, () -> eq.poll());
    }

    @Test
    void queueArbitrary() {
        EventQueue eq = new EventQueue();

        int eventCount = 25;
        Event[] events = new Event[eventCount];
        for (int i = 0; i < eventCount; i++) {
            events[i] = new TestEvent(new Time(9, 0, i));
        }

        for (int i : new int[] {
                1, 3, 7, 9, 11, 22, 13, 15, 17, 19, 21, 23, 0,
                2, 4, 5, 6, 8, 12, 10, 24, 14, 16, 18, 20,
        }) {
            eq.publish(events[i]);
        }

        for (int i = 0; i < eventCount; i++) {
            assertSame(events[i], eq.poll());
        }
    }

    @Test
    void simultenous() {
        EventQueue eq = new EventQueue();
        Event a = new TestEvent(new Time(9, 0, 0));
        Event b = new TestEvent(new Time(9, 0, 0));
        eq.publish(a);
        eq.publish(b);
        assertSame(a, eq.poll());
        assertSame(b, eq.poll());
    }

    @Test
    void hasEvents() {
        EventQueue eq = new EventQueue();
        assertFalse(eq.hasEvents());
        Event event = new TestEvent(new Time(15, 0, 0));
        eq.publish(event);
        assertTrue(eq.hasEvents());
    }
}
