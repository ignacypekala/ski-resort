package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;

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
        VertexRegistry vertices = simulation.getVertexRegistry();
        vertices.initialize(2);
        a = new Vertex(0, 0, new Coordinates(0, 0));
        vertices.register(a);
        b = new Vertex(1, 0, new Coordinates(0, 0));
        vertices.register(b);
    }

    @Test
    void artificial() {
        Broker eventBroker = simulation.getEventBroker();
        Event a = new ArtificialEvent(0);
        eventBroker.publish(a);
        Event b = new ArtificialEvent(5);
        eventBroker.publish(b);
        Event c = new ArtificialEvent(2);
        eventBroker.publish(c);
        simulation.run();

        assertSame(a, eventHistory.get(0));
        assertSame(c, eventHistory.get(1));
        assertSame(b, eventHistory.get(2));
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
        EdgeRegistry lifts = simulation.getLiftRegistry();
        lifts.initialize(1);
        Lift lift = new Lift(
                0,
                a,
                b,
                15 * 60,
                60,
                3,
                simulation.getContext());
        lift.addStartEdge();
        lifts.register(lift);

        EdgeRegistry slopes = simulation.getSlopeRegistry();
        slopes.initialize(1);
        Slope slope = new Slope(0, b, a, 5 * 60, 0.8, 5, 1.0);
        slope.addStartEdge();
        slopes.register(slope);

        SkierGroupProfile groupProfile = new SkierGroupProfile(a, 5, 0, 0.5, 0.5);
        Skier skier = new Skier(0, groupProfile, new Time(14, 0, 0), simulation.getContext());

        simulation.run();
        simulation.printSummary();

        assertEquals(3, lift.getRideCount());
        assertEquals(3, slope.getRideCount());
        assertSame(a, skier.getLocation());
    }
}
