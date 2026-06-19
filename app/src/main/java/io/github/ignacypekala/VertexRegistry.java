package io.github.ignacypekala;

import java.util.NoSuchElementException;

public class VertexRegistry {
    private Vertex[] vertices;

    public void initialize(final int size) {
        vertices = new Vertex[size];
    }

    public void register(final Vertex vertex) {
        final int identifier = vertex.getIdentifier();
        if (vertices == null) {
            throw new IllegalStateException(
                    "Cannot register vertices to a an uninitialized registry");
        }
        if (identifier < 0) {
            throw new IllegalArgumentException(
                    "Identifiers cannot be negative.");
        }
        if (identifier > vertices.length) {
            throw new IllegalStateException(
                    "Not enough space in the vertex registry.");
        }
        vertices[vertex.getIdentifier()] = vertex;
    }

    public Vertex fetch(final int identifier) {
        if (vertices == null) {
            throw new IllegalStateException(
                    "Cannot fetch from an uninitialized registry");
        }
        if (identifier < 0) {
            throw new IllegalArgumentException(
                    "Identifiers cannot be negative.");
        } 
        if (identifier > vertices.length) {
            throw new NoSuchElementException(
                    "There is no vertex with an identifier this high in the registry.");
        }
        final Vertex vertex = vertices[identifier];
        if (vertex == null) {
            throw new NoSuchElementException(
                    "There is no vertex with this identifier in the registry.");
        }
        return vertex;
    }
}
