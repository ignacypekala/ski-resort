package io.github.ignacypekala;

public class Slope extends Edge {
    private final int difficulty;
    private final double baseAppeal;
    private final double wearResistance;

    public Slope(
            final int identifier,
            final Vertex start,
            final Vertex end,
            final int rideTime,
            final double wearResistance,
            final int difficulty,
            final double baseAppeal) {
        super(identifier, start, end, rideTime);
        if (wearResistance < 0 || wearResistance > 1) {
            throw new IllegalArgumentException(
                    "Wear resistance must be in the range [0, 1]");
        }
        this.wearResistance = wearResistance;

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

    public double difficultyAppeal(final int proficiency) {
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
                wearResistance,
                getRideCount());
    }

    @Override
    public double calculateAppeal(final Skier skier) {
        final double difficultyWeight = skier.getDifficultyWeight();
        final double surfaceWeight = skier.getSurfaceWeight();

        final double skillMatch = difficultyWeight * difficultyAppeal(skier.getProficiency());
        final double surfaceMatch = surfaceWeight * surfaceAppeal();

        return skillMatch + surfaceMatch;
    }

    @Override
    public void ride(final Skier skier) {
        skier.rideSlope(this);
    }

    @Override
    public void addStartEdge() {
        getStart().addSlope(this);
    }

    @Override
    public String getRideStartMessage(final Skier skier) {
        return skier + " has started their run on " + this + ".";
    }

    @Override
    public String getRideFinishMessage(final Skier skier) {
        return skier + " has finished their run on " + this + ".";
    }

    @Override
    public String toString() {
        return "slope " + getIdentifier();
    }
}
