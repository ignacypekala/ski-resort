package io.github.ignacypekala.skier;

import io.github.ignacypekala.utils.Reporter;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.*;

public class SkierTracker implements SkierListener {
    private final Reporter reporter;

    public SkierTracker(final Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void onRideStarted(final Skier skier, final Edge edge) {
        reporter.report(edge.getRideStartMessage(skier));
    }

    @Override
    public void onRideFinished(final Skier skier, final Edge edge) {
        reporter.report(edge.getRideFinishMessage(skier));
    }

    @Override
    public void onLiftQueueJoined(final Skier skier, final Lift lift) {
        reporter.report(String.format(
                "%s has joined the queue for %s.",
                skier,
                lift));
    }
}
