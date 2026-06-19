package io.github.ignacypekala;

import java.util.Objects;

import io.github.ignacypekala.simulation.SimulationObject;

public abstract class Edge extends SimulationObject {
    private final Vertex start;
    private final Vertex end;
    private final int rideTime;
    private int rideCount = 0;

    public Edge(
            final int identifier,
            final Vertex start,
            final Vertex end,
            final int rideTime) {
        super(identifier);
        this.start = start;
        this.end = end;
        this.rideTime = rideTime;
    }

    public abstract double calculateAppeal(Skier skier);

    public abstract void ride(Skier skier);

    // Adds this edge to the appropriate collection in the start vertex.
    public abstract void addStartEdge();

    public abstract String getRideStartMessage(Skier skier);

    public abstract String getRideFinishMessage(Skier skier);

    public void rideStarted() {
        rideCount += 1;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public int getRideTime() {
        return rideTime;
    }

    public int getRideCount() {
        return rideCount;
    }

    public abstract String toString();
}
