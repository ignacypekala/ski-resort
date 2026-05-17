package io.github.ignacypekala.lift;

import com.google.common.annotations.VisibleForTesting;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;

public class Lift extends Edge {
    private LiftQueue queue;
    private int waitTime;
    private int passengerCapacity;

    private EventPublisher eventPublisher;
    private Clock clock;

    public Lift(
        Vertex start,
        Vertex end,
        int waitTime,
        int passengerCapacity,
        int rideTime,
        EventPublisher eventPublisher,
        Clock clock
    ) {
        super(start, end, rideTime);
        addStartEdge();
        this.waitTime = waitTime;
        this.passengerCapacity = passengerCapacity;
        queue = new LiftQueue();
        this.eventPublisher = eventPublisher;
        this.clock = clock;

        eventPublisher.send(new LiftStart());
    }

    public void depart() {
        int i = 0;
        Skier[] passengers = new Skier[passengerCapacity];
        while (i < passengerCapacity && !queue.empty()) {
            passengers[i++] = queue.peek();
            queue.dequeue();
        }
        LiftChairLine chairLine = new LiftChairLine(passengers, i, this);
        ChairLineArrival event = new ChairLineArrival(chairLine);
        eventPublisher.send(event);
        chairLine.depart();
        if (clock.getEndTime().compareTo(clock.getCurrentTime()) > 0 ) {
            scheduleLiftDepart();
        }
    }

    public void scheduleLiftDepart() {
        LiftDepart event = new LiftDepart();
        eventPublisher.send(event);
    }

    @Override
    public double appeal(Skier skier) {
        double maxAppeal = 0;
        Slope[] slopes = getEnd().getSlopes();
        for (int i = 0; i < getEnd().getSlopeCount(); i++) {
            Slope slope = slopes[i];
            double appeal = slope.appeal(skier);
            if (appeal > maxAppeal) {
                maxAppeal = appeal;
            }
        }
        return maxAppeal;
    }

    @Override 
    public void ride(Skier skier) {
        queue.enqueue(skier);
    }

    @VisibleForTesting
    class LiftDepart extends RelativeEvent {
        public LiftDepart() { super(clock, waitTime); }
        public void handle() { depart(); }
    }

    @VisibleForTesting
    class LiftStart extends Event {
        public LiftStart() { super(clock.getStartTime()); }
        public void handle() { depart(); }
    }

    @VisibleForTesting
    class ChairLineArrival extends RelativeEvent {
        LiftChairLine chairLine;
        public ChairLineArrival(LiftChairLine chairLine) {
            super(clock, getRideTime());
            this.chairLine = chairLine;
        }
        public void handle() { chairLine.arrival(); }

        @VisibleForTesting
        LiftChairLine getChairLine() { return chairLine; }
    }

    @Override
    public void addStartEdge() {
        getStart().addLift(this);
    }

    public int getWaitTime() { return waitTime; }
    public int getPassengerCapacity() { return passengerCapacity; }

}
