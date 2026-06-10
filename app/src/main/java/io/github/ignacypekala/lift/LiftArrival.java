package io.github.ignacypekala.lift;

import io.github.ignacypekala.event.RelativeEvent;
import io.github.ignacypekala.simulation.Clock;

class LiftArrival extends RelativeEvent {
    private final Carrier carrier;

    public LiftArrival(final Carrier carrier, final Clock clock) {
        super(clock, carrier.getLift().getRideTime());
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
        if (!(obj instanceof LiftArrival other)) {
            return false;
        }
        if (!carrier.equals(other.getCarrier())) {
            return false;
        }
        return super.equals(obj);
    }
}
