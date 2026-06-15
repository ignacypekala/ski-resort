package io.github.ignacypekala.lift;

import io.github.ignacypekala.skier.*;

import java.util.Arrays;

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

    int getPassengerCount() {
        return passengerCount;
    }

    @Override
    public String toString() {
        return String.format(
                "carrier (%s) with %d passengers",
                lift,
                passengerCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Carrier other)) {
            return false;
        }
        if (passengerCount != other.getPassengerCount()) {
            return false;
        }
        if (!lift.equals(other.getLift())) {
            return false;
        }
        if (!Arrays.equals(passengers, other.getPassengers())) {
            return false;
        }
        return true;
    }
}
