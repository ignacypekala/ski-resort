package io.github.ignacypekala;

public abstract class GraphElement {
    private int identifier;

    protected GraphElement(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
