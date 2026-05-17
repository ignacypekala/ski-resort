package io.github.ignacypekala;

import java.util.function.Consumer;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.utils.Coordinates;

public class TestClass {
    static EventBroker eventBroker = new EventQueueList();
    static Clock clock = new Simulation();

    public static class TestSlope extends Slope {
        static int identifier = 0;
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, 0, pos);
        static Vertex b = new Vertex(1, 0, pos);

        public TestSlope(
            double durability,
            int difficulty,
            double baseAppeal,
            int rideTime
        ) {
            super(identifier++, a, b, rideTime, durability, difficulty, baseAppeal);
        }
    }

    public static class TestSkier extends Skier {
        static int identifier = 0;
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, 0, pos);

        public TestSkier(
            int proficiency,
            double difficultyWeight,
            double surfaceWeight
        ) {
            super(
                identifier++,
                a,
                proficiency,
                0,
                difficultyWeight,
                surfaceWeight,
                clock.getStartTime(),
                eventBroker,
                clock
            );
        }
    }

    public static class SnitchSkier extends TestSkier {
        Consumer<Edge> rideStartedCallback;
        Consumer<Edge> rideFinishedCallback;
        public SnitchSkier(
            int proficiency,
            double difficultyWeight,
            double surfaceWeight,
            Consumer<Edge> rideStartedCallback,
            Consumer<Edge> rideFinishedCallback
        ) {
            super(proficiency, difficultyWeight, surfaceWeight);
            this.rideStartedCallback = rideStartedCallback;
            this.rideFinishedCallback = rideFinishedCallback;
        }

        @Override
        public void rideStartedHook(Edge edge) {
            super.rideStartedHook(edge);
            rideStartedCallback.accept(edge);
        }

        @Override
        public void rideFinishedHook(Edge edge) {
            super.rideFinishedHook(edge);
            rideFinishedCallback.accept(edge);
        }
    }

    public static EventBroker getEventBroker() { return eventBroker; }
    public static Clock getClock() { return clock; }
}
