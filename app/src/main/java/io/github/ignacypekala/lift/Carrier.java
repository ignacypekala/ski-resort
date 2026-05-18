package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;

import com.google.common.annotations.VisibleForTesting;

public class Carrier {
    private Skier[] passengers;
    private int passengerCount;
    private Lift lift;

    public Carrier(Skier[] passengers, int passengerCount, Lift lift) {
        this.passengers = passengers;
        this.passengerCount = passengerCount;
        this.lift = lift;
    }

    public void depart() {
        for (int i = 0; i < passengerCount; i++) {
            Skier passenger = passengers[i];
            passenger.rideStarted(lift);
        }
    }

    public void arrival() {
        for (int i = 0; i < passengerCount; i++) {
            Skier passenger = passengers[i];
            passenger.rideFinished(lift);
        }
    }

    @VisibleForTesting
    Lift getLift() {
        return lift;
    }

    @VisibleForTesting
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
