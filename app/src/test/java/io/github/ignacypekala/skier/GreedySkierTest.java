package io.github.ignacypekala.skier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import io.github.ignacypekala.*;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public class GreedySkierTest {
    private Vertex startVertex;
    private Vertex endVertex;
    private Time startTime;
    private SimulationContext context;

    @BeforeEach
    void setUp() {
        startVertex = new Vertex(1, 100, new Coordinates(0, 0));
        endVertex = new Vertex(2, 50, new Coordinates(10, 10));
        startTime = new Time(9, 0, 0);
        context = new Simulation().getContext();
    }

    @Test
    void testCompareSlopesByAppeal() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.0, 1.0, 0.0);
        GreedySkier skier = new GreedySkier(1, profile, startTime, context);

        Slope slopeA = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 0.0);
        Slope slopeB = new Slope(102, startVertex, endVertex, 60, 1.0, 10, 0.0);
        Slope slopeC = new Slope(103, startVertex, endVertex, 60, 1.0, 5, 0.0);

        StrategicSkier.RouteOption optionA = new StrategicSkier.RouteOption(slopeA, 0);
        StrategicSkier.RouteOption optionB = new StrategicSkier.RouteOption(slopeB, 0);
        StrategicSkier.RouteOption optionC = new StrategicSkier.RouteOption(slopeC, 0);

        assertTrue(skier.compareRouteOptions(optionA, optionB) > 0);
        assertTrue(skier.compareRouteOptions(optionB, optionA) < 0);
        assertEquals(0, skier.compareRouteOptions(optionA, optionC));
    }
}
