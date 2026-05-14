package io.github.ignacypekala;

public class Skier {
    private Vertex startPoint;
    private int proficiency;
    private double spontaneity;
    private double difficultyWeight;
    private double surfaceWeight;
    private int identifier;
    private int startTime;

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

}
