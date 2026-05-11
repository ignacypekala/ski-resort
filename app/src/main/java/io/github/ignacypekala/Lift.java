package io.github.ignacypekala;

public class Lift extends Edge {
    private int waitTime;
    private int passengerCapacity;
    private int rideTime;

    public Lift(
        Vertex start,
        Vertex end,
        int waitTime,
        int passengerCapacity,
        int rideTime
    ) {
        super(start, end);
        this.waitTime = waitTime;
        this.passengerCapacity = passengerCapacity;
        this.rideTime = rideTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public int getRideTime() {
        return rideTime;
    }

}
