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
import java.util.function.Consumer;

public class LiftTest {
    private static Coordinates pos;
    private static Vertex a;
    private static Vertex b;
    private static SimulationContext simulationContext;
    private static Broker eventBroker;
    private static Clock clock;
    private static SkierGroupProfile groupProfile;

    private static final int LIFT_CAPACITY = 3;
    private static final int LIFT_RIDE_TIME = 1;
    private static final int LIFT_DEPARTURE_INTERVAL = 2;
    private static Lift lift;

    @BeforeEach
    void initializeEnvironment() {
        pos = new Coordinates(0, 0);
        a = new Vertex(0, 0, pos);
        b = new Vertex(1, 0, pos);
        Simulation simulation = new Simulation();
        simulationContext = simulation.getContext();
        clock = simulationContext.clock();
        eventBroker = simulation.getEventBroker();

        groupProfile = new SkierGroupProfile(a, 10, 0, 1.0, 0.0);

        lift = new Lift(
                0,
                a,
                b,
                LIFT_RIDE_TIME,
                LIFT_DEPARTURE_INTERVAL,
                LIFT_CAPACITY,
                simulationContext);
    }

    @Test
    void construct() {
        lift.addStartEdge();
        assertSame(a, lift.getStart());
        assertSame(b, lift.getEnd());
        assertEquals(LIFT_DEPARTURE_INTERVAL, lift.getDepartureInterval());
        assertEquals(LIFT_CAPACITY, lift.getPassengerCapacity());
        assertEquals(LIFT_RIDE_TIME, lift.getRideTime());
        assertTrue(
                Arrays.asList(a.getLifts()).contains(lift),
                "Lift hasn't been added to a's lift array.");
    }

    @Test
    void appeal() {
        Slope goodSlope = new Slope(0, b, a, 0, 1, 10, 1);
        Slope badSlope = new Slope(1, b, a, 0, 0, 0, 0);
        b.addSlope(goodSlope);
        b.addSlope(badSlope);
        Skier skier = createSkiers(1)[0];
        assertEquals(goodSlope.calculateAppeal(skier), lift.calculateAppeal(skier));
    }

    @Test
    void dryRun() {
        checkLiftStarts(lift);
        checkCarrierArrives(lift, new Skier[3], 0);
        checkLiftDeparts(lift);
    }

    @Test
    void fullLoad() {
        a.addLift(lift);
        int skierCount = 4;
        Skier[] skiers = createSkiers(skierCount);

        checkLiftStarts(lift);

        checkSkierArrives(skiers[0]);
        checkSkierArrives(skiers[1]);
        checkSkierArrives(skiers[2]);
        checkSkierArrives(skiers[3]);

        checkCarrierArrives(lift, new Skier[LIFT_CAPACITY], 0);

        checkLiftDeparts(lift);
        checkCarrierArrives(lift, new Skier[]{skiers[0], skiers[1], skiers[2]}, 3);
        assertEquals(3, lift.getRideCount());

        checkLiftDeparts(lift);
        checkCarrierArrives(lift, new Skier[]{skiers[3], null, null}, 1);
        assertEquals(4, lift.getRideCount());
    }

    @Test
    void partialLoad() {
        a.addLift(lift);
        b.addLift(lift);

        checkLiftStarts(lift);

        Skier[] skiers = createSkiers(2);
        checkSkierArrives(skiers[0]);
        checkSkierArrives(skiers[1]);

        checkCarrierArrives(lift, new Skier[3], 0);

        checkLiftDeparts(lift);
        checkCarrierArrives(lift, new Skier[]{skiers[0], skiers[1], null}, 2);

        assertEquals(2, lift.getRideCount());
    }

    @Test
    void aftermath() {
        Skier skier = new SnitchSkier(
                0,
                groupProfile,
                this::startHookConsumer,
                this::finishHookConsumer);

        checkLiftStarts(lift);

        // Create a loop so that the end vertex has an outgoing edge.
        a.addLift(lift);
        b.addLift(lift);

        checkSkierArrives(skier);

        checkCarrierArrives(lift, new Skier[3], 0);

        Event departureEvent = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), departureEvent);

        assertFalse(startHookFlag, "The skier has started their ride prematurely.");
        departureEvent.handle();
        assertTrue(startHookFlag, "The skier hasn't started their ride.");

        // Check if the correct passengers got a ride
        Event arrivalEvent = eventBroker.poll();
        assertEquals(
            new LiftArrival(new Carrier(new Skier[]{skier, null, null}, 1, lift), clock),
            arrivalEvent
        );
        LiftArrival arrival = (LiftArrival) arrivalEvent;

        assertFalse(finishHookFlag, "The skier has finished their ride prematurely.");
        arrival.handle();
        assertTrue(finishHookFlag, "The skier hasn't finished their ride.");

        assertSame(b, skier.getLocation());
    }

    private boolean startHookFlag = false;
    private boolean finishHookFlag = false;

    private void startHookConsumer(Edge edge) {
        startHookFlag = true;
    }

    private void finishHookConsumer(Edge edge) {
        finishHookFlag = true;
    }

    private static class SnitchSkier extends LocalSkier {
        public SnitchSkier(
                int identifier,
                SkierGroupProfile groupProfile,
                Consumer<Edge> rideStartedCallback,
                Consumer<Edge> rideFinishedCallback) {
            super(
                identifier,
                groupProfile,
                clock.getStartTime(),
                simulationContext,
                new SkierListener() {
                    @Override
                    public void onRideStarted(Skier skier, Edge edge) {
                        rideStartedCallback.accept(edge);
                    }

                    @Override
                    public void onRideFinished(Skier skier, Edge edge) {
                        rideFinishedCallback.accept(edge);
                    }

                    @Override
                    public void onLiftQueueJoined(Skier skier, Lift lift) {
                    }
                }
            );
        }
    }

    private Skier[] createSkiers(int n) {
        Skier[] skiers = new Skier[n];
        for (int i = 0; i < n; i++) {
            skiers[i] = new LocalSkier(i, groupProfile, clock.getStartTime(), simulationContext);
        }
        return skiers;
    }

    private void checkSkierArrives(Skier skier) {
        Event event = eventBroker.poll();
        assertEquals(new DayStart(skier), event);
        event.handle();
    }

    private void checkLiftStarts(Lift lift) {
        Event event = eventBroker.poll();
        assertEquals(
            new LiftStart(lift, clock),
            event
        );
        event.handle();
    }

    // Doesn't handle the event as it's currently outside of scope of the tests and
    // it would pollute the event queue.
    private void checkCarrierArrives(Lift lift, Skier[] passengers, int passengerCount) {
        Event event = eventBroker.poll();
        assertTrue(eventBroker.hasEvents());
        assertEquals(
            new LiftArrival(new Carrier(passengers, passengerCount, lift), clock),
            event
        );
    }

    private void checkLiftDeparts(Lift lift) {
        Event event = eventBroker.poll();
        assertEquals(new LiftDeparture(clock, lift), event);
        event.handle();
    }
}
