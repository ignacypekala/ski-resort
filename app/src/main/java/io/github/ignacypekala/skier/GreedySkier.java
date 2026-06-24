package io.github.ignacypekala.skier;

import io.github.ignacypekala.Slope;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public class GreedySkier extends StrategicSkier {
    public GreedySkier(
            final int identifier,
            final SkierGroupProfile groupProfile,
            final Time startTime,
            final SimulationContext simulationContext) {
        super(identifier, groupProfile, startTime, simulationContext);
    }

    @Override
    protected int compareSlopes(Slope slopeA, Slope slopeB) {
        return Double.compare(slopeA.calculateAppeal(this), slopeB.calculateAppeal(this));
    }
}
