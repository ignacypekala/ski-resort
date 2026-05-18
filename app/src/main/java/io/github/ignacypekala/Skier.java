package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.SimulationObject;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.lift.*;

import java.util.Random;

public class Skier extends SimulationObject {
    private Vertex startPoint;
    private Vertex location;
    private int proficiency;
    private double spontaneity;
    private double difficultyWeight;
    private double surfaceWeight;
    private Time startTime;

    private Random generator = new Random();

    private Publisher eventPublisher;
    private Clock clock;

    public Skier(
            int identifier,
            Vertex startPoint,
            int proficiency,
            double spontaneity,
            double difficultyWeight,
            double surfaceWeight,
            Time startTime,
            Publisher eventPublisher,
            Clock clock) {
        super(identifier);
        this.startPoint = startPoint;
        location = startPoint;

        if (proficiency < 0 || proficiency > 10) {
            throw new IllegalArgumentException(
                    "Proficiency must be in range {0, ..., 10}");
        }
        this.proficiency = proficiency;

        if (spontaneity < 0 || spontaneity > 1) {
            throw new IllegalArgumentException(
                    "Spontaneity must be in range [0, 1]");
        }
        this.spontaneity = spontaneity;

        if (difficultyWeight < 0 || difficultyWeight > 1) {
            throw new IllegalArgumentException(
                    "The difficulty appeal weight must be in range [0, 1]");
        }
        this.difficultyWeight = difficultyWeight;

        if (surfaceWeight < 0 || surfaceWeight > 1) {
            throw new IllegalArgumentException(
                    "The surface appeal weight must be in range [0, 1]");
        }
        this.surfaceWeight = surfaceWeight;

        this.startTime = startTime;

        this.eventPublisher = eventPublisher;
        this.clock = clock;

        scheduleArrival();
    }

    private void scheduleArrival() {
        Arrival event = new Arrival();
        eventPublisher.send(event);
    }

    private void ski() {
        chooseEdge().ride(this);
    }

    Edge chooseEdge() {
        if (location.getEdgeCount() <= 0) {
            throw new IllegalStateException(
                    "Unfulfilled assumption that every vertex has at least one edge.");
        }
        Edge[] edges = location.getEdges();
        if (generator.nextDouble() < spontaneity) {
            return edges[generator.nextInt(0, location.getEdgeCount())];
        } else {
            double maxAppeal = -1;
            Edge mostAppealing = null;
            for (int i = 0; i < location.getEdgeCount(); i++) {
                Edge edge = edges[i];
                double appeal = edge.calculateAppeal(this);
                if (appeal > maxAppeal) {
                    maxAppeal = appeal;
                    mostAppealing = edge;
                }
            }
            return mostAppealing;
        }
    }

    public void rideSlope(Slope slope) {
        SlopeRideFinished event = new SlopeRideFinished(slope, clock);
        eventPublisher.send(event);
        rideStarted(slope);
    }

    public void rideStarted(Edge edge) {
        location = null;
        rideStartedHook(edge);
    }

    public void rideFinished(Edge edge) {
        location = edge.getEnd();
        rideFinishedHook(edge);
        edge.rideFinished();
        if (!clock.isTimeUp()) {
            ski();
        }
    }

    // Empty hooks to be overridden by subclasses
    public void rideStartedHook(Edge edge) {
    }

    public void rideFinishedHook(Edge edge) {
    }

    public void liftQueueJoinedHook(Lift lift) {
    }

    private class SlopeRideFinished extends RelativeEvent {
        private Edge edge;

        public SlopeRideFinished(Edge edge, Clock clock) {
            super(clock, edge.getRideTime());
            this.edge = edge;
        }

        public void handle() {
            rideFinished(edge);
        }

        public String toString() {
            return String.format(
                    "%s finished a ride on %s",
                    Skier.this,
                    edge);
        }
    }

    private class Arrival extends Event {
        public Arrival() {
            super(startTime);
        }

        public void handle() {
            ski();
        }

        public String toString() {
            return String.format(
                    "%s has arrived at %s",
                    Skier.this,
                    startPoint);
        }
    }

    @Override
    public String toString() {
        return String.format("skier %d", getIdentifier());
    }

    public int getProficiency() {
        return proficiency;
    }

    public double getDifficultyWeight() {
        return difficultyWeight;
    }

    public double getSurfaceWeight() {
        return surfaceWeight;
    }

    double getSpontaneity() {
        return spontaneity;
    }

    Vertex getStartPoint() {
        return startPoint;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Vertex getLocation() {
        return location;
    }

}
