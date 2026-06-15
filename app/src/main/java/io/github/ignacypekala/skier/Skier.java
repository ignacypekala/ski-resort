package io.github.ignacypekala.skier;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.*;

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
            final SkierGroupProfile groupProfile,
            final Time startTime,
            final SimulationContext simulationContext) {
        super(identifier);

        startPoint = groupProfile.startPoint();
        location = startPoint;

        proficiency = groupProfile.proficiency();
        if (proficiency < 0 || proficiency > 10) {
            throw new IllegalArgumentException(
                    "Proficiency must be in range {0, ..., 10}");
        }

        spontaneity = groupProfile.spontaneity();
        if (spontaneity < 0 || spontaneity > 1) {
            throw new IllegalArgumentException(
                    "Spontaneity must be in range [0, 1]");
        }

        difficultyWeight = groupProfile.difficultyWeight();
        if (difficultyWeight < 0 || difficultyWeight > 1) {
            throw new IllegalArgumentException(
                    "The difficulty appeal weight must be in range [0, 1]");
        }

        surfaceWeight = groupProfile.surfaceWeight();
        if (surfaceWeight < 0 || surfaceWeight > 1) {
            throw new IllegalArgumentException(
                    "The surface appeal weight must be in range [0, 1]");
        }

        this.startTime = startTime;

        this.eventPublisher = simulationContext.publisher();
        this.clock = simulationContext.clock();

        scheduleDayStart();
    }

    private void scheduleDayStart() {
        final DayStart event = new DayStart(this);
        eventPublisher.publish(event);
    }

    public void decideAndRide() {
        ride(chooseEdge());
    }

    Edge chooseEdge() {
        if (location.getEdgeCount() <= 0) {
            throw new IllegalStateException(
                    "Unfulfilled assumption that every vertex has at least one edge.");
        }
        if (generator.nextDouble() < spontaneity) {
            return chooseRandomEdge();
        } else {
            return chooseBestEdge();
        }
    }

    private Edge chooseRandomEdge() {
        int chosenEdgeIndex = generator.nextInt(0, location.getEdgeCount());
        return location.getEdges()[chosenEdgeIndex];
    }

    protected Edge chooseBestEdge() {
        Edge[] edges = location.getEdges();
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

    private void ride(final Edge edge) {
        edge.ride(this);
    }

    public void rideSlope(final Slope slope) {
        final SlopeRideFinished event = new SlopeRideFinished(slope, clock);
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

    private class SlopeRideFinished extends RelativeEvent {
        private final Edge edge;

        public SlopeRideFinished(final Edge edge, final Clock clock) {
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

    public Vertex getStartPoint() {
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
