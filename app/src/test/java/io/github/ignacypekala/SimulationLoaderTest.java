package io.github.ignacypekala;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.utils.Coordinates;

public class SimulationLoaderTest {
    Simulation simulation;
    SimulationLoader loader;

    @BeforeEach
    void initializeEnvironment() {
        simulation = new Simulation();
        simulation.getVertexRegistry().initialize(10);
        simulation.getEdgeRegistry().initialize(10);
        loader = new SimulationLoader(simulation);
    }

    @Test
    void vertex() {
        Vertex vertex = loader.loadVertex(new Scanner("102 6 9"), 0);
        assertEquals(102, vertex.getAltitude());
        Coordinates position = vertex.getPosition();
        assertEquals(6, position.getX());
        assertEquals(9, position.getY());
        assertFalse(vertex instanceof VertexAccessible);
    }

    @Test
    void vertexAccessible() {
        Vertex vertex = loader.loadVertex(new Scanner("2147483647 -2147483648 0 s"), 0);
        assertEquals(2147483647, vertex.getAltitude());
        Coordinates position = vertex.getPosition();
        assertEquals(-2147483648, position.getX());
        assertEquals(0, position.getY());
        assertTrue(vertex instanceof VertexAccessible);
    }

    @Test
    void lift() {
        VertexRegistry vertices = simulation.getVertexRegistry();
        vertices.register(new Vertex(0, 0, new Coordinates(0, 0)));
        vertices.register(new Vertex(1, 0, new Coordinates(0, 0)));
        Lift lift = loader.loadLift(new Scanner("0 1 300 3 600"), 0);
    }

}
