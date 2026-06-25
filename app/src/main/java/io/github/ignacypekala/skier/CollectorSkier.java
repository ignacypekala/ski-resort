package io.github.ignacypekala.skier;

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
    // Compares two slopes by: visit count, distance and appeal.
    protected int compareRouteOptions(RouteOption a, RouteOption b) {
        Slope slopeA = a.slope();
        Slope slopeB = b.slope();

        Integer visitsA = visitCount.getOrDefault(slopeA, 0);
        Integer visitsB = visitCount.getOrDefault(slopeB, 0);
        int visitsComparison = Integer.compare(visitsA, visitsB);
        if (visitsComparison != 0) {
            // Fewer visits = better
            return -visitsComparison;
        }

        int depthComparison = Integer.compare(a.distance(), b.distance());
        if (depthComparison != 0) {
            // Shorter distance = better
            return -depthComparison;
        }

        double appealA = slopeA.calculateAppeal(this);
        double appealB = slopeB.calculateAppeal(this);
        int appealComparision = Double.compare(appealA, appealB);
        if (appealComparision != 0) {
            // Higher appeal = better
            return appealComparision;
        }

        return 1;
    }
}
