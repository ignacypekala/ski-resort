package io.github.ignacypekala;

public class Slope extends Edge {
    private static int nextIdentifier = 0;
    private int difficulty;
    private double baseAppeal;
    private double durability;

    public Slope(
            Vertex start,
            Vertex end,
            int rideTime,
            double durability,
            int difficulty,
            double baseAppeal) {
        super(nextIdentifier++, start, end, rideTime);
        addStartEdge();
        if (durability < 0 || durability > 1) {
            throw new IllegalArgumentException(
                    "Durability must be in the range [0, 1]");
        }
        this.durability = durability;

        if (difficulty < 0 || difficulty > 10) {
            throw new IllegalArgumentException(
                    "Difficulty must be in the range {0, ..., 10}");
        }
        this.difficulty = difficulty;

        if (baseAppeal < 0 || baseAppeal > 1) {
            throw new IllegalArgumentException(
                    "Base appeal must be in the range [0, 1]");
        }
        this.baseAppeal = baseAppeal;
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
                getRideCount());
    }

    @Override
    public double appeal(Skier skier) {
        double diffWeight = skier.getDifficultyWeight();
        double surfWeight = skier.getSurfaceWeight();

        double skillMatch = diffWeight * difficultyAppeal(skier.getProficiency());
        double surfMatch = surfWeight * surfaceAppeal();

        return skillMatch + surfMatch;
    }

    @Override
    public void ride(Skier skier) {
        skier.rideSlope(this);
    }

    @Override
    public void addStartEdge() {
        getStart().addSlope(this);
    }

    @Override
    public String getRideStartMessage(Skier skier) {
        return skier + " has started their run on " + this + ".";
    }

    @Override
    public String getRideFinishMessage(Skier skier) {
        return skier + " has finished their run on " + this + ".";
    }

    @Override
    public String toString() {
        return "slope " + getIdentifier();
    }
}
