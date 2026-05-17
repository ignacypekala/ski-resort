package io.github.ignacypekala;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Queue;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;

public class SkierTrackedTest {
    private Queue<String> reports = new ArrayDeque<String>();

    @Test
    void reports() {
        Simulation simulation = new HijackedSimulation();
        EventBroker eventBroker = simulation.getEventBroker();
        Coordinates pos = new Coordinates(0, 0);
        Vertex vertexA = new Vertex(0, pos);
        Vertex vertexB = new Vertex(0, pos);
        Lift lift = new Lift(vertexA, vertexB, 3 * 60, 4 * 60, 3, eventBroker, simulation);
        Slope slope = new Slope(vertexB, vertexA, 1 * 60, 1.0, 1, 1.0);
        Skier skier = new SkierTracked(
                vertexA,
                1,
                0.0,
                1.0,
                0.0,
                simulation.getStartTime(),
                eventBroker,
                simulation,
                simulation);

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

        // Lift start
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
