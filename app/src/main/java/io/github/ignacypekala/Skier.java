package io.github.ignacypekala;

public class Skier {
    private Vertex startPoint;
    private int proficiency;
    private int identifier;
    private int startTime;
    private double spontaneity;

    public Skier(
        Vertex startPoint,
        int proficiency,
        int identifier,
        int startTime,
        double spontaneity,
        boolean spectated
    ) {
        this.startPoint = startPoint;
        this.proficiency = proficiency;
        this.identifier = identifier;
        this.startTime = startTime;
        this.spontaneity = spontaneity;
    }

}
