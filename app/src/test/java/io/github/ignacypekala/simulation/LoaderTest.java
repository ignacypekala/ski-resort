package io.github.ignacypekala.simulation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.utils.Coordinates;
import io.github.ignacypekala.utils.Time;

public class LoaderTest {
    Simulation simulation;
    Loader loader;

    @BeforeEach
    void initializeEnvironment() {
        simulation = new Simulation();
        loader = new Loader(simulation);
    }

    @Test
    void vertex() {
        Vertex vertex = loader.loadVertex("102 6 9", 0);
        assertEquals(102, vertex.getAltitude());
        Coordinates position = vertex.getPosition();
        assertEquals(6, position.getX());
        assertEquals(9, position.getY());
        assertFalse(vertex instanceof VertexAccessible);
    }

    @Test
    void vertexAccessible() {
        Vertex vertex = loader.loadVertex("2147483647 -2147483648 0 s", 0);
        assertEquals(2147483647, vertex.getAltitude());
        Coordinates position = vertex.getPosition();
        assertEquals(-2147483648, position.getX());
        assertEquals(0, position.getY());
        assertTrue(vertex instanceof VertexAccessible);
    }

    @Test
    void lift() {
        VertexRegistry vertices = simulation.getVertexRegistry();
        Vertex start = new Vertex(0, 100, new Coordinates(0, 0));
        Vertex end = new Vertex(1, 200, new Coordinates(1, 1));
        vertices.initialize(2);
        vertices.register(start);
        vertices.register(end);
        Lift lift = loader.loadLift("0 1 300 3 600", 42);

        assertEquals(42, lift.getIdentifier());
        assertSame(start, lift.getStart());
        assertSame(end, lift.getEnd());
        assertEquals(300, lift.getDepartureInterval());
        assertEquals(3, lift.getPassengerCapacity());
        assertEquals(600, lift.getRideTime());
    }

    @Test
    void slope() {
        VertexRegistry vertices = simulation.getVertexRegistry();
        Vertex v0 = new Vertex(0, 0, new Coordinates(0, 0));
        Vertex v1 = new Vertex(1, 0, new Coordinates(1, 0));
        vertices.initialize(2);
        vertices.register(v0);
        vertices.register(v1);
        Slope slope = loader.loadSlope("0 1 8 60 0.5 0.75", 7);
        assertEquals(7, slope.getIdentifier());
        assertSame(v0, slope.getStart());
        assertSame(v1, slope.getEnd());
        assertEquals(60, slope.getRideTime());
        assertEquals(1, v0.getSlopeCount());
        assertSame(slope, v0.getSlopes()[0]);
        Skier skier = new Skier(
                0,
                v0,
                5,
                0.0,
                1.0,
                0.0,
                simulation.getClock().getStartTime(),
                simulation.getEventBroker(),
                simulation.getClock());
        assertEquals(0.4, slope.calculateAppeal(skier), 1e-9);
    }

    @Test
    void time() {
        Time time = loader.loadTime("1:2:3");
        assertEquals(1, time.getHours());
        assertEquals(2, time.getMinutes());
        assertEquals(3, time.getSeconds());
    }

    @Test
    void timeNormalizesOverflow() {
        Time time = loader.loadTime("0:0:3661");
        assertEquals(1, time.getHours());
        assertEquals(1, time.getMinutes());
        assertEquals(1, time.getSeconds());
    }

    @Test
    void skierGroupSingleUntracked() {
        registerVerticesThrough(2);
        Scanner scanner = new Scanner(String.join(
                "\n",
                "1 4 0.25",
                "0.6 0.4",
                "2 10:15:30"));
        scanner.useLocale(Locale.ENGLISH);
        Skier[] skiers = loader.loadSkierGroup(scanner);
        assertEquals(1, skiers.length);
        assertFalse(skiers[0] instanceof SkierTracked);
        assertEquals(0, skiers[0].getIdentifier());
        assertEquals(4, skiers[0].getProficiency());
        assertEquals(0.6, skiers[0].getDifficultyWeight(), 0.0);
        assertEquals(0.4, skiers[0].getSurfaceWeight(), 0.0);
        assertEquals(10, skiers[0].getStartTime().getHours());
        assertEquals(15, skiers[0].getStartTime().getMinutes());
        assertEquals(30, skiers[0].getStartTime().getSeconds());
    }

    @Test
    void skierGroupSingleTracked() {
        registerVerticesThrough(0);
        Scanner scanner = new Scanner(String.join(
                "\n",
                "1 7 1.0 s",
                "0.0 1.0",
                "0 09:00:00"));
        scanner.useLocale(Locale.ENGLISH);
        Skier[] skiers = loader.loadSkierGroup(scanner);
        assertEquals(1, skiers.length);
        assertTrue(skiers[0] instanceof SkierTracked);
        assertEquals(7, skiers[0].getProficiency());
    }

    @Test
    void skierGroupMultipleIncrementsIdentifiersAndStartTimes() {
        registerVerticesThrough(1);
        Scanner scanner = new Scanner(String.join(
                "\n",
                "2 3 0.0",
                "1.0 0.0",
                "1 12:00:00 90"));
        scanner.useLocale(Locale.ENGLISH);
        Skier[] skiers = loader.loadSkierGroup(scanner);
        assertEquals(2, skiers.length);
        assertEquals(0, skiers[0].getIdentifier());
        assertEquals(1, skiers[1].getIdentifier());
        assertEquals(0, skiers[0].getStartTime().compareTo(new Time(12, 0, 0)));
        assertEquals(0, skiers[1].getStartTime().compareTo(new Time(12, 1, 30)));
    }

    @Test
    void loadExample() {
        String testCase = String.join("\n",
                "4",
                "859 5 1 s",
                "919 3 6",
                "879 1 2 s",
                "1120 2 9",

                "2",
                "0 1 10 4 240",
                "2 3 8 6 360",

                "4",
                "1 0 3 150 0.3 0.9998",
                "3 1 7 180 0.3 0.9997",
                "1 2 4 120 0.3 0.9998",
                "2 0 2 60 0.3 0.99985",

                "5",
                "180 3 0.1",
                "1 0",
                "0 09:00:00 15",
                "120 7 0.1",
                "1 0",
                "2 09:00:00 20",
                "1 2 0 s",
                "0.8 0.2",
                "0 09:05:30",
                "1 10 0 s",
                "0.8 0.2",
                "0 09:10:00",
                "1 5 1 s",
                "0.8 0.2",
                "2 09:15:00");
        assertDoesNotThrow(() -> loader.load(new Scanner(testCase)));
    }

    private void registerVerticesThrough(int maxIdentifier) {
        VertexRegistry vertices = simulation.getVertexRegistry();
        vertices.initialize(maxIdentifier + 1);
        for (int i = 0; i <= maxIdentifier; i++) {
            vertices.register(new Vertex(i, i, new Coordinates(i, 0)));
        }
    }
}
