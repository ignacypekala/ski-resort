package io.github.ignacypekala;

public abstract class Edge {
    private Vertex start;
    private Vertex end;

    private int rideCount = 0;

    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
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

    public int getRideCount() {
        return rideCount;
    }

    public abstract double appeal(Skier skier);
}
