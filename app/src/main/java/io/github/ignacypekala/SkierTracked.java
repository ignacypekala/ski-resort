package io.github.ignacypekala;

import io.github.ignacypekala.EventQueue.*;
import io.github.ignacypekala.utils.*;

public class SkierTracked extends Skier {

    public SkierTracked(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight,
        int identifier,
        Time startTime,
        EventPublisher eventPublisher,
        Clock clock
    ) {
        super(
            startPoint,
            proficiency,
            spontaneity,
            difficultyWeight,
            surfaceWeight,
            identifier,
            startTime,
            eventPublisher,
            clock
        );
    }
}
