package io.github.ignacypekala.skier;

import io.github.ignacypekala.utils.Reporter;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.*;

public class SkierTracker {
    private Reporter reporter;

    public void rideStarted(final Skier skier, final Edge edge) {
        reporter.report(edge.getRideStartMessage(skier));
    }

    public void rideFinished(final Skier skier, final Edge edge) {
        reporter.report(edge.getRideFinishMessage(skier));
    }

    public void liftQueueJoined(final Lift lift) {
        reporter.report(String.format(
                "%s has joined the queue for %s.",
                this,
                lift));
    }
}
