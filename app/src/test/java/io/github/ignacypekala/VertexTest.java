package io.github.ignacypekala;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.utils.Coordinates;

public class VertexTest {
    private Vertex a;
    private Vertex b;
    private Simulation simulation;
    private Coordinates coordinates;
    private int altitude;
    private Vertex vertex;

    @BeforeEach
    void intializeEnvironment() {
        a = new Vertex(0, 0, new Coordinates(0, 0));
        b = new Vertex(1, 0, new Coordinates(0, 0));
        simulation = new Simulation();
        coordinates = new Coordinates(11, 0);
        altitude = 2001;
        vertex = new Vertex(0, altitude, coordinates);
    }

    @Test
    void construct() {
        assertEquals(altitude, vertex.getAltitude());
        assertEquals(coordinates, vertex.getPosition());
        assertEquals(0, vertex.getSlopeCount());
        assertEquals(0, vertex.getLiftCount());
        assertEquals(0, vertex.getEdgeCount());
    }

    // Checks whether two edge arrays contain the same elements in spite of the
    // actual containing additional space.
    void assertTwoEdgeArraysEqualWithPadding(Edge[] expected, Edge[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertSame(
                    expected[i], actual[i],
                    String.format("The expected and actual edges don't match at position i = %d", i));
        }
    }

    @Test
    void addEdges() {
        int slopeCount = 6;
        Slope[] slopes = new Slope[slopeCount];
        for (int i = 0; i < slopeCount; i++) {
            Slope slope = new Slope(i, a, b, 1, 1, 0, 1);
            vertex.addSlope(slope);
            slopes[i] = slope;
        }
        int liftCount = 6;
        Lift[] lifts = new Lift[slopeCount];
        for (int i = 0; i < liftCount; i++) {
            Lift lift = new Lift(
                    i,
                    vertex, vertex,
                    20, 20, 20,
                    simulation.getContext());
            lift.addStartEdge();
            lifts[i] = lift;
        }
        Edge[] edges = new Edge[slopeCount + liftCount];
        System.arraycopy(slopes, 0, edges, 0, slopeCount);
        System.arraycopy(lifts, 0, edges, slopeCount, liftCount);

        assertTwoEdgeArraysEqualWithPadding(slopes, vertex.getSlopes());
        assertTwoEdgeArraysEqualWithPadding(lifts, vertex.getLifts());
        assertTwoEdgeArraysEqualWithPadding(edges, vertex.getEdges());
    }
}
