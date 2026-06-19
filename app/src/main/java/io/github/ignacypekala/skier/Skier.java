package io.github.ignacypekala.skier;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.*;

import java.util.Random;
import java.util.Objects;

public abstract class Skier extends SimulationObject {
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

    private SkierListener listener = null; // optional

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

    public Skier(
            final int identifier,
            final SkierGroupProfile groupProfile,
            final Time startTime,
            final SimulationContext simulationContext,
            final SkierListener listern) {
        this(identifier, groupProfile, startTime, simulationContext);
        this.listener = listener;
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

    protected abstract Edge chooseBestEdge();

    private void ride(final Edge edge) {
        edge.ride(this);
    }

    public void rideSlope(final Slope slope) {
        final RideFinished event = new RideFinished(slope, this, clock);
        eventPublisher.publish(event);
        rideStarted(slope);
    }

    public void rideStarted(final Edge edge) {
        location = null;
        edge.rideStarted();
        if (listener != null) {
            listener.onRideStarted(this, edge);
        }
    }

    public void rideFinished(final Edge edge) {
        location = edge.getEnd();
        if (listener != null) {
            listener.onRideFinished(this, edge);
        }
        if (!clock.isTimeUp()) {
            decideAndRide();
        }
    }

    public void liftQueueJoinedHook(final Lift lift) {
        if (listener != null) {
            listener.onLiftQueueJoined(this, lift);
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
