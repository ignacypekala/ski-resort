package io.github.ignacypekala.lift;

import java.util.ArrayDeque;

import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.simulation.*;

public class LiftQueue {
    private static final int INITIAL_SIZE = 10;

    private final Clock clock;

    private ArrayDeque<Skier> queue = new ArrayDeque<>(INITIAL_SIZE);
    private int maxSize = 0;
    private int weightedSize = 0;
    private Time lastModification;

    public LiftQueue(Clock clock) {
        this.clock = clock;
        this.lastModification = clock.getStartTime();
    }

    public void enqueue(final Skier skier) {
        updateWeightedSize();
        queue.addLast(skier);
        if (maxSize < queue.size()) {
            maxSize++;
        }
    }

    public Skier peek() {
        return queue.getFirst();
    }

    public void dequeue() {
        updateWeightedSize();
        queue.removeFirst();
    }

    private void updateWeightedSize() {
        Time currentTime = clock.getTime();
        int seconds = Time.secondsBetween(currentTime, lastModification);
        weightedSize += queue.size() * seconds;
        lastModification = currentTime;
    }

    public boolean empty() {
        return queue.isEmpty();
    }

    public double averageSize() {
        updateWeightedSize();
        int totalSeconds = Time.secondsBetween(clock.getTime(), clock.getStartTime());
        return (double) weightedSize / totalSeconds;
    }

    public int maxSize() {
        return maxSize;
    }
}
