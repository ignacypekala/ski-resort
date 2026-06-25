package io.github.ignacypekala.skier;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;
import io.github.ignacypekala.*;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public abstract class StrategicSkier extends Skier {
    private Queue<Edge> plan;

    protected StrategicSkier(
            final int identifier,
            final SkierGroupProfile groupProfile,
            final Time startTime,
            final SimulationContext simulationContext) {
        super(identifier, groupProfile, startTime, simulationContext);
        plan = new ArrayDeque<>();
    }

    @Override
    Edge chooseEdge() {
        if (plan.isEmpty()) {
            return super.chooseEdge();
        } else {
            return chooseBestEdge();
        }
    }

    @Override
    protected Edge chooseBestEdge() {
        if (plan.isEmpty()) {
            plan = createNewPlan();
        }
        return plan.poll();
    }

    private record TraversalRegistryEntry(Edge enteredVia, int depth) {};

    // Creates a new plan based on the subclass-defined partial ordering.
    private Queue<Edge> createNewPlan() {
        if (getLocation() == null) {
            throw new IllegalStateException(
                "Skier location cannot be null when planning a new route."
            );
        }

        RouteOption bestRouteOption = null;
        Queue<Edge> bestSlopePath = null;
        final HashMap<Vertex, TraversalRegistryEntry> traversalRegistry = new HashMap<>();
        final Queue<Vertex> bfsQueue = new ArrayDeque<>();

        bfsQueue.add(getLocation());
        while (!bfsQueue.isEmpty()) {
            final Vertex vertex = bfsQueue.remove();
            TraversalRegistryEntry vertexEntry = traversalRegistry.get(vertex);
            int depth = vertexEntry.depth();

            for (final Slope contender : vertex.getSlopes()) {
                RouteOption contenderRouteOption = new RouteOption(contender, depth + 1);
                boolean isBetter = compareRouteOptions(contenderRouteOption, bestRouteOption) > 0;
                if (bestRouteOption == null || isBetter) {
                    bestRouteOption = contenderRouteOption;
                    // The new best slope hasn't been added to the registry yet.
                    bestSlopePath = reconstructPath(traversalRegistry, contender.getStart());
                    bestSlopePath.add(contender);

                }
            }

            for (final Edge edge : vertex.getEdges()) {
                final Vertex destination = edge.getEnd();
                if (!traversalRegistry.containsKey(destination)) {
                    bfsQueue.add(destination);
                    traversalRegistry.put(destination, new TraversalRegistryEntry(edge, depth + 1));
                } 
            }
        }
        return bestSlopePath;
    };

    private Queue<Edge> reconstructPath(
            final HashMap<Vertex, TraversalRegistryEntry> traversalRegistry,
            final Vertex destination) {
        TraversalRegistryEntry destinationEntry = traversalRegistry.get(destination);
        int pathLength = destinationEntry == null ? 0 : destinationEntry.depth();

        final ArrayDeque<Edge> path = new ArrayDeque<Edge>(pathLength);

        Vertex vertex = destination;
        TraversalRegistryEntry entry = traversalRegistry.get(vertex);
        while (entry != null) {
            Edge edge = entry.enteredVia();
            path.addFirst(edge);
            vertex = edge.getStart();
            entry = traversalRegistry.get(vertex);
        }
        return path;
    }

    protected record RouteOption(Slope slope, int distance) {};

    //  - positive if a > b
    //  - 0 if a == b
    //  - negative if a < b
    protected abstract int compareRouteOptions(RouteOption a, RouteOption b);

}
