package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.resort.Resort;

public class Simulation implements Reporter {
    private final Resort resort;
    private final SimulationClock clock;
    private final Broker eventBroker;

    public Simulation(final Resort resort, final SimulationClock clock, final EventQueue eventQueue) {
        this.resort = resort;
        this.clock = clock;
        this.eventBroker = eventQueue;
    }

    public Simulation() {
        this(
            new Resort(new Vertex[0], new Slope[0], new io.github.ignacypekala.lift.Lift[0]),
            new SimulationClock(),
            new EventQueue()
        );
    }

    public void tick() {
        final Event event = eventBroker.poll();
        clock.setTime(event.getTime());
        event.handle();
    }

    public void run() {
        while (eventBroker.hasEvents()) {
            tick();
        }
    }

    public void printSummary() {
        for (final Edge lift : resort.getLifts()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    lift,
                    lift.getRideCount()));
        }
        for (final Edge slope : resort.getSlopes()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    slope,
                    slope.getRideCount()));
        }
    }

    @Override
    public void report(String message) {
        message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
        System.out.println(String.format("%s: %s", clock.getTime(), message));
    }

    public Clock getClock() {
        return clock;
    }

    public Broker getEventBroker() {
        return eventBroker;
    }

    public SimulationContext getContext() {
        return new SimulationContext(clock, eventBroker);
    }
}
