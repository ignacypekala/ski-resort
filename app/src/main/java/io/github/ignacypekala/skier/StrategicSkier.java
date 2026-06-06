package io.github.ignacypekala.skier;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;
import io.github.ignacypekala.*;
import io.github.ignacypekala.event.Publisher;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.utils.Time;

public abstract class StrategicSkier extends Skier {
    private Queue<Edge> plan;

    public StrategicSkier(
            int identifier, Vertex startPoint, int proficiency, double spontaneity,
            double difficultyWeight, double surfaceWeight, Time startTime, Publisher eventPublisher,
            Clock clock) {
        super(identifier, startPoint, proficiency, spontaneity, difficultyWeight, surfaceWeight, startTime,
                eventPublisher, clock);
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

        Slope leader = null;
        Queue<Edge> leaderPath = new ArrayDeque<>();
        HashMap<Vertex, Edge> visited = new HashMap<>();
        Queue<Vertex> bfsQueue = new ArrayDeque<>();

        bfsQueue.add(getLocation());
        while (!bfsQueue.isEmpty()) {
            Vertex vertex = bfsQueue.remove();
            for (Slope slope : vertex.getSlopes()) {
                if (leader == null || compareSlopes(slope, leader) > 0) {
                    leader = slope;
                    leaderPath = reconstructPath(visited, leader.getStart());
                    leaderPath.add(slope);
                };
            }
            for (Edge edge : vertex.getEdges()) {
                Vertex destination = edge.getEnd();
                if (!visited.containsKey(destination)) {
                    bfsQueue.add(destination);
                    visited.put(destination, edge);
                } 
            }
        }
        return leaderPath;
    };

    // Reconstructs the path to the given destination vertex based on the visited hashmap.
    private Queue<Edge> reconstructPath(HashMap<Vertex, Edge> visited, Vertex destination) {
        Queue<Edge> queue = new ArrayDeque<>();
        Vertex vertex = destination;
        Edge edge = visited.get(vertex);
        while (edge != null) {
            queue.add(edge);
            vertex = edge.getStart();
            edge = visited.get(vertex);
        }
        return queue;
    }

    // Compares two slopes.
    //  - positive if a better than b
    //  - 0 if equal
    //  - negative if a worse than b
    protected abstract int compareSlopes(Slope slopeA, Slope slopeB);

}
