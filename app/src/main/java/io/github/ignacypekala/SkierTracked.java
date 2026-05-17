package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.utils.*;

public class SkierTracked extends Skier {
    private Reporter reporter;

    public SkierTracked(
        Vertex startPoint,
        int proficiency,
        double spontaneity,
        double difficultyWeight,
        double surfaceWeight,
        int identifier,
        Time startTime,
        EventPublisher eventPublisher,
        Clock clock,
        Reporter reporter
    ) {
        super(
            startPoint,
            proficiency,
            spontaneity,
            difficultyWeight,
            surfaceWeight,
            identifier,
            startTime,
            eventPublisher,
            clock
        );
        this.reporter = reporter;
    }

    @Override
    public void rideStartedHook(Edge edge) {
        reporter.report(
            String.format("%s has started a ride on %s.", this, edge)
        );
    }
    @Override
    public void rideFinishedHook(Edge edge) {
        reporter.report(
            String.format("%s has finished a ride on.", this, edge)
        );
    }
    @Override
    public void liftQueueJoinedHook(Lift lift) {
        reporter.report(
            String.format("%s has ")
        );
    }

}
