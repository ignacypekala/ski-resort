package io.github.ignacypekala.skier;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;
import io.github.ignacypekala.*;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public abstract class StrategicSkier extends Skier {
    private Queue<Edge> plan;

    public StrategicSkier(
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

    // Creates a new plan based on the subclass-defined partial ordering.
    private Queue<Edge> createNewPlan() {
        if (getLocation() == null) {
            throw new IllegalStateException(
                "Skier location cannot be null when planning a new route."
            );
        }

        Slope bestSlope = null;
        Queue<Edge> bestSlopePath = new ArrayDeque<>();
        final HashMap<Vertex, Edge> traversalRegistry = new HashMap<>();
        final Queue<Vertex> bfsQueue = new ArrayDeque<>();

        bfsQueue.add(getLocation());
        while (!bfsQueue.isEmpty()) {
            final Vertex vertex = bfsQueue.remove();
            for (final Slope slope : vertex.getSlopes()) {
                if (bestSlope == null || compareSlopes(slope, bestSlope) > 0) {
                    bestSlope = slope;
                    bestSlopePath = reconstructPath(traversalRegistry, bestSlope.getStart());
                    bestSlopePath.add(slope);
                }
            }

            for (final Edge edge : vertex.getEdges()) {
                final Vertex destination = edge.getEnd();
                if (!traversalRegistry.containsKey(destination)) {
                    bfsQueue.add(destination);
                    traversalRegistry.put(destination, edge);
                } 
            }
        }
        return bestSlopePath;
    };

    private Queue<Edge> reconstructPath(
            final HashMap<Vertex, Edge> visited,
            final Vertex destination) {
        final ArrayDeque<Edge> queue = new ArrayDeque<>();
        Vertex vertex = destination;
        Edge edge = visited.get(vertex);
        while (edge != null) {
            queue.addFirst(edge);
            vertex = edge.getStart();
            edge = visited.get(vertex);
        }
        return queue;
    }

    //  - positive if a > b
    //  - 0 if a == b
    //  - negative if a < b
    protected abstract int compareSlopes(Slope slopeA, Slope slopeB);

}
