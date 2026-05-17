package io.github.ignacypekala.lift;

import com.google.common.annotations.VisibleForTesting;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.utils.*;

public class Lift extends Edge {
    private static int nextIdentifier = 0;
    private LiftQueue queue;
    private int waitTime;
    private int passengerCapacity;
    private EventPublisher eventPublisher;
    private Clock clock;

    public Lift(
            Vertex start,
            Vertex end,
            int rideTime,
            int waitTime,
            int passengerCapacity,
            EventPublisher eventPublisher,
            Clock clock) {
        super(nextIdentifier++, start, end, rideTime);
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
        Carrier carrier = new Carrier(passengers, i, this);
        Arrival event = new Arrival(carrier);
        eventPublisher.send(event);
        carrier.depart();
        if (clock.getEndTime().compareTo(clock.getCurrentTime()) > 0) {
            scheduleLiftDepart();
        }
    }

    public void scheduleLiftDepart() {
        Depart event = new Depart();
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
        skier.liftQueueJoinedHook(this);
    }

    @VisibleForTesting
    class Depart extends RelativeEvent {
        public Depart() {
            super(clock, waitTime);
        }

        public void handle() {
            depart();
        }

        public String toString() {
            return String.format("%s has departed", Lift.this);
        }
    }

    @VisibleForTesting
    class LiftStart extends Event {
        public LiftStart() {
            super(clock.getStartTime());
        }

        public void handle() {
            depart();
        }

        public String toString() {
            return String.format("%s has started", Lift.this);
        }
    }

    @VisibleForTesting
    class Arrival extends RelativeEvent {
        Carrier carrier;

        public Arrival(Carrier carrier) {
            super(clock, getRideTime());
            this.carrier = carrier;
        }

        public void handle() {
            carrier.arrival();
        }

        @VisibleForTesting
        Carrier getCarrier() {
            return carrier;
        }

        public String toString() {
            return String.format("%s has arrived", carrier);
        }
    }

    @Override
    public void addStartEdge() {
        getStart().addLift(this);
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public String toString() {
        return String.format("lift %s", getIdentifier());
    }

    @Override
    public String getRideStartMessage(Skier skier) {
        return skier + " has boarded " + this + ".";
    }

    @Override
    public String getRideFinishMessage(Skier skier) {
        return skier + " has gotten off " + this + ".";
    }

}
