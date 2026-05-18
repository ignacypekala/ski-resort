package io.github.ignacypekala;

import java.util.NoSuchElementException;

public class EdgeRegistry {
    private Edge[] edges;

    public void initialize(int size) {
        edges = new Edge[size];
    }

    public void resize(int size) {
        Edge[] newEdges = new Edge[size];
        System.arraycopy(edges, 0, newEdges, 0, edges.length);
        edges = newEdges;
    }

    public void register(Edge edge) {
        int identifier = edge.getIdentifier();
        if (identifier < 0) {
            throw new IllegalArgumentException(
                    "Identifiers cannot be negative.");
        } else if (identifier > edges.length) {
            throw new IllegalStateException(
                    "Not enough space in the edge registry.");
        }
        edges[edge.getIdentifier()] = edge;
    }

    public Edge fetch(int identifier) {
        if (identifier < 0) {
            throw new IllegalArgumentException(
                    "Identifiers cannot be negative.");
        } else if (identifier > edges.length) {
            throw new NoSuchElementException(
                    "There is no edge with an identifier this high in the registry.");
        }
        Edge edge = edges[identifier];
        if (edge == null) {
            throw new NoSuchElementException(
                    "There is no edge with this identifier in the registry."
            );
        }
        return edge;
    }

    public Edge[] getEdges() {
        return edges;
    }
}

