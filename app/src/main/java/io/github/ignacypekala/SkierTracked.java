package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.utils.*;

public class SkierTracked extends Skier {
    private Reporter reporter;

    public SkierTracked(
            int identifier,
            Vertex startPoint,
            int proficiency,
            double spontaneity,
            double difficultyWeight,
            double surfaceWeight,
            Time startTime,
            EventPublisher eventPublisher,
            Clock clock,
            Reporter reporter) {
        super(
                identifier,
                startPoint,
                proficiency,
                spontaneity,
                difficultyWeight,
                surfaceWeight,
                startTime,
                eventPublisher,
                clock);
        this.reporter = reporter;
    }

    @Override
    public void rideStartedHook(Edge edge) {
        reporter.report(edge.getRideStartMessage(this));
    }

    @Override
    public void rideFinishedHook(Edge edge) {
        reporter.report(edge.getRideFinishMessage(this));
    }

    @Override
    public void liftQueueJoinedHook(Lift lift) {
        reporter.report(String.format(
                "%s has joined the queue for %s.",
                this,
                lift));
    }

}
