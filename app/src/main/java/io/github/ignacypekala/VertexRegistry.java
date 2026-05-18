package io.github.ignacypekala;

import java.util.NoSuchElementException;

public class VertexRegistry {
    private Vertex[] vertices;

    public void initialize(int size) {
        vertices = new Vertex[size];
    }

    public void resize(int size) {
        Vertex[] newVertices = new Vertex[size];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        vertices = newVertices;
    }

    public void register(Vertex vertex) {
        int identifier = vertex.getIdentifier();
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

    public Vertex fetch(int identifier) {
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
        Vertex vertex = vertices[identifier];
        if (vertex == null) {
            throw new NoSuchElementException(
                    "There is no vertex with this identifier in the registry.");
        }
        return vertex;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}

