package io.github.ignacypekala;

import java.util.function.Consumer;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.utils.Coordinates;

public class TestClass {
    static EventQueueList eventQueue = new EventQueueList();
    static Simulation simulation = new Simulation();

    public static class TestSlope extends Slope {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);
        static Vertex b = new Vertex(0, pos, 1);

        public TestSlope(
            double durability,
            int difficulty,
            double baseAppeal,
            int rideTime
        ) {
            super(a, b, durability, difficulty, rideTime, baseAppeal);
        }
    }

    public static class TestSkier extends Skier {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, pos, 0);

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
                eventQueue,
                simulation
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

    public static EventQueueList getEventQueue() { return eventQueue; }
    public static Simulation getSimulation() { return simulation; }
}
