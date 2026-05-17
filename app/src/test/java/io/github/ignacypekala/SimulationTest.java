package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;

import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    private Simulation simulation;
    private EventBroker eventBroker;
    private ArrayList<Event> eventHistory;
    private Vertex a;
    private Vertex b;

    @BeforeEach
    void intializeEnvironment() {
        simulation = new Simulation();
        eventHistory = new ArrayList<Event>();
        eventBroker = simulation.getEventBroker();
        a = new Vertex(0, new Coordinates(0, 0), 0);
        b = new Vertex(0, new Coordinates(0, 0), 1);
    }

    @Test
    void artificial() {
        EventBroker eventBroker = simulation.getEventBroker();
        Event a = new ArtificialEvent(0);
        eventBroker.send(a);
        Event b = new ArtificialEvent(5);
        eventBroker.send(b);
        Event c = new ArtificialEvent(2);
        eventBroker.send(c);
        simulation.run();

        assertSame(a, eventHistory.get(0));
        assertSame(c, eventHistory.get(1));
        assertSame(b, eventHistory.get(2));
    }

    private class ArtificialEvent extends RelativeEvent {
        public ArtificialEvent(int delay) {
            super(simulation, delay);
        }

        public void handle() {
            eventHistory.addLast(this);
        }
    }

    @Test
    void loop() {
        Lift lift = new Lift(a, b, 60, 3, 14 * 60, eventBroker, simulation);
        Slope slope = new Slope(b, a, 0.8, 5, 5 * 60, 1);
        Skier skier = new Skier(
            a, 5, 0, 0.5, 0.5, 0,
            new Time(14, 0, 0),
            eventBroker,
            simulation
        );
        simulation.run();
        assertEquals(3, lift.getRideCount());
        assertEquals(3, slope.getRideCount());
        assertSame(a, skier.getLocation());
    }

}
