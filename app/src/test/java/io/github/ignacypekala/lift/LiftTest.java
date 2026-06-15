package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LiftTest {
    private static Coordinates pos;
    private static Vertex a;
    private static Vertex b;
    private static SimulationContext simulationContext;
    private static Time startTime;
    private static Broker eventBroker;
    private static Clock clock;
    private static SkierGroupProfile groupProfile;

    @BeforeEach
    void initializeEnvironment() {
        pos = new Coordinates(0, 0);
        a = new Vertex(0, 0, pos);
        b = new Vertex(1, 0, pos);
        Simulation simulation = new Simulation();
        simulationContext = simulation.getContext();
        clock = simulationContext.clock();
        eventBroker = simulation.getEventBroker();
        startTime = simulationContext.clock().getStartTime();
        groupProfile = new SkierGroupProfile(a, 10, 0, 1.0, 0.0);
    }

    @Test
    void construct() {
        Lift lift = new Lift(0, a, b, 3, 1, 2, simulationContext);
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
        Skier skier = new Skier(
                0,
                groupProfile,
                simulationContext.clock().getStartTime(),
                simulationContext);
        Lift lift = new Lift(0, a, end, 0, 0, 0, simulationContext);
        assertEquals(goodSlope.calculateAppeal(skier), lift.calculateAppeal(skier));
    }

    @Test
    void dryRun() {
        assertFalse(eventBroker.hasEvents());
        Lift lift = new Lift(0, a, b, 1 * 60, 2 * 60, 3, simulationContext);
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
            new LiftArrival(new Carrier(new Skier[3], 0, lift), clock),
            event
        );

        // Check if the next lift depart was scheduled
        event = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), event);

        assertFalse(eventBroker.hasEvents());
    }

    @Test
    void fullLoad() {
        int liftCapacity = 3;
        // Longer departureInterval than rideTime so that the first carrier arrives
        // before the 2nd depart.
        Lift lift = new Lift(0, a, b, 1, 2, liftCapacity, simulationContext);
        a.addLift(lift);

        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            Skier skier = new Skier(
                    i,
                    groupProfile,
                    simulationContext.clock().getStartTime(),
                    simulationContext);
            skiers[i] = skier;
        }

        Event event = eventBroker.poll();
        assertEquals(new LiftStart(lift, clock), event);
        event.handle();

        for (int i = 0; i < 5; i++) {
            event = eventBroker.poll();
            assertEquals(new DayStart(skiers[i]), event);
            event.handle();
        }

        // The initial empty carrier
        event = eventBroker.poll();
        assertEquals(
                new LiftArrival(new Carrier(new Skier[liftCapacity], 0, lift), clock),
                event);
        event.handle();

        // First non-empty ride
        event = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), event);
        event.handle();

        event = eventBroker.poll();
        Skier[] passengers = Arrays.copyOfRange(skiers, 0, liftCapacity);
        assertEquals(new LiftArrival(new Carrier(passengers, 3, lift), clock), event);

        // Second ride - 2/3 passengers
        event = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), event);
        event.handle();

        event = eventBroker.poll();
        passengers = new Skier[3];
        System.arraycopy(skiers, 3, passengers, 0, 2);
        assertEquals(new LiftArrival(new Carrier(passengers, 2, lift), clock), event);
    }

    @Test
    void partialLoad() {
        int liftCapacity = 3;
        Lift lift = new Lift(0, a, b, 1, 2, liftCapacity, simulationContext);
        a.addLift(lift);
        b.addLift(lift);
        Skier skier = new Skier(0, groupProfile, startTime, simulationContext);

        Event event = eventBroker.poll();
        assertEquals(new LiftStart(lift, clock), event);
        event.handle();

        event = eventBroker.poll();
        assertEquals(new DayStart(skier), event);
        event.handle();

        event = eventBroker.poll();
        assertEquals(
                new LiftArrival(new Carrier(new Skier[3], 0, lift), clock),
                event);
        event.handle();

        event = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), event);
        event.handle();

        event = eventBroker.poll();
        event.handle();

        // Check if correct passengers got a lift
        Skier[] passengers = new Skier[liftCapacity];
        passengers[0] = skier;
        assertEquals(new LiftArrival(
            new Carrier(passengers, 1, lift),
            clock
        ), event);

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
                groupProfile,
                this::startHookConsumer,
                this::finishHookConsumer);

        Lift lift = new Lift(0, skier.getLocation(), b, 1, 2, 1, simulationContext);
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
        assertTrue(event instanceof LiftArrival);
        LiftArrival arrival = (LiftArrival) event;

        assertFalse(finishHook, "The skier has finished their ride prematurely.");
        arrival.handle();
        assertTrue(finishHook, "The skier hasn't finished their ride.");

        assertSame(b, skier.getLocation());
    }
}
