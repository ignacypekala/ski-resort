package io.github.ignacypekala;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

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
}
