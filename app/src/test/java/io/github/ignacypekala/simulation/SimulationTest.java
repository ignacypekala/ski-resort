package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.resort.Resort;

import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    private Simulation simulation;
    private ArrayList<Event> eventHistory;
    private Vertex a;
    private Vertex b;

    @BeforeEach
    void initializeEnvironment() {
        simulation = new Simulation();
        eventHistory = new ArrayList<Event>();
        a = new Vertex(0, 0, new Coordinates(0, 0));
        b = new Vertex(1, 0, new Coordinates(0, 0));
    }

    @Test
    void artificial() {
        Broker eventBroker = simulation.getEventBroker();
        Event aEvent = new ArtificialEvent(0);
        eventBroker.publish(aEvent);
        Event bEvent = new ArtificialEvent(5);
        eventBroker.publish(bEvent);
        Event cEvent = new ArtificialEvent(2);
        eventBroker.publish(cEvent);
        simulation.run();

        assertSame(aEvent, eventHistory.get(0));
        assertSame(cEvent, eventHistory.get(1));
        assertSame(bEvent, eventHistory.get(2));
    }

    private class ArtificialEvent extends RelativeEvent {
        public ArtificialEvent(int delay) {
            super(simulation.getClock(), delay);
        }

        public void handle() {
            eventHistory.addLast(this);
        }
    }

    @Test
    void loop() {
        Vertex[] vertices = new Vertex[] { a, b };

        SimulationClock clock = new SimulationClock();
        EventQueue eventQueue = new EventQueue();
        SimulationContext context = new SimulationContext(clock, eventQueue);

        Lift lift = new Lift(
                0,
                a,
                b,
                15 * 60,
                60,
                3,
                context);
        lift.addStartEdge();
        Lift[] lifts = new Lift[] { lift };

        Slope slope = new Slope(0, b, a, 5 * 60, 0.8, 5, 1.0);
        slope.addStartEdge();
        Slope[] slopes = new Slope[] { slope };

        Resort resort = new Resort(vertices, slopes, lifts);
        simulation = new Simulation(resort, clock, eventQueue);

        SkierGroupProfile groupProfile = new SkierGroupProfile(a, 5, 0, 0.5, 0.5);
        Skier skier = new LocalSkier(0, groupProfile, new Time(14, 0, 0), context);

        simulation.run();
        simulation.printSummary();

        assertEquals(3, lift.getRideCount());
        assertEquals(3, slope.getRideCount());
        assertSame(a, skier.getLocation());
    }
}
