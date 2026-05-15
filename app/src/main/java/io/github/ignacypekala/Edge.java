package io.github.ignacypekala;

public abstract class Edge {
    private Vertex start;
    private Vertex end;
    private int rideTime;
    private int rideCount = 0;

    public Edge(Vertex start, Vertex end, int rideTime) {
        this.start = start;
        this.end = end;
        this.rideTime = rideTime;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void ride() {
        rideCount += 1;
    }

    public int getRideTime() {
        return rideTime;
    }

    public int getRideCount() {
        return rideCount;
    }

    public abstract double appeal(Skier skier);
}
