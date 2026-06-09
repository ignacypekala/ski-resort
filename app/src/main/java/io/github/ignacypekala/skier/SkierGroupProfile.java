package io.github.ignacypekala.skier;

import io.github.ignacypekala.*;

public record SkierGroupProfile(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight) {
}
