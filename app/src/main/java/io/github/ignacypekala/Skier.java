package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.simulation.SimulationObject;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.lift.*;

import java.util.Random;
import java.util.Objects;

public class Skier extends SimulationObject {
    private final Vertex startPoint;
    private Vertex location;
    private final int proficiency;
    private final double spontaneity;
    private final double difficultyWeight;
    private final double surfaceWeight;
    private final Time startTime;

    private final Random generator = new Random();

    private final Publisher eventPublisher;
    private final Clock clock;

    public Skier(
            final int identifier,
            final Vertex startPoint,
            final int proficiency,
            final double spontaneity,
            final double difficultyWeight,
            final double surfaceWeight,
            final Time startTime,
            final Publisher eventPublisher,
            final Clock clock) {
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

        scheduleDayStart();
    }

    private void scheduleDayStart() {
        final DayStart event = new DayStart();
        eventPublisher.publish(event);
    }

    private void decideAndRide() {
        chooseEdge().ride(this);
    }

    private Edge chooseEdge() {
        if (location.getEdgeCount() <= 0) {
            throw new IllegalStateException(
                    "Unfulfilled assumption that every vertex has at least one edge.");
        }
        final Edge[] edges = location.getEdges();
        if (generator.nextDouble() < spontaneity) {
            return edges[generator.nextInt(0, location.getEdgeCount())];
        } else {
            double maxAppeal = -1;
            Edge mostAppealing = null;
            for (int i = 0; i < location.getEdgeCount(); i++) {
                final Edge edge = edges[i];
                final double appeal = edge.calculateAppeal(this);
                if (appeal > maxAppeal) {
                    maxAppeal = appeal;
                    mostAppealing = edge;
                }
            }
            return mostAppealing;
        }
    }

    public void rideSlope(final Slope slope) {
        final SlopeRideFinished event = new SlopeRideFinished(slope, this, clock);
        eventPublisher.publish(event);
        rideStarted(slope);
    }

    public void rideStarted(final Edge edge) {
        location = null;
        edge.rideStarted();
        rideStartedHook(edge);
    }

    public void rideFinished(final Edge edge) {
        location = edge.getEnd();
        rideFinishedHook(edge);
        if (!clock.isTimeUp()) {
            decideAndRide();
        }
    }

    // Empty hooks to be overridden by subclasses
    public void rideStartedHook(final Edge edge) {
    }

    public void rideFinishedHook(final Edge edge) {
    }

    public void liftQueueJoinedHook(final Lift lift) {
    }

    private class DayStart extends Event {
        public DayStart() {
            super(startTime);
        }

        @Override
        public void handle() {
            decideAndRide();
        }

        @Override
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Skier other = (Skier) obj;
        if (proficiency != other.proficiency) {
            return false;
        }
        if (Double.compare(spontaneity, other.spontaneity) != 0) {
            return false;
        }
        if (Double.compare(difficultyWeight, other.difficultyWeight) != 0) {
            return false;
        }
        if (Double.compare(surfaceWeight, other.surfaceWeight) != 0) {
            return false;
        }

        return Objects.equals(startPoint, other.startPoint)
                && Objects.equals(location, other.location)
                && Objects.equals(startTime, other.startTime);
    }

}
