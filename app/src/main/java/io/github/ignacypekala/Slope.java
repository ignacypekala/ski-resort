package io.github.ignacypekala;

public class Slope extends Edge {
    private int difficulty;
    private int rideTime;
    private double baseAppeal;
    private double durability;

    public Slope(
        Vertex start,
        Vertex end,
        double durability,
        int difficulty,
        int rideTime,
        double baseAppeal
    ) {
        super(start, end);
        if (durability < 0 || durability > 1) {
            throw new IllegalArgumentException(
                "Durability must be in the range [0, 1]"
            );
        }
        this.durability = durability;
        if (difficulty < 0 || difficulty > 10) {
            throw new IllegalArgumentException(
                "Difficulty must be in the range {0, ..., 10}"
            );
        }
        this.difficulty = difficulty;
        this.rideTime = rideTime;
        if (baseAppeal < 0 || baseAppeal > 1) {
            throw new IllegalArgumentException(
                "Base appeal must be in the range [0, 1]"
            );
        }
        this.baseAppeal = baseAppeal;
    }

    public int getRideTime() {
        return rideTime;
    }

    public double difficultyAppeal(int proficiency) {
        if (difficulty >= proficiency + 5) {
            return 0;
        } else if (proficiency + 5 > difficulty && difficulty >= proficiency) {
            return 1.0 - (difficulty - proficiency) / 5.0;
        } else {
            return Math.max(0.2, 1.0 - (proficiency - difficulty) / 7.0);
        }
    }

    public double surfaceAppeal() {
        return baseAppeal + (1.0 - baseAppeal) * Math.pow(
            durability,
            getRideCount()
        );
    }

    @Override
    public double appeal(Skier skier) {
        double weightedSkillMatch = skier.getDifficultyWeight() * difficultyAppeal( skier.getProficiency());
        double weightedSurfaceCondition = skier.getSurfaceWeight() * surfaceAppeal();
        return weightedSkillMatch + weightedSurfaceCondition;
    }

}
