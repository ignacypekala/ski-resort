package io.github.ignacypekala;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;

public class VertexTest {
    private static EventBroker eventBroker = new EventQueueList();
    private static Clock clock = new Simulation();
    private static Coordinates coordinates = new Coordinates(11, 0);
    private static int altitude = 2001;
    private static int identifier = 9;
    private Vertex vertex;

    @BeforeEach
    void intialiseEnvironment() {
        vertex = new Vertex(identifier, altitude, coordinates);
    }

    @Test
    void construct() {
        assertEquals(altitude, vertex.getAltitude());
        assertEquals(coordinates, vertex.getPosition());
        assertEquals(identifier, vertex.getIdentifier());
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
            Slope slope = new TestClass.TestSlope(1, 0, 1, 1);
            vertex.addSlope(slope);
            slopes[i] = slope;
        }
        int liftCount = 6;
        Lift[] lifts = new Lift[slopeCount];
        for (int i = 0; i < liftCount; i++) {
            Lift lift = new Lift(
                    0,
                    vertex, vertex,
                    20, 20, 20,
                    eventBroker,
                    clock);
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
