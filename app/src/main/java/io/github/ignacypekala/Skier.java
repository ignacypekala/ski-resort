package io.github.ignacypekala;

import io.github.ignacypekala.EventQueue.*;
import io.github.ignacypekala.utils.*;
import java.util.Random;

public class Skier {
    private Vertex startPoint;
    private Vertex location;
    private int proficiency;
    private double spontaneity;
    private double difficultyWeight;
    private double surfaceWeight;
    private int identifier;
    private Time startTime;

    private Random generator = new Random();

    private EventPublisher eventPublisher;
    private Clock clock;


    public Skier(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight,
        int identifier,
        Time startTime,
        EventPublisher eventPublisher,
        Clock clock
    ) {
        this.startPoint = startPoint;
        location = startPoint;

        if (proficiency < 0 || proficiency > 10) {
            throw new IllegalArgumentException(
                "Proficiency must be in range {0, ..., 10}"
            );
        }
        this.proficiency = proficiency;

        if (spontaneity < 0 || spontaneity > 1) {
            throw new IllegalArgumentException(
                "Spontaneity must be in range [0, 1]"
            );
        }
        this.spontaneity = spontaneity;

        if (difficultyWeight < 0 || difficultyWeight > 1) {
            throw new IllegalArgumentException(
                "The difficulty appeal weight must be in range [0, 1]"
            );
        }
        this.difficultyWeight = difficultyWeight;

        if (surfaceWeight < 0 || surfaceWeight > 1) {
            throw new IllegalArgumentException(
                "The surface appeal weight must be in range [0, 1]"
            );
        }
        this.surfaceWeight = surfaceWeight;

        this.identifier = identifier;
        this.startTime = startTime;
        
        this.eventPublisher = eventPublisher;
        this.clock = clock;

        scheduleArrival();
    }

    private void scheduleArrival() {
        Arrival event = new Arrival();
        eventPublisher.send(event);
    }

    public void ski() { rideEdge(chooseEdge()); }

    public Edge chooseEdge() {
        Edge[] edges = location.getEdges();
        if (generator.nextDouble() < spontaneity) {
            return edges[generator.nextInt(0, location.getEdgeCount())];
        } else {
            double maxAppeal = -1;
            Edge mostAppealing = null;
            for (Edge edge : edges) {
                double appeal = edge.appeal(this);
                if (appeal > maxAppeal) {
                    maxAppeal = appeal;
                    mostAppealing = edge;
                }
            }
            return mostAppealing;
        }
    }

    public void rideEdge(Edge edge) {
        RideFinished event = new RideFinished(edge, clock);
        eventPublisher.send(event);
        rideStartedHook(edge);
    }

    public void rideFinished(Edge edge) {
        edge.ride();
        rideFinished(edge);
        location = edge.getEnd();
        rideFinishedHook(edge);
        ski();
    }
    // Empty hooks to be overridden by subclasses
    public void rideStartedHook(Edge edge) {}
    private void rideFinishedHook(Edge edge) {}

    private class RideFinished extends RelativeEvent {
        private Edge edge;
        public RideFinished(Edge edge, Clock clock) {
            super(clock, edge.getRideTime());
            this.edge = edge;
        }
        public void handle() { rideFinished(edge); }
    }

    private class Arrival extends Event {
        public Arrival() { super(startTime); }
        public void handle() { ski(); }
    }

    public Vertex getStartPoint() { return startPoint; }
    public int getProficiency() { return proficiency; }
    public double getSpontaneity() { return spontaneity; }
    public double getDifficultyWeight() { return difficultyWeight; }
    public double getSurfaceWeight() { return surfaceWeight; }
    public int getIdentifier() { return identifier; }
    public Time getStartTime() { return startTime; }

}
