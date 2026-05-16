package io.github.ignacypekala;

import org.junit.jupiter.api.*;

import io.github.ignacypekala.utils.Coordinates;
import io.github.ignacypekala.EventQueue.*;
import io.github.ignacypekala.Lift.ChairLineArrival;
import io.github.ignacypekala.Lift.LiftDepart;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LiftTest {
    private static Coordinates pos;
    private static Vertex a;
    private static Vertex b;
    private static EventQueueList eventQueue;
    private static Simulation simulation;

    @BeforeEach
    void initiateEnvironment() {
        pos = new Coordinates(0, 0);
        a = new Vertex(0, pos, 0);
        b = new Vertex(0, pos, 0);
        eventQueue = new EventQueueList();
        simulation = new Simulation();
    }

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

    @Test
    void dryRun() {
        assertFalse(eventQueue.hasEvents());
        Lift lift = new Lift(a, b, 2, 3, 10, eventQueue, simulation);
        assertTrue(eventQueue.hasEvents());

        // Check if the lift has scheduled its first depart
        Event event = eventQueue.poll();
        assertTrue(event instanceof LiftDepart);
        LiftDepart departEvent = (LiftDepart) event;
        assertSame(lift, departEvent.getLift());

        assertFalse(eventQueue.hasEvents());
        departEvent.handle();
        assertTrue(eventQueue.hasEvents());

        // Check if the next lift depart was scheduled
        event = eventQueue.poll();
        assertTrue(eventQueue.hasEvents());
        assertTrue(event instanceof LiftDepart);
        departEvent = (LiftDepart) event;
        assertSame(lift, departEvent.getLift());

        // Check if the departed chair line had its arrival scheduled
        event = eventQueue.poll();
        assertFalse(eventQueue.hasEvents());
        assertTrue(event instanceof ChairLineArrival);
        ChairLineArrival chairLineArrival = (ChairLineArrival) event;
        assertSame(lift, chairLineArrival.getChairLine().getLift());

        assertFalse(eventQueue.hasEvents());
    }

    @Test
    void fullLoad() {
        int liftCapacity = 3;
        // Longer waitTime than rideTime so that the first ChairLine arrives
        // before the 2nd depart.
        Lift lift = new Lift(a, b, 2, liftCapacity, 1, eventQueue, simulation);

        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            Skier skier = new TestClass.TestSkier(i, 0.5, 0.5);
            lift.ride(skier);
            skiers[i] = skier;
        }

        // Send the lift
        Event event = eventQueue.poll();
        assertTrue(event instanceof LiftDepart);
        LiftDepart departEvent = (LiftDepart) event;
        departEvent.handle();

        // Check if the correct passengers got a ride
        event = eventQueue.poll();
        assertTrue(event instanceof ChairLineArrival);
        ChairLineArrival arrival = (ChairLineArrival) event;
        Skier[] passengers = Arrays.copyOfRange(skiers, 0, liftCapacity);
        assertArrayEquals(
            passengers,
            arrival.getChairLine().getPassengers()
        );
    }

    @Test
    void partialLoad() {
        int liftCapacity = 3;
        // Longer waitTime than rideTime so that the first ChairLine arrives
        // before the 2nd depart.
        Lift lift = new Lift(a, b, 2, liftCapacity, 1, eventQueue, simulation);

        Skier skier = new TestClass.TestSkier(0, 0.5, 0.5);
        lift.ride(skier);

        // Send the lift
        Event event = eventQueue.poll();
        assertTrue(event instanceof LiftDepart);
        LiftDepart departEvent = (LiftDepart) event;
        departEvent.handle();

        // Check if the correct passengers got a ride
        event = eventQueue.poll();
        assertTrue(event instanceof ChairLineArrival);
        ChairLineArrival arrival = (ChairLineArrival) event;
        Skier[] passengers = new Skier[liftCapacity];
        passengers[0] = skier;
        assertArrayEquals(
            passengers,
            arrival.getChairLine().getPassengers()
        );
        
    }
}
