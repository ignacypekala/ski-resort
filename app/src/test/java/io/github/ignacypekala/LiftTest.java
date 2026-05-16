package io.github.ignacypekala;

import org.junit.jupiter.api.Test;

import io.github.ignacypekala.EventQueue.EventQueueList;
import io.github.ignacypekala.utils.Coordinates;
import static org.junit.jupiter.api.Assertions.*;

public class LiftTest {
    private static Coordinates pos = new Coordinates(0, 0);
    private static Vertex a = new Vertex(0, pos, 0);
    private static Vertex b = new Vertex(0, pos, 0);
    private static EventQueueList eventQueue = new EventQueueList();
    private static Simulation simulation = new Simulation();

    @Test
    void construct() {
        Lift lift = new Lift(a, b, 1, 2, 3, eventQueue, simulation);
        assertSame(a, lift.getStart());
        assertSame(b, lift.getEnd());
        assertEquals(1, lift.getWaitTime());
        assertEquals(2, lift.getPassengerCapacity());
        assertEquals(3, lift.getRideTime());
    }

    @Test
    void appeal() {
        Vertex end = new Vertex(0, pos, 0);
        Slope goodSlope = new TestClass.TestSlope(1, 10, 1, 0);
        Slope badSlope = new TestClass.TestSlope(0, 0, 0, 0);
        end.addSlope(goodSlope);
        end.addSlope(badSlope);
        Skier skier = new TestClass.TestSkier(10, 1.0, 0.0);
        Lift lift = new Lift(a, end, 0, 0, 0, eventQueue, simulation);
        assertEquals(goodSlope.appeal(skier), lift.appeal(skier));
    }
}
