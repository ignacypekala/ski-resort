package io.github.ignacypekala;

import io.github.ignacypekala.utils.*;

public class Simulation implements Clock {
    private Time time;
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public Time getCurrentTime() {
        return time;
    }
}
