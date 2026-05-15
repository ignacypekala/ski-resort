package io.github.ignacypekala;

import java.util.Random;

public class Skier {
    private Vertex startPoint;
    private Vertex location;
    private int proficiency;
    private double spontaneity;
    private double difficultyWeight;
    private double surfaceWeight;
    private int identifier;
    private int startTime;

    private Random generator = new Random();

    public Skier(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight,
        int identifier,
        int startTime
    ) {
        this.startPoint = startPoint;
        location = startPoint;

        if (proficiency < 0 || proficiency > 10) {
            throw new IllegalArgumentException(
                "Proficiency must be in range [0, 10]"
            );
        }
        this.proficiency = proficiency;

        if (spontaneity < 0 || spontaneity > 1) {
            throw new IllegalArgumentException(
                "Spontaneity must be in range [0, 1]"
            );
        }
        this.spontaneity = spontaneity;

        if (difficultyWeight < 0 || difficultyWeight > 1) {
            throw new IllegalArgumentException(
                "The difficulty appeal weight must be in range [0, 1]"
            );
        }
        this.difficultyWeight = difficultyWeight;

        if (surfaceWeight < 0 || surfaceWeight > 1) {
            throw new IllegalArgumentException(
                "The surface appeal weight must be in range [0, 1]"
            );
        }
        this.surfaceWeight = surfaceWeight;

        this.identifier = identifier;
        this.startTime = startTime;
    }

    public Vertex getStartPoint() {
        return startPoint;
    }

    public int getProficiency() {
        return proficiency;
    }

    public double getSpontaneity() {
        return spontaneity;
    }

    public double getDifficultyWeight() {
        return difficultyWeight;
    }

    public double getSurfaceWeight() {
        return surfaceWeight;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getStartTime() {
        return startTime;
    }

    public Edge decide() {
        Edge[] edges = location.getEdges();
        if (generator.nextDouble() < spontaneity) {
            return edges[generator.nextInt(0, location.getEdgeCount())];
        } else {
            double maxAppeal = -1;
            Edge mostAppealing = null;
            for (Edge edge : edges) {
                double appeal = edge.appeal(this);
                if (appeal > maxAppeal) {
                    maxAppeal = appeal;
                    mostAppealing = edge;
                }
            }
            return mostAppealing;
        }
    }

}
