package io.github.ignacypekala;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import io.github.ignacypekala.event.EventQueue;
import io.github.ignacypekala.event.Broker;
import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.utils.Coordinates;

public class VertexTest {
    private static Broker eventBroker = new EventQueue();
    private static Simulation simulation = new Simulation();
    private static Clock clock = simulation.getClock();
    private static Coordinates coordinates = new Coordinates(11, 0);
    private static int altitude = 2001;
    private Vertex vertex;

    @BeforeEach
    void intialiseEnvironment() {
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
                    String.format(
                            "The expected and actual edges don't match at position i = %d",
                            i));
        }
    }

    @Test
    void addEdges() {
        int slopeCount = 6;
        Slope[] slopes = new Slope[slopeCount];
        for (int i = 0; i < slopeCount; i++) {
            Slope slope = new TestClass.TestSlope(i, 1, 0, 1, 1);
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
                    eventBroker,
                    clock);
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
