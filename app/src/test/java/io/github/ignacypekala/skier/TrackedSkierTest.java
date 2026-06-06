package io.github.ignacypekala.simulation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Queue;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;

public class TrackedSkierTest {
    private Queue<String> reports = new ArrayDeque<String>();

    @Test
    void reports() {
        Simulation simulation = new HijackedSimulation();
        Broker eventBroker = simulation.getEventBroker();
        Coordinates pos = new Coordinates(0, 0);
        Vertex vertexA = new Vertex(0, 0, pos);
        Vertex vertexB = new Vertex(1, 0, pos);
        Lift lift = new Lift(
            0,
            vertexA,
            vertexB,
            3 * 60,
            4 * 60,
            3,
            eventBroker,
            simulation.getClock()
        );
        lift.addStartEdge();
        Slope slope = new Slope(0, vertexB, vertexA, 1 * 60, 1.0, 1, 1.0);
        slope.addStartEdge();
        Skier skier = new TrackedSkier(
                0,
                vertexA,
                1,
                0.0,
                1.0,
                0.0,
                simulation.getClock().getStartTime(),
                eventBroker,
                simulation.getClock(),
                simulation);

        // Lift start
        simulation.tick();

        simulation.tick();
        assertEquals(
            String.format(
                "skier %d has joined the queue for lift %d.",
                skier.getIdentifier(),
                lift.getIdentifier()
            ),
            reports.remove()
        );
        assertTrue(eventBroker.hasEvents());

        // Empty carriage arrival
        simulation.tick();

        simulation.tick();
        assertEquals(
            String.format(
                "skier %d has boarded lift %d.",
                skier.getIdentifier(),
                lift.getIdentifier()
            ),
            reports.remove()
        );

        // Lift depart
        simulation.tick();

        // Carrier arrival
        simulation.tick();

        assertEquals(
            String.format(
                "skier %d has gotten off lift %d.",
                skier.getIdentifier(),
                lift.getIdentifier()
            ),
            reports.remove()
        );
        assertEquals(
            String.format(
                "skier %d has started their run on slope %d.",
                skier.getIdentifier(),
                slope.getIdentifier()
            ),
            reports.remove()
        );

        simulation.tick();
        simulation.tick();
        assertEquals(
            String.format(
                "skier %d has finished their run on slope %d.",
                skier.getIdentifier(),
                slope.getIdentifier()
            ),
            reports.remove()
        );

    }

    private class HijackedSimulation extends Simulation {
        @Override
        public void report(String message) {
            reports.add(message);
            super.report(message);
        }
    }
}
