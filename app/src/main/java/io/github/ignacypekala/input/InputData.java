package io.github.ignacypekala.input;

import io.github.ignacypekala.resort.Resort;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.simulation.SimulationClock;
import io.github.ignacypekala.event.EventQueue;

public class InputData {
    private final Resort resort;
    private final SimulationClock clock;
    private final EventQueue eventQueue;
    private final DelegatingReporter reporter;

    public InputData(final Resort resort, final SimulationClock clock, final EventQueue eventQueue, final DelegatingReporter reporter) {
        this.resort = resort;
        this.clock = clock;
        this.eventQueue = eventQueue;
        this.reporter = reporter;
    }

    public Simulation createSimulation() {
        final Simulation simulation = new Simulation(resort, clock, eventQueue);
        if (reporter != null) {
            reporter.setDelegate(simulation);
        }
        return simulation;
    }

    public Resort getResort() {
        return resort;
    }

    public SimulationClock getClock() {
        return clock;
    }

    public EventQueue getEventQueue() {
        return eventQueue;
    }
}
