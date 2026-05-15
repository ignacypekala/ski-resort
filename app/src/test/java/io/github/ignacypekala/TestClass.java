package io.github.ignacypekala;

import io.github.ignacypekala.EventQueue.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.utils.Coordinates;

public class TestClass {
    public static class TestSlope extends Slope {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);
        static Vertex b = new Vertex(0, pos, 1);

        public TestSlope(
            double durability,
            int difficulty,
            double baseAppeal
        ) {
            super(a, b, durability, difficulty, 420, baseAppeal);
        }
    }

    public static class TestSkier extends Skier {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);
        static EventPublisher eventPublisher = new EventQueueList();
        static Clock clock = new Simulation();

        public TestSkier(
            int proficiency,
            double difficultyWeight,
            double surfaceWeight
        ) {
            super(
                a,
                proficiency,
                0,
                difficultyWeight,
                surfaceWeight,
                0,
                new Time(0, 0, 0),
                eventPublisher,
                clock
            );
        }
    }
}
