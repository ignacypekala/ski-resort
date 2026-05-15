package io.github.ignacypekala;

public class Lift extends Edge {
    private int waitTime;
    private int passengerCapacity;

    public Lift(
        Vertex start,
        Vertex end,
        int waitTime,
        int passengerCapacity,
        int rideTime
    ) {
        super(start, end, rideTime);
        this.waitTime = waitTime;
        this.passengerCapacity = passengerCapacity;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public double appeal(Skier skier) {
        double maxAppeal = 0;
        Slope[] slopes = getEnd().getSlopes();
        for (int i = 0; i < getEnd().getSlopeCount(); i++) {
            Slope slope = slopes[i];
            double appeal = slope.appeal(skier);
            if (appeal >= maxAppeal) {
                maxAppeal = appeal;
            }
        }
        return maxAppeal;
    }

}
