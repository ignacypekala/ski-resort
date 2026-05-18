package io.github.ignacypekala.simulation;

public abstract class SimulationObject {
    private int identifier;

    protected SimulationObject(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
