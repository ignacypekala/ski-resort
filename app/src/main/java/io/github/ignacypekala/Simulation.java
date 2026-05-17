package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;

import com.google.common.annotations.VisibleForTesting;

import io.github.ignacypekala.event.*;

public class Simulation implements Clock {
    private final Time START_TIME = new Time(9, 0, 0);
    private final Time END_TIME = new Time(15, 0, 0);
    private Time currentTime;
    private EventBroker eventBroker;

    public Simulation() {
        currentTime = new Time(9, 0, 0);
        eventBroker = new EventQueueList();
    }

    private void tick() {
        Event event = eventBroker.poll();
        currentTime = event.getTime();
        event.handle();
    }

    public void run() {
        while (eventBroker.hasEvents()) {
            tick();
        }
    }

    public static void main(String[] args) {}

    @Override public Time getCurrentTime() { return currentTime; }
    @Override public Time getStartTime() { return START_TIME; }
    @Override public Time getEndTime() { return END_TIME; }

    @VisibleForTesting EventBroker getEventBroker() { return eventBroker; }
}
