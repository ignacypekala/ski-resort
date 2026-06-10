package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.event.EventQueue;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.Simulation;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LiftTest {
    private static Coordinates pos;
    private static Vertex a;
    private static Vertex b;
    private static Broker eventBroker;
    private static Clock clock;

    @BeforeEach
    void initiateEnvironment() {
        pos = new Coordinates(0, 0);
        a = new Vertex(0, 0, pos);
        b = new Vertex(1, 0, pos);
        eventBroker = new EventQueue();
        Simulation simulation = new Simulation();
        clock = simulation.getClock();
    }

    @Test
    void construct() {
        Lift lift = new Lift(0, a, b, 3, 1, 2, eventBroker, clock);
        lift.addStartEdge();
        assertSame(a, lift.getStart());
        assertSame(b, lift.getEnd());
        assertEquals(1, lift.getDepartureInterval());
        assertEquals(2, lift.getPassengerCapacity());
        assertEquals(3, lift.getRideTime());
        assertTrue(
                Arrays.asList(a.getLifts()).contains(lift),
                "Lift hasn't been added to a's lift array.");
    }

    @Test
    void appeal() {
        Vertex end = new Vertex(2, 0, pos);
        Slope goodSlope = new TestClass.TestSlope(0, 1, 10, 1, 0);
        Slope badSlope = new TestClass.TestSlope(1, 0, 0, 0, 0);
        end.addSlope(goodSlope);
        end.addSlope(badSlope);
        Skier skier = new TestClass.TestSkier(0, 10, 1.0, 0.0);
        Lift lift = new Lift(0, a, end, 0, 0, 0, eventBroker, clock);
        assertEquals(goodSlope.calculateAppeal(skier), lift.calculateAppeal(skier));
    }

    @Test
    void dryRun() {
        assertFalse(eventBroker.hasEvents());
        Lift lift = new Lift(0, a, b, 1 * 60, 2 * 60, 3, eventBroker, clock);
        assertTrue(eventBroker.hasEvents());

        // Check if the lift has scheduled its startup
        Event event = eventBroker.poll();
        assertEquals(
            new LiftStart(lift, clock),
            event
        );

        assertFalse(eventBroker.hasEvents());
        event.handle();
        assertTrue(eventBroker.hasEvents());

        // Check if the first departed carrier has arrived
        event = eventBroker.poll();
        assertTrue(eventBroker.hasEvents());
        assertEquals(
            lift.new Arrival(new Carrier(new Skier[3], 0, lift)),
            event
        );

        // Check if the next lift depart was scheduled
        event = eventBroker.poll();
        assertEquals(lift.new Departure(), event);

        assertFalse(eventBroker.hasEvents());
    }

    @Test
    void fullLoad() {
        int liftCapacity = 3;
        // Longer departureInterval than rideTime so that the first carrier arrives
        // before the 2nd depart.
        Lift lift = new Lift(0, a, b, 1, 2, liftCapacity, eventBroker, clock);

        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            Skier skier = new TestClass.TestSkier(i, i, 0.5, 0.5);
            lift.ride(skier);
            skiers[i] = skier;
        }

        // TODO: Remove instanceof and cast
        // Send the lift
        Event event = eventBroker.poll();
        assertTrue(event instanceof LiftStart);
        LiftStart departEvent = (LiftStart) event;
        departEvent.handle();

        // Check if the correct passengers got a ride
        event = eventBroker.poll();
        assertTrue(event instanceof Lift.Arrival);
        Lift.Arrival arrival = (Lift.Arrival) event;
        Skier[] passengers = Arrays.copyOfRange(skiers, 0, liftCapacity);
        assertArrayEquals(
                passengers,
                arrival.getCarrier().getPassengers());
    }

    @Test
    void partialLoad() {
        int liftCapacity = 3;
        Lift lift = new Lift(0, a, b, 1, 2, liftCapacity, eventBroker, clock);

        Skier skier = new TestClass.TestSkier(0, 0, 0.5, 0.5);
        lift.ride(skier);

        // Send the lift
        Event event = eventBroker.poll();
        assertTrue(event instanceof LiftStart);
        LiftStart liftStart = (LiftStart) event;
        liftStart.handle();

        // Check if the correct passengers got a ride
        event = eventBroker.poll();
        assertTrue(event instanceof Lift.Arrival);
        Lift.Arrival arrival = (Lift.Arrival) event;
        Skier[] passengers = new Skier[liftCapacity];
        passengers[0] = skier;
        assertArrayEquals(
                passengers,
                arrival.getCarrier().getPassengers());
    }

    private boolean startHook = false;
    private boolean finishHook = false;

    private void startHookConsumer(Edge edge) {
        startHook = true;
    }

    private void finishHookConsumer(Edge edge) {
        finishHook = true;
    }

    // Check whether the passengers get correct feedback when using lifts.
    @Test
    void aftermath() {
        Skier skier = new TestClass.SnitchSkier(
                0,
                0, 0.5, 0.5,
                this::startHookConsumer,
                this::finishHookConsumer);

        Lift lift = new Lift(0, skier.getLocation(), b, 1, 2, 1, eventBroker, clock);
        // Create a loop so that the end vertex has an outgoing edge.
        b.addLift(lift);

        lift.ride(skier);

        // Send the lift
        Event event = eventBroker.poll();
        assertTrue(event instanceof LiftStart);
        LiftStart departEvent = (LiftStart) event;

        assertFalse(startHook, "The skier has started their ride prematurely.");
        departEvent.handle();
        assertTrue(startHook, "The skier hasn't started their ride.");

        // Check if the correct passengers got a ride
        event = eventBroker.poll();
        assertTrue(event instanceof Lift.Arrival);
        Lift.Arrival arrival = (Lift.Arrival) event;

        assertFalse(finishHook, "The skier has finished their ride prematurely.");
        arrival.handle();
        assertTrue(finishHook, "The skier hasn't finished their ride.");

        assertSame(b, skier.getLocation());
    }
}
