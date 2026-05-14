package io.github.ignacypekala.EventQueue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    void list() {
        Event event = new TestEvent(0);
        assertSame(0, event.getTime());
    }
}
