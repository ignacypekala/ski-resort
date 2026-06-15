package io.github.ignacypekala.skier;

import io.github.ignacypekala.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.simulation.*;

public class TrackedSkier extends Skier {
    private final Reporter reporter;

    public TrackedSkier(
            final int identifier,
            final Vertex startPoint,
            final int proficiency,
            final double spontaneity,
            final double difficultyWeight,
            final double surfaceWeight,
            final Time startTime,
            final SimulationContext simulationContext,
            final Reporter reporter) {
        super(
                identifier,
                startPoint,
                proficiency,
                spontaneity,
                difficultyWeight,
                surfaceWeight,
                startTime,
                simulationContext);
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
