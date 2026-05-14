package io.github.ignacypekala;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.github.ignacypekala.utils.Coordinates;

public class SlopeTest {
    private class TestSlope extends Slope {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);
        static Vertex b = new Vertex(0, pos, 1);

        public TestSlope(double durability, int difficulty, double baseAppeal) {
            super(a, b, durability, difficulty, 0, baseAppeal);
        }
    }
    private class TestSkier extends Skier {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);
        public TestSkier(
            int proficiency,
            double difficultyWeight,
            double surfaceWeight
        ) {
            super(a, proficiency, 0, difficultyWeight, surfaceWeight, 0, 0);
        }
    }

    @Test
    void difficultyAppealVeryHard() {
        Slope slope = new TestSlope(0, 5, 0);
        assertEquals(0, slope.difficultyAppeal(0));
    }

    @Test
    void difficultyAppealHard() {
        Slope slopeA = new TestSlope(0, 3, 0);
        assertEquals(3.0 / 5.0, slopeA.difficultyAppeal(1), 0.000001);
        Slope slopeB = new TestSlope(0, 5, 0);
        assertEquals(1.0 / 5.0, slopeB.difficultyAppeal(1), 0.000001);
        Slope slopeC = new TestSlope(0, 1, 0);
        assertEquals(1, slopeC.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealEasy() {
        Slope slope = new TestSlope(0, 0, 0);
        assertEquals(1.0 - 1.0 / 7.0, slope.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealVeryEasy() {
        Slope slope = new TestSlope(0, 0, 0);
        assertEquals(0.2, slope.difficultyAppeal(6), 0.000001);
    }

    @Test
    void surfaceInvulnerable() {
        Slope slope = new TestSlope(1, 0, 0);
        assertEquals(1.0, slope.surfaceAppeal());
        slope.ride();
        assertEquals(1.0, slope.surfaceAppeal());
        for (int i = 0; i < 25; i++) {
            slope.ride();
        }
        assertEquals(1.0, slope.surfaceAppeal());
    }

    @Test
    void surfaceVulnerable() {
        Slope slope = new TestSlope(0.5, 0, 0.75);
        assertEquals(0.75 + 0.25 * 1, slope.surfaceAppeal());
        slope.ride();
        assertEquals(0.75 + 0.25 * 0.5, slope.surfaceAppeal());
        slope.ride();
        assertEquals(0.75 + 0.25 * 0.5 * 0.5, slope.surfaceAppeal());
    }

    @Test
    void accumulativeAppeal() {
        Slope surfaceSlope = new TestSlope(1, 10, 1);
        Skier surfaceSkier = new TestSkier(0, 0, 1);
        assertEquals(1.0, surfaceSlope.appeal(surfaceSkier));

        Slope difficultySlope = new TestSlope(0, 10, 0);
        Skier proficientSkier = new TestSkier(10, 1, 0);
        assertEquals(1.0, difficultySlope.appeal(proficientSkier));

        Slope allRoundSlope = new TestSlope(0.5, 5, 0.5);
        Skier allRoundSkier = new TestSkier(5, 0.5, 0.5);
        assertEquals(1.0, allRoundSlope.appeal(allRoundSkier));
    }
}
