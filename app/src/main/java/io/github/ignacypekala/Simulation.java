package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;

import java.util.Scanner;
import java.util.Locale;
import com.google.common.annotations.VisibleForTesting;

public class Simulation implements Clock, Reporter {
    private final Time START_TIME = new Time(9, 0, 0);
    private final Time END_TIME = new Time(15, 0, 0);
    private Time currentTime;
    private EventBroker eventBroker;
    private VertexRegistry vertices;
    private EdgeRegistry edges;

    public Simulation() {
        currentTime = new Time(9, 0, 0);
        eventBroker = new EventQueueList();
        vertices = new VertexRegistry();
        edges = new EdgeRegistry();
    }

    @VisibleForTesting
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

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        stdin.useLocale(Locale.ENGLISH);
        Simulation simulation = new Simulation();
        SimulationLoader loader = new SimulationLoader(simulation);
        loader.load(stdin);
        simulation.run();
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

    protected EdgeRegistry getEdgeRegistry() {
        return edges;
    }

    @Override
    public void report(String message) {
        message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
        System.out.println(String.format("%s: %s", currentTime, message));
    }

    @VisibleForTesting
    EventBroker getEventBroker() {
        return eventBroker;
    }
}
