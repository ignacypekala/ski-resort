package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;

public class Carrier {
    private final Skier[] passengers;
    private final int passengerCount;
    private final Lift lift;

    public Carrier(final Skier[] passengers, final int passengerCount, final Lift lift) {
        this.passengers = passengers;
        this.passengerCount = passengerCount;
        this.lift = lift;
    }

    public void depart() {
        for (int i = 0; i < passengerCount; i++) {
            final Skier passenger = passengers[i];
            passenger.rideStarted(lift);
        }
    }

    public void arrival() {
        for (int i = 0; i < passengerCount; i++) {
            final Skier passenger = passengers[i];
            passenger.rideFinished(lift);
        }
    }

    Lift getLift() {
        return lift;
    }

    Skier[] getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return String.format(
                "carrier (%s) with %d passengers",
                lift,
                passengerCount);
    }
}
