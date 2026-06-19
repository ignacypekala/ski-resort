package io.github.ignacypekala.skier;

import io.github.ignacypekala.Edge;
import io.github.ignacypekala.lift.Lift;

public interface SkierListener {
    void onRideStarted(Skier skier, Edge edge);

    void onRideFinished(Skier skier, Edge edge);

    void onLiftQueueJoined(Skier skier, Lift lift);
}
