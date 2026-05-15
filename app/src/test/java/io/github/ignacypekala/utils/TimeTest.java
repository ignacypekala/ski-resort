package io.github.ignacypekala.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimeTest {
    @Test 
    void normalized() {
        assertEquals("19:00:01", new Time(19, 0, 1).toString());
        assertEquals("00:00:00", new Time(0, 0, 0).toString());
        assertEquals("21:37:42", new Time(21, 37, 42).toString());
        assertEquals("23:59:59", new Time(23, 59, 59).toString());
    }

    @Test
    void unnormalized() {
        assertEquals("16:01:00", new Time(16, 0, 60).toString());
        assertEquals("09:00:00", new Time(8, 58, 120).toString());
        assertEquals("00:00:00", new Time(23, 59, 60).toString());
    }

    @Test
    void ivnalid() {
        assertThrows(IllegalArgumentException.class, () -> new Time(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Time(0, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Time(0, 0, -1));
    }
}
