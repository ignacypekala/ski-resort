package io.github.ignacypekala.skier;

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
    protected int compareRouteOptions(RouteOption a, RouteOption b) {
        return Double.compare(a.slope().calculateAppeal(this), b.slope().calculateAppeal(this));
    }
}
