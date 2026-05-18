package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;

import java.util.Scanner;
import java.util.Locale;

public class Simulation implements Reporter {
    private SimulationClock clock;
    private Broker eventBroker;
    private VertexRegistry vertices;
    private EdgeRegistry lifts;
    private EdgeRegistry slopes;

    public Simulation() {
        clock = new SimulationClock();
        eventBroker = new EventQueue();
        vertices = new VertexRegistry();
        lifts = new EdgeRegistry();
        slopes = new EdgeRegistry();
    }

    void tick() {
        Event event = eventBroker.poll();
        clock.setTime(event.getTime());
        event.handle();
    }

    public void run() {
        while (eventBroker.hasEvents()) {
            tick();
        }
    }

    void printStatistics() {
        for (Edge lift : lifts.getEdges()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    lift,
                    lift.getRideCount()));
        }
        for (Edge slope : slopes.getEdges()) {
            System.out.println(String.format(
                    "%s: %d rides",
                    slope,
                    slope.getRideCount()));
        }
    }

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        stdin.useLocale(Locale.ENGLISH);

        Simulation simulation = new Simulation();
        Loader loader = new Loader(simulation);
        loader.load(stdin);

        simulation.run();
        simulation.printStatistics();

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
