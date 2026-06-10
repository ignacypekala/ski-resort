package io.github.ignacypekala.simulation;

public abstract class SimulationObject {
    private final int identifier;

    protected SimulationObject(final int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
