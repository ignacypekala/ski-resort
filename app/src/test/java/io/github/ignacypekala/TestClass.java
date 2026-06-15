package io.github.ignacypekala;

import java.util.function.Consumer;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.utils.Coordinates;

public class TestClass {
    static Broker eventBroker = new EventQueue();
    static Simulation simulation = new Simulation();
    static Clock clock = simulation.getClock();

    public static class TestSlope extends Slope {
        static Coordinates pos = new Coordinates(0, 0);
        static Vertex a = new Vertex(0, 0, pos);
        static Vertex b = new Vertex(1, 0, pos);

        public TestSlope(
                int identifier,
                double wearResistance,
                int difficulty,
                double baseAppeal,
                int rideTime) {
            super(identifier, a, b, rideTime, wearResistance, difficulty, baseAppeal);
        }
    }

    public static class SnitchSkier extends Skier {
        Consumer<Edge> rideStartedCallback;
        Consumer<Edge> rideFinishedCallback;

        public SnitchSkier(
                int identifier,
                SkierGroupProfile groupProfile,
                Consumer<Edge> rideStartedCallback,
                Consumer<Edge> rideFinishedCallback) {
            super(
                identifier,
                groupProfile,
                clock.getStartTime(),
                simulation.getContext()
            );
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
}
