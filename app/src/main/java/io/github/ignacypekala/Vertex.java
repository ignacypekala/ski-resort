package io.github.ignacypekala;

import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;

public class Vertex extends SimulationObject {
    private static int nextIdentifier = 0;

    private final int altitude;
    private final Coordinates position;

    private final int INITIAL_LIFT_CAPACITY = 10;
    private int liftCount;
    private Lift[] lifts;

    private final int INITIAL_SLOPE_CAPACITY = 10;
    private int slopeCount;
    private Slope[] slopes;

    public Vertex(int altitude, Coordinates position) {
        super(nextIdentifier++);
        this.altitude = altitude;
        this.position = position;
        lifts = new Lift[INITIAL_LIFT_CAPACITY];
        slopes = new Slope[INITIAL_SLOPE_CAPACITY];
    }

    public int getAltitude() {
        return altitude;
    }

    public Coordinates getPosition() {
        return position;
    }

    public int getLiftCount() {
        return liftCount;
    }

    public Lift[] getLifts() {
        return lifts;
    }

    public int getSlopeCount() {
        return slopeCount;
    }

    public Slope[] getSlopes() {
        return slopes;
    }

    public void addLift(Lift lift) {
        if (liftCount == lifts.length) {
            Lift[] newLifts = new Lift[lifts.length * 2];
            System.arraycopy(lifts, 0, newLifts, 0, lifts.length);
            lifts = newLifts;
        }
        lifts[liftCount++] = lift;
    }

    public void addSlope(Slope slope) {
        if (slopeCount == slopes.length) {
            Slope[] newSlopes = new Slope[slopes.length * 2];
            System.arraycopy(slopes, 0, newSlopes, 0, slopes.length);
            slopes = newSlopes;
        }
        slopes[slopeCount++] = slope;
    }

    public int getEdgeCount() {
        return liftCount + slopeCount;
    }

    public Edge[] getEdges() {
        Edge[] edges = new Edge[slopeCount + liftCount];
        System.arraycopy(slopes, 0, edges, 0, slopeCount);
        System.arraycopy(lifts, 0, edges, slopeCount, liftCount);
        return edges;
    }

    public String toString() {
        return String.format("vertex %d", getIdentifier());
    }
}
