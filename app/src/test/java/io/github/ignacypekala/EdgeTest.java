package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.simulation.SimulationContext;
import io.github.ignacypekala.skier.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    private class ConcreteEdge extends Edge {

        public ConcreteEdge(int identifier, Vertex start, Vertex end, int rideTime) {
            super(identifier, start, end, rideTime);
        }

        public double calculateAppeal(Skier skier) {
            return 4.20;
        }

        public void ride(Skier skier) {
            super.rideStarted();
        }

        @Override
        public String getRideFinishMessage(Skier skier) {
            return "";
        }

        @Override
        public String getRideStartMessage(Skier skier) {
            return "";
        }

        @Override
        public void addStartEdge() {
        }

        @Override
        public String toString() {
            return "concrete edge";
        }

    }

    @Test
    void construct() {
        Coordinates pos = new Coordinates(0, 0);
        Vertex a = new Vertex(0, 0, pos);
        Vertex b = new Vertex(1, 0, pos);
        Edge edge = new ConcreteEdge(0, a, b, 1);
        assertEquals(1, edge.getRideTime());
        assertSame(a, edge.getStart());
        assertSame(b, edge.getEnd());

        assertEquals(0, edge.getRideCount());
        SkierGroupProfile groupProfile = new SkierGroupProfile(
            new Vertex(0, 0, new Coordinates(0, 0)), 0, 0.0, 0.0, 0.0);
        SimulationContext context = new Simulation().getContext();

        edge.ride(new Skier(0, groupProfile, context.clock().getStartTime(), context));
        assertEquals(1, edge.getRideCount());
    }

}
