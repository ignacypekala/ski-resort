package io.github.ignacypekala;

public class SkierTracked extends Skier {

    public SkierTracked(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight,
        int identifier,
        int startTime
    ) {
        super(
            startPoint,
            proficiency,
            spontaneity,
            difficultyWeight,
            surfaceWeight,
            identifier,
            startTime
        );
    }
}
