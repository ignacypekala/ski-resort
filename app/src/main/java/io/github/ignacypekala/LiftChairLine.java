package io.github.ignacypekala;

public class LiftChairLine {
    private Skier[] passengers;
    private int passengerCount;
    private Lift lift;
    
    public LiftChairLine(Skier[] passengers, int passengerCount, Lift lift) {
        this.passengers = passengers;
        this.passengerCount = passengerCount;
        this.lift = lift;
    }

    public void depart() {
        for (int i = 0; i < passengerCount; i++) {
            Skier passenger = passengers[i];
            passenger.rideStartedHook(lift);
        }
    }
    public void arrival() {
        for (int i = 0; i < passengerCount; i++) {
            Skier passenger = passengers[i];
            passenger.rideFinished(lift);
        }
    }
}
