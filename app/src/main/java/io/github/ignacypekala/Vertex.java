package io.github.ignacypekala;

import io.github.ignacypekala.utils.Coordinates;

public class Vertex {
    private final int altitude;
    private final Coordinates position;
    private final int identifier;

    public Vertex(int altitude, Coordinates position, int identifier) {
        this.altitude = altitude;
        this.position = position;
        this.identifier = identifier;
    }

    public int getAltitude() {
        return altitude;
    }

    public Coordinates getPosition() {
        return position;
    }

    public int getIdentifier() {
        return identifier;
    }

}
