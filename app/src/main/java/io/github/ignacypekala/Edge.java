package io.github.ignacypekala;

import io.github.ignacypekala.simulation.Object;

public abstract class Edge extends Object {
    private Vertex start;
    private Vertex end;
    private int rideTime;
    private int rideCount = 0;

    public Edge(
            int identifier,
            Vertex start,
            Vertex end,
            int rideTime) {
        super(identifier);
        this.start = start;
        this.end = end;
        this.rideTime = rideTime;
    }

    public abstract double appeal(Skier skier);

    public abstract void ride(Skier skier);

    // Adds this edge to the appropriate collection in the start vertex.
    public abstract void addStartEdge();

    public abstract String getRideStartMessage(Skier skier);

    public abstract String getRideFinishMessage(Skier skier);

    public void rideFinished() {
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
