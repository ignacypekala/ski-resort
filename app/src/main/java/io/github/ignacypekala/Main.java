package io.github.ignacypekala;

import io.github.ignacypekala.input.InputData;
import io.github.ignacypekala.input.InputLoader;
import io.github.ignacypekala.simulation.Simulation;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        final Scanner stdin = new Scanner(System.in);
        stdin.useLocale(Locale.ENGLISH);

        final InputLoader loader = new InputLoader(stdin);
        final InputData inputData = loader.load();

        final Simulation simulation = inputData.createSimulation();
        simulation.run();
        simulation.printSummary();

        stdin.close();
    }
}
