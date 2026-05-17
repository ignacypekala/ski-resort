package io.github.ignacypekala;

import org.junit.jupiter.api.Test;

import io.github.ignacypekala.utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class SlopeTest {
    private Skier skier = new TestClass.TestSkier(0, 0.0, 0.0);

    @Test
    void construct() {
        Vertex a = new Vertex(0, new Coordinates(1, 1), 'a');
        Vertex b = new Vertex(0, new Coordinates(-1, -1), 'b');
        assertEquals(0, a.getSlopeCount());
        Slope slope = new Slope(a, b, 1, 5, 10, 1);
        assertEquals(1, a.getSlopeCount());

        assertEquals(slope, a.getSlopes()[0]);
    }

    @Test
    void rideTime() {
        Slope slope = new TestClass.TestSlope(0, 0, 0, 420);
        assertEquals(420, slope.getRideTime(), 0.000001);
    }

    @Test
    void badInput() {
        Class<IllegalArgumentException> badArg = IllegalArgumentException.class;
        assertThrows(badArg, () -> new TestClass.TestSlope(-1, 0, 0, 0));
        assertThrows(badArg, () -> new TestClass.TestSlope(2, 0, 0, 0));
        assertThrows(badArg, () -> new TestClass.TestSlope(0, -1, 0, 0));
        assertThrows(badArg, () -> new TestClass.TestSlope(0, 11, 0, 0));
        assertThrows(badArg, () -> new TestClass.TestSlope(0, 0, -1, 0));
        assertThrows(badArg, () -> new TestClass.TestSlope(0, 0, 2, 0));

    }

    @Test
    void difficultyAppealVeryHard() {
        Slope slope = new TestClass.TestSlope(0, 5, 0, 0);
        assertEquals(0, slope.difficultyAppeal(0));
    }

    @Test
    void difficultyAppealHard() {
        Slope slopeA = new TestClass.TestSlope(0, 3, 0, 0);
        assertEquals(3.0 / 5.0, slopeA.difficultyAppeal(1), 0.000001);
        Slope slopeB = new TestClass.TestSlope(0, 5, 0, 0);
        assertEquals(1.0 / 5.0, slopeB.difficultyAppeal(1), 0.000001);
        Slope slopeC = new TestClass.TestSlope(0, 1, 0, 0);
        assertEquals(1, slopeC.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealEasy() {
        Slope slope = new TestClass.TestSlope(0, 0, 0, 0);
        assertEquals(1.0 - 1.0 / 7.0, slope.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealVeryEasy() {
        Slope slope = new TestClass.TestSlope(0, 0, 0, 0);
        assertEquals(0.2, slope.difficultyAppeal(6), 0.000001);
    }

    @Test
    void surfaceInvulnerable() {
        Slope slope = new TestClass.TestSlope(1, 0, 0, 0);
        assertEquals(1.0, slope.surfaceAppeal());
        slope.ride(skier);
        assertEquals(1.0, slope.surfaceAppeal());
        for (int i = 0; i < 25; i++) {
            slope.ride(skier);
        }
        assertEquals(1.0, slope.surfaceAppeal());
    }

    @Test
    void surfaceVulnerable() {
        Slope slope = new TestClass.TestSlope(0.5, 0, 0.75, 0);
        assertEquals(0.75 + 0.25 * 1, slope.surfaceAppeal());
        slope.rideFinished();
        assertEquals(0.75 + 0.25 * 0.5, slope.surfaceAppeal());
        slope.rideFinished();
        assertEquals(0.75 + 0.25 * 0.5 * 0.5, slope.surfaceAppeal());
    }

    @Test
    void accumulativeAppeal() {
        Slope surfaceSlope = new TestClass.TestSlope(1, 10, 1, 0);
        Skier surfaceSkier = new TestClass.TestSkier(0, 0, 1);
        assertEquals(1.0, surfaceSlope.appeal(surfaceSkier));

        Slope difficultySlope = new TestClass.TestSlope(0, 10, 0, 0);
        Skier proficientSkier = new TestClass.TestSkier(10, 1, 0);
        assertEquals(1.0, difficultySlope.appeal(proficientSkier));

        Slope allRoundSlope = new TestClass.TestSlope(0.5, 5, 0.5, 0);
        Skier allRoundSkier = new TestClass.TestSkier(5, 0.5, 0.5);
        assertEquals(1.0, allRoundSlope.appeal(allRoundSkier));
    }
}
