package io.github.ignacypekala;

import org.junit.jupiter.api.Test;
import io.github.ignacypekala.utils.Coordinates;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    private class ConcreteEdge extends Edge {
        public ConcreteEdge(Vertex start, Vertex end, int rideTime) {
            super(start, end, rideTime);
        }
        public double appeal(Skier skier) {
            return 4.20;
        }
        public void ride(Skier skier) {
            super.rideFinished();
        }

        @Override
        public void addStartEdge() {}

    }

    @Test
    void construct() {
        Coordinates pos = new Coordinates(0, 0);
        Vertex a = new Vertex(0, pos, 0);
        Vertex b = new Vertex(0, pos, 0);
        Edge edge = new ConcreteEdge(a, b, 1);
        assertEquals(1, edge.getRideTime());
        assertSame(a, edge.getStart());
        assertSame(b, edge.getEnd());

        assertEquals(0, edge.getRideCount());
        edge.ride(new TestClass.TestSkier(0, 0.0, 0.0));
        assertEquals(1, edge.getRideCount());
    }

}
