package io.github.ignacypekala;

import java.util.Locale;
import java.util.Scanner;

import com.google.common.annotations.VisibleForTesting;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;

public class SimulationLoader {
    private VertexRegistry vertices;
    private EdgeRegistry lifts;
    private EdgeRegistry slopes;
    private Clock clock;
    private EventPublisher publisher;
    private Reporter reporter;
    private int nextSkierIdentifier;

    public SimulationLoader(Simulation simulation) {
        vertices = simulation.getVertexRegistry();
        lifts = simulation.getLiftRegistry();
        slopes = simulation.getSlopeRegistry();
        clock = simulation;
        publisher = simulation.getEventBroker();
        reporter = simulation;
    }

    public void load(Scanner scanner) {
        nextSkierIdentifier = 0;
        int vertexCount = Integer.parseInt(scanner.nextLine().trim());
        vertices.initialize(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            vertices.register(loadVertex(scanner.nextLine(), i));
        }

        scanner.nextLine(); // Consume empty line
        int liftCount = Integer.parseInt(scanner.nextLine().trim());
        lifts.initialize(liftCount);
        for (int i = 0; i < liftCount; i++) {
            lifts.register(loadLift(scanner.nextLine(), i));
        }

        scanner.nextLine(); // Consume empty line
        int slopeCount = Integer.parseInt(scanner.nextLine().trim());
        slopes.initialize(slopeCount);
        for (int i = 0; i < slopeCount; i++) {
            slopes.register(loadSlope(scanner.nextLine(), i));
        }

        scanner.nextLine(); // Consume empty line
        int skierGroupCount = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < skierGroupCount; i++) {
            loadSkierGroup(scanner);
        }
    }

    @VisibleForTesting
    Vertex loadVertex(String line, int identifier) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        int altitude = lineScanner.nextInt();
        Coordinates position = new Coordinates(
                lineScanner.nextInt(),
                lineScanner.nextInt());

        boolean accessible = lineScanner.hasNext() && lineScanner.next().charAt(0) == 's';

        Vertex vertex;
        if (accessible) {
            vertex = new VertexAccessible(identifier, altitude, position);
        } else {
            vertex = new Vertex(identifier, altitude, position);
        }

        lineScanner.close();
        return vertex;
    }

    @VisibleForTesting
    Lift loadLift(String line, int identifier) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        Vertex start = vertices.fetch(lineScanner.nextInt());
        Vertex end = vertices.fetch(lineScanner.nextInt());
        int waitTime = lineScanner.nextInt();
        int passengerCapacity = lineScanner.nextInt();
        int rideTime = lineScanner.nextInt();

        lineScanner.close();
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
    Slope loadSlope(String line, int identifier) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        Vertex start = vertices.fetch(lineScanner.nextInt());
        Vertex end = vertices.fetch(lineScanner.nextInt());
        int difficulty = lineScanner.nextInt();
        int rideTime = lineScanner.nextInt();
        double baseAppeal = lineScanner.nextDouble();
        double durability = lineScanner.nextDouble();

        lineScanner.close();
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
    Time loadTime(String timeString) {
        Scanner scanner = new Scanner(timeString);
        scanner.useLocale(Locale.ENGLISH);
        scanner.useDelimiter(":");

        int hours = scanner.nextInt();
        int minutes = scanner.nextInt();
        int seconds = scanner.nextInt();

        scanner.close();
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

        Vertex startPoint = vertices.fetch(line.nextInt());

        Time firstStartTime = loadTime(line.next());

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
