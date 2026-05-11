package io.github.ignacypekala;

import io.github.ignacypekala.utils.Coordinates;

public class VertexConnected extends Vertex {
    private final int INITIAL_LIFT_CAPACITY = 10;
    private final int INITIAL_SLOPE_CAPACITY = 10;
    private int liftCount;
    private Lift[] lifts;
    private int slopeCount;
    private Slope[] slopes;

    public VertexConnected(int altitude, Coordinates position, int identifier) {
        super(altitude, position, identifier);
        lifts = new Lift[INITIAL_LIFT_CAPACITY];
        slopes = new Slope[INITIAL_SLOPE_CAPACITY];
    }

    public Lift[] getLifts() {
        return lifts;
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

}
