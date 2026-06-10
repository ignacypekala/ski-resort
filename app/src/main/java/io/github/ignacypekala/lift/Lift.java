package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.Clock;

public class Lift extends Edge {
    private final LiftQueue queue;
    private final int departureInterval;
    private final int passengerCapacity;
    private final Publisher eventPublisher;
    private final Clock clock;

    public Lift(
            final int identifier,
            final Vertex start,
            final Vertex end,
            final int rideTime,
            final int departureInterval,
            final int passengerCapacity,
            final Publisher eventPublisher,
            final Clock clock) {
        super(identifier, start, end, rideTime);

        this.departureInterval = departureInterval;
        this.passengerCapacity = passengerCapacity;
        queue = new LiftQueue();
        this.eventPublisher = eventPublisher;
        this.clock = clock;

        eventPublisher.publish(new LiftStart(this, clock));
    }

    public void depart() {
        int i = 0;
        final Skier[] passengers = new Skier[passengerCapacity];
        while (i < passengerCapacity && !queue.empty()) {
            passengers[i++] = queue.peek();
            queue.dequeue();
        }
        final Carrier carrier = new Carrier(passengers, i, this);
        final Arrival event = new Arrival(carrier);
        eventPublisher.publish(event);
        carrier.depart();
        scheduleLiftDepart();
    }

    private void scheduleLiftDepart() {
        final Departure event = new Departure(clock, this);
        eventPublisher.publish(event);
    }

    // Appeal for lifts is defined as the maximum appeal of the slopes outgoing from
    // the end vertex.
    @Override
    public double calculateAppeal(final Skier skier) {
        double maxAppeal = 0;
        final Slope[] slopes = getEnd().getSlopes();
        for (int i = 0; i < getEnd().getSlopeCount(); i++) {
            final Slope slope = slopes[i];
            final double appeal = slope.calculateAppeal(skier);
            if (appeal > maxAppeal) {
                maxAppeal = appeal;
            }
        }
        return maxAppeal;
    }

    @Override
    public void ride(final Skier skier) {
        queue.enqueue(skier);
        skier.liftQueueJoinedHook(this);
    }

    class Arrival extends RelativeEvent {
        Carrier carrier;

        public Arrival(final Carrier carrier) {
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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Arrival other)) {
                return false;
            }
            if (!carrier.equals(other.getCarrier())) {
                return false;
            }
            return super.equals(obj);
        }
    }

    @Override
    public String getRideStartMessage(final Skier skier) {
        return skier + " has boarded " + this + ".";
    }

    @Override
    public String getRideFinishMessage(final Skier skier) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } if (obj == null) {
            return false;
        } if (getClass() != obj.getClass()) {
            return false;
        }
        Lift other = (Lift) obj;
        if (queue == null) {
            if (other.queue != null) {
                return false;
            }
        } else if (!queue.equals(other.queue)) {
            return false;
        } if (departureInterval != other.departureInterval) {
            return false;
        } if (passengerCapacity != other.passengerCapacity) {
            return false;
        }
        return true;
    }

    public int getDepartureInterval() {
        return departureInterval;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

}
