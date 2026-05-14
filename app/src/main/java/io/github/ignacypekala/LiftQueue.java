package io.github.ignacypekala;

public class LiftQueue {
    private static final int INITIAL_SIZE = 10;
    private static final int REALLOC_MULTIPLIER = 2;
    private Skier[] queue;
    private int start;
    private int length;

    public LiftQueue() {
        queue = new Skier[INITIAL_SIZE];
        start = 0;
        length = 0;
    }

    private int getStartOffset() {
        return queue.length - start;
    }
    private int getLastIndex() {
        return (start + length - 1) % queue.length;
    }
    private void realloc(int newLength) {
        Skier[] newQueue = new Skier[newLength];

        if (start + length > queue.length) {
            System.arraycopy(
                queue, start,
                newQueue, 0,
                getStartOffset()
            );
            System.arraycopy(
                queue, 0,
                newQueue, getStartOffset(), 
                getLastIndex() + 1
            );
        } else {
            System.arraycopy(queue, start, newQueue, 0, length);
        }

        queue = newQueue;
        start = 0;
    }

    public void push(Skier skier) {
        if (length == queue.length) {
            realloc(length * REALLOC_MULTIPLIER);
        }
        queue[(getLastIndex() + 1) % queue.length] = skier;
        length++;
    }

    public Skier front() {
        if (length == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue[start];
    }

    public void pop() {
        if (length == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        start = (start + 1) % queue.length;
        length--;
    }

    public boolean isEmpty() {
        return length == 0;
    }
}
