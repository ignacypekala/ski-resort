package io.github.ignacypekala.skier;

import io.github.ignacypekala.event.RelativeEvent;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.Edge;

public class RideFinished extends RelativeEvent {
    private final Edge edge;
    private final Skier skier;

    public RideFinished(
            final Edge edge,
            final Skier skier,
            final Clock clock) {
        super(clock, edge.getRideTime());
        this.edge = edge;
        this.skier = skier;
    }

    @Override
    public void handle() {
        skier.rideFinished(edge);
    }

    @Override
    public String toString() {
        return String.format(
            "%s finished a ride on %s",
            skier,
            edge);
    }
}
