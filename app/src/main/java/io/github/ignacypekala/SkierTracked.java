package io.github.ignacypekala;

import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.Lift;
import io.github.ignacypekala.simulation.Clock;
import io.github.ignacypekala.utils.*;

public class SkierTracked extends Skier {
    private final Reporter reporter;

    public SkierTracked(
            final int identifier,
            final Vertex startPoint,
            final int proficiency,
            final double spontaneity,
            final double difficultyWeight,
            final double surfaceWeight,
            final Time startTime,
            final Publisher eventPublisher,
            final Clock clock,
            final Reporter reporter) {
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
    public void rideStartedHook(final Edge edge) {
        reporter.report(edge.getRideStartMessage(this));
    }

    @Override
    public void rideFinishedHook(final Edge edge) {
        reporter.report(edge.getRideFinishMessage(this));
    }

    @Override
    public void liftQueueJoinedHook(final Lift lift) {
        reporter.report(String.format(
                "%s has joined the queue for %s.",
                this,
                lift));
    }

}
