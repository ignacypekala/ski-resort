package io.github.ignacypekala.skier;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;
import io.github.ignacypekala.*;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.Time;

public abstract class StrategicSkier extends Skier {
    private Queue<Edge> plan;

    public StrategicSkier(
            final int identifier,
            final Vertex startPoint,
            final int proficiency,
            final double spontaneity,
            final double difficultyWeight,
            final double surfaceWeight,
            final Time startTime,
            final SimulationContext simulationContext) {
        super(
                identifier,
                startPoint,
                proficiency,
                spontaneity,
                difficultyWeight,
                surfaceWeight,
                startTime,
                simulationContext);
        plan = new ArrayDeque<>();
    }

    @Override
    Edge chooseEdge() {
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
                };
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

    private Queue<Edge> reconstructPath(final HashMap<Vertex, Edge> visited, final Vertex destination) {
        final Queue<Edge> queue = new ArrayDeque<>();
        Vertex vertex = destination;
        Edge edge = visited.get(vertex);
        while (edge != null) {
            queue.add(edge);
            vertex = edge.getStart();
            edge = visited.get(vertex);
        }
        return queue;
    }

    //  - positive if a better than b
    //  - 0 if equal
    //  - negative if a worse than b
    protected abstract int compareSlopes(Slope slopeA, Slope slopeB);

}
