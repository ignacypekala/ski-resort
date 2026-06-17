package io.github.ignacypekala.resort;

import io.github.ignacypekala.Slope;
import io.github.ignacypekala.Vertex;
import io.github.ignacypekala.lift.Lift;

public class Resort {
    private final Vertex[] vertices;
    private final Slope[] slopes;
    private final Lift[] lifts;

    public Resort(final Vertex[] vertices, final Slope[] slopes, final Lift[] lifts) {
        this.vertices = vertices;
        this.slopes = slopes;
        this.lifts = lifts;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Slope[] getSlopes() {
        return slopes;
    }

    public Lift[] getLifts() {
        return lifts;
    }
}
