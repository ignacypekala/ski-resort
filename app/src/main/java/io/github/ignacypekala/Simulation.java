package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;

public class Simulation implements Clock {
    private final Time endTime = new Time(15, 0, 0);
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

    @Override
    public Time getCurrentTime() { return currentTime; }

    @Override
    public Time getEndTime() { return endTime; }
}
