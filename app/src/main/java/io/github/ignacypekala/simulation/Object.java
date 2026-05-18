package io.github.ignacypekala.simulation;

public abstract class Object {
    private int identifier;

    protected Object(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
