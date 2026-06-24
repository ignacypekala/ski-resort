package io.github.ignacypekala.lift;

import java.util.ArrayDeque;

import io.github.ignacypekala.skier.*;

public class LiftQueue {
    private static final int INITIAL_SIZE = 10;
    private ArrayDeque<Skier> queue;

    public LiftQueue() {
        queue = new ArrayDeque<>(INITIAL_SIZE);
    }

    public void enqueue(final Skier skier) {
        queue.addLast(skier);
    }

    public Skier peek() {
        return queue.getFirst();
    }

    public void dequeue() {
        queue.removeFirst();
    }

    public boolean empty() {
        return queue.isEmpty();
    }
}
