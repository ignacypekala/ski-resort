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
        Vertex vertexA = new Vertex(0, 0, pos);
        Vertex vertexB = new Vertex(1, 0, pos);
        Lift lift = new Lift(0, vertexA, vertexB, 3 * 60, 4 * 60, 3, eventBroker, simulation);
        new Slope(0, vertexB, vertexA, 1 * 60, 1.0, 1, 1.0);
        Skier skier = new SkierTracked(
            0,
            vertexA,
            1,
            0.0,
            1.0,
            0.0,
            simulation.getStartTime(),
            eventBroker,
            simulation,
            simulation
        );

        simulation.tick();
        assertEquals(
            "skier 0 has joined the queue for lift 0.",
            reports.remove()
        );
        assertTrue(eventBroker.hasEvents());

        // Lift start
        simulation.tick();
        assertEquals("skier 0 has boarded lift 0.", reports.remove());

        // Lift depart
        simulation.tick();

        // Charline arrival
        simulation.tick();

        assertEquals("skier 0 has gotten off lift 0.", reports.remove());
        assertEquals("skier 0 has started their run on slope 0.", reports.remove());


        simulation.tick();
        simulation.tick();
        assertEquals("skier 0 has finished their run on slope 0.", reports.remove());


    }
    private class HijackedSimulation extends Simulation {
        @Override
        public void report(String message) {
            reports.add(message);
            super.report(message);
        }
    }
}
