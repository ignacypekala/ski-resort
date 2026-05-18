package io.github.ignacypekala;

import java.util.Locale;
import java.util.Scanner;

import com.google.common.annotations.VisibleForTesting;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;

public class SimulationLoader {
    private VertexRegistry vertexRegistry;
    private EdgeRegistry edgeRegistry;
    private Clock clock;
    private EventPublisher publisher;
    private Reporter reporter;
    private int nextSkierIdentifier;

    public SimulationLoader(Simulation simulation) {
        vertexRegistry = simulation.getVertexRegistry();
        edgeRegistry = simulation.getEdgeRegistry();
        clock = simulation;
        publisher = simulation.getEventBroker();
        reporter = simulation;
    }

    public void load(Scanner scanner) {
        nextSkierIdentifier = 0;
        int vertexCount = scanner.nextInt();
        vertexRegistry.initialize(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            vertexRegistry.register(loadVertex(scanner, i));
        }

        int liftCount = scanner.nextInt();
        edgeRegistry.initialize(liftCount);
        for (int i = 0; i < liftCount; i++) {
            edgeRegistry.register(loadLift(scanner, i));
        }

        int slopeCount = scanner.nextInt();
        edgeRegistry.resize(slopeCount);
        for (int i = 0; i < slopeCount; i++) {
            loadSlope(scanner, i);
        }

        for (int i = 0; i < scanner.nextInt(); i++) {
            loadSkierGroup(scanner);
        }
    }

    @VisibleForTesting
    Vertex loadVertex(Scanner scanner, int identifier) {
        Scanner line = new Scanner(scanner.nextLine());
        line.useLocale(Locale.ENGLISH);

        int altitude = line.nextInt();
        Coordinates position = new Coordinates(
                line.nextInt(),
                line.nextInt());

        boolean accessible = line.hasNext() && line.next().charAt(0) == 's';

        Vertex vertex;
        if (accessible) {
            vertex = new VertexAccessible(identifier, altitude, position);
        } else {
            vertex = new Vertex(identifier, altitude, position);
        }

        line.close();
        return vertex;
    }

    @VisibleForTesting
    Lift loadLift(Scanner scanner, int identifier) {
        Vertex start = vertexRegistry.fetch(scanner.nextInt());
        Vertex end = vertexRegistry.fetch(scanner.nextInt());
        int waitTime = scanner.nextInt();
        int passengerCapacity = scanner.nextInt();
        int rideTime = scanner.nextInt();
        return new Lift(
                identifier,
                start,
                end,
                rideTime,
                waitTime,
                passengerCapacity,
                publisher,
                clock);
    }

    @VisibleForTesting
    Slope loadSlope(Scanner scanner, int identifier) {
        Vertex start = vertexRegistry.fetch(scanner.nextInt());
        Vertex end = vertexRegistry.fetch(scanner.nextInt());
        int difficulty = scanner.nextInt();
        int rideTime = scanner.nextInt();
        double baseAppeal = scanner.nextDouble();
        double durability = scanner.nextDouble();
        return new Slope(
                identifier,
                start,
                end,
                rideTime,
                durability,
                difficulty,
                baseAppeal);
    }

    @VisibleForTesting
    Time loadTime(Scanner scanner) {
        Scanner timeScanner = new Scanner(scanner.next());
        timeScanner.useLocale(Locale.ENGLISH);
        timeScanner.useDelimiter(":");

        int hours = timeScanner.nextInt();
        int minutes = timeScanner.nextInt();
        int seconds = timeScanner.nextInt();
        timeScanner.close();
        return new Time(hours, minutes, seconds);
    }

    @VisibleForTesting
    Skier[] loadSkierGroup(Scanner scanner) {
        Scanner line = new Scanner(scanner.nextLine());
        line.useLocale(Locale.ENGLISH);

        int skierCount = line.nextInt();
        int proficiency = line.nextInt();
        double spontaneity = line.nextDouble();
        boolean tracked = line.hasNext() && line.next().charAt(0) == 's';

        line.close();
        line = new Scanner(scanner.nextLine());
        line.useLocale(Locale.ENGLISH);

        double difficultyWeight = line.nextDouble();
        double surfaceWeight = line.nextDouble();

        line.close();
        line = new Scanner(scanner.nextLine());
        line.useLocale(Locale.ENGLISH);

        Vertex startPoint = vertexRegistry.fetch(line.nextInt());

        Time firstStartTime = loadTime(line);

        int interval = 0;
        if (skierCount > 1) {
            interval = line.nextInt();
        }

        Skier[] skiers = new Skier[skierCount];

        Time startTime = firstStartTime;
        for (int i = 0; i < skierCount; i++) {
            int skierIdentifier = nextSkierIdentifier++;
            if (tracked) {
                skiers[i] = new SkierTracked(
                        skierIdentifier,
                        startPoint,
                        proficiency,
                        spontaneity,
                        difficultyWeight,
                        surfaceWeight,
                        startTime,
                        publisher,
                        clock,
                        reporter);

            } else {
                skiers[i] = new Skier(
                        skierIdentifier,
                        startPoint,
                        proficiency,
                        spontaneity,
                        difficultyWeight,
                        surfaceWeight,
                        startTime,
                        publisher,
                        clock);
            }
            startTime = Time.secondsLater(startTime, interval);
        }
        line.close();
        return skiers;
    }
}
