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
        this.durability = durability;
        this.difficulty = difficulty;
        this.rideTime = rideTime;
        this.baseAppeal = baseAppeal;
    }
}
