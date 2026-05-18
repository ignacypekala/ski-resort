package io.github.ignacypekala.simulation;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;

import java.util.Scanner;
import java.util.Locale;

public class Simulation implements Clock, Reporter {
    private final Time START_TIME = new Time(9, 0, 0);
    private final Time END_TIME = new Time(15, 0, 0);
    private Time currentTime;
    private EventBroker eventBroker;
    private VertexRegistry vertices;
    private EdgeRegistry lifts;
    private EdgeRegistry slopes;

    public Simulation() {
        currentTime = new Time(9, 0, 0);
        eventBroker = new EventQueueList();
        vertices = new VertexRegistry();
        lifts = new EdgeRegistry();
        slopes = new EdgeRegistry();
    }

    void tick() {
        Event event = eventBroker.poll();
        currentTime = event.getTime();
        event.handle();
    }

    public void run() {
        while (eventBroker.hasEvents()) {
            tick();
        }
    }

    void printRecap() {
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
        simulation.printRecap();

        stdin.close();
    }

    @Override
    public Time getCurrentTime() {
        return currentTime;
    }

    @Override
    public Time getStartTime() {
        return START_TIME;
    }

    @Override
    public Time getEndTime() {
        return END_TIME;
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
        System.out.println(String.format("%s: %s", currentTime, message));
    }

    EventBroker getEventBroker() {
        return eventBroker;
    }
}
