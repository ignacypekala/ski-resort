package io.github.ignacypekala.event;

import io.github.ignacypekala.utils.Time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    void event() {
        Time time = new Time(0, 0, 0);
        Event event = new TestEvent(time);
        assertSame(time, event.getTime());
    }
}
