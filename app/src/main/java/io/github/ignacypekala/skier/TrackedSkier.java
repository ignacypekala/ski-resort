package io.github.ignacypekala.skier;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.simulation.*;

public class TrackedSkier extends Skier {
    private Reporter reporter;

    public TrackedSkier(
            int identifier,
            Vertex startPoint,
            int proficiency,
            double spontaneity,
            double difficultyWeight,
            double surfaceWeight,
            Time startTime,
            Publisher eventPublisher,
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
