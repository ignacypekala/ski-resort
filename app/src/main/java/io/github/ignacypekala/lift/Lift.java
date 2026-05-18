package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.Clock;

public class Lift extends Edge {
    private Queue queue;
    private int waitTime;
    private int passengerCapacity;
    private EventPublisher eventPublisher;
    private Clock clock;

    public Lift(
            int identifier,
            Vertex start,
            Vertex end,
            int rideTime,
            int waitTime,
            int passengerCapacity,
            EventPublisher eventPublisher,
            Clock clock) {
        super(identifier, start, end, rideTime);

        this.waitTime = waitTime;
        this.passengerCapacity = passengerCapacity;
        queue = new Queue();
        this.eventPublisher = eventPublisher;
        this.clock = clock;

        eventPublisher.send(new LiftStart());
    }

    private void depart() {
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

    private void scheduleLiftDepart() {
        Depart event = new Depart();
        eventPublisher.send(event);
    }

    // Appeal for lifts is defined as the maximum appeal of the slopes outgoing from
    // the end vertex.
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

    class Arrival extends RelativeEvent {
        Carrier carrier;

        public Arrival(Carrier carrier) {
            super(clock, getRideTime());
            this.carrier = carrier;
        }

        public void handle() {
            carrier.arrival();
        }

        Carrier getCarrier() {
            return carrier;
        }

        public String toString() {
            return String.format("%s has arrived", carrier);
        }
    }

    @Override
    public String getRideStartMessage(Skier skier) {
        return skier + " has boarded " + this + ".";
    }

    @Override
    public String getRideFinishMessage(Skier skier) {
        return skier + " has gotten off " + this + ".";
    }

    @Override
    public String toString() {
        return String.format("lift %s", getIdentifier());
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
}
