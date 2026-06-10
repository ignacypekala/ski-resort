package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;

import java.util.Scanner;
import java.util.Locale;

public class Simulation implements Reporter {
    private final SimulationClock clock;
    private final Broker eventBroker;
    private final VertexRegistry vertices;
    private final EdgeRegistry lifts;
    private final EdgeRegistry slopes;

    public Simulation() {
        clock = new SimulationClock();
        eventBroker = new EventQueue();
        vertices = new VertexRegistry();
        lifts = new EdgeRegistry();
        slopes = new EdgeRegistry();
    }

    void tick() {
        final Event event = eventBroker.poll();
        clock.setTime(event.getTime());
        event.handle();
    }

    public void run() {
        while (eventBroker.hasEvents()) {
            tick();
        }
    }

    void printSummary() {
        for (final Edge lift : lifts.getEdges()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    lift,
                    lift.getRideCount()));
        }
        for (final Edge slope : slopes.getEdges()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    slope,
                    slope.getRideCount()));
        }
    }

    public static void main(final String[] args) {
        final Scanner stdin = new Scanner(System.in);
        stdin.useLocale(Locale.ENGLISH);

        final Simulation simulation = new Simulation();
        final Loader loader = new Loader(simulation);
        loader.load(stdin);

        simulation.run();
        simulation.printSummary();

        stdin.close();
    }

    public Clock getClock() {
        return clock;
    }

    protected VertexRegistry getVertexRegistry() {
        return vertices;
    }

    protected EdgeRegistry getLiftRegistry() {
        return lifts;
    }

    protected EdgeRegistry getSlopeRegistry() {
        return slopes;
    }

    @Override
    public void report(String message) {
        message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
        System.out.println(String.format("%s: %s", clock.getTime(), message));
    }

    Broker getEventBroker() {
        return eventBroker;
    }
}
