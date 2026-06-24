package io.github.ignacypekala.skier;

import java.util.Comparator;
import java.util.HashMap;

import io.github.ignacypekala.Slope;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public class CollectorSkier extends StrategicSkier {
    private HashMap<Slope, Integer> visitCount;

    public CollectorSkier(
            final int identifier,
            final SkierGroupProfile groupProfile,
            final Time startTime,
            final SimulationContext simulationContext) {
        super(identifier, groupProfile, startTime, simulationContext);
        visitCount = new HashMap<>();
    }

    @Override
    public void rideSlope(Slope slope) {
        // Mark slope as visited
        Integer count = visitCount.getOrDefault(slope, 0);
        visitCount.put(slope, count + 1);
        super.rideSlope(slope);
    }

    @Override
    protected int compareSlopes(Slope slopeA, Slope slopeB) {
        Integer visitsA = visitCount.getOrDefault(slopeA, 0);
        Integer visitsB = visitCount.getOrDefault(slopeB, 0);
        int visitsComparison = Integer.compare(visitsA, visitsB);
        if (visitsComparison != 0) {
            // Fewer visits = better
            return -visitsComparison;
        }

        double appealA = slopeA.calculateAppeal(this);
        double appealB = slopeB.calculateAppeal(this);
        int appealComparision = Double.compare(appealA, appealB);
        if (appealComparision != 0) {
            return appealComparision;
        }

        return 1;
    }
}
