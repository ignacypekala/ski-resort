package io.github.ignacypekala.simulation;

import java.util.Locale;
import java.util.Scanner;

import io.github.ignacypekala.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;

public class Loader {
    private final VertexRegistry vertices;
    private final EdgeRegistry lifts;
    private final EdgeRegistry slopes;
    private final Clock clock;
    private final Publisher publisher;
    private final Reporter reporter;
    private int nextSkierIdentifier;

    public Loader(final Simulation simulation) {
        vertices = simulation.getVertexRegistry();
        lifts = simulation.getLiftRegistry();
        slopes = simulation.getSlopeRegistry();
        clock = simulation.getClock();
        publisher = simulation.getEventBroker();
        reporter = simulation;
    }

    public void load(final Scanner scanner) {
        nextSkierIdentifier = 0;
        final int vertexCount = Integer.parseInt(getLine(scanner));
        vertices.initialize(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            vertices.register(loadVertex(getLine(scanner), i));
        }

        final int liftCount = Integer.parseInt(getLine(scanner));
        lifts.initialize(liftCount);
        for (int i = 0; i < liftCount; i++) {
            lifts.register(loadLift(getLine(scanner), i));
        }

        final int slopeCount = Integer.parseInt(getLine(scanner));
        slopes.initialize(slopeCount);
        for (int i = 0; i < slopeCount; i++) {
            slopes.register(loadSlope(scanner.nextLine(), i));
        }

        final int skierGroupCount = Integer.parseInt(getLine(scanner));
        for (int i = 0; i < skierGroupCount; i++) {
            loadSkierGroup(scanner);
        }
    }

    Vertex loadVertex(final String line, final int identifier) {
        final Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        final int altitude = lineScanner.nextInt();
        final Coordinates position = new Coordinates(
                lineScanner.nextInt(),
                lineScanner.nextInt());

        final boolean accessible = lineScanner.hasNext() && lineScanner.next().charAt(0) == 's';

        Vertex vertex;
        if (accessible) {
            vertex = new VertexAccessible(identifier, altitude, position);
        } else {
            vertex = new Vertex(identifier, altitude, position);
        }

        lineScanner.close();
        return vertex;
    }

    Lift loadLift(final String line, final int identifier) {
        final Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        final Vertex start = vertices.fetch(lineScanner.nextInt());
        final Vertex end = vertices.fetch(lineScanner.nextInt());
        final int departureInterval = lineScanner.nextInt();
        final int passengerCapacity = lineScanner.nextInt();
        final int rideTime = lineScanner.nextInt();

        lineScanner.close();
        final Lift lift = new Lift(
                identifier,
                start,
                end,
                rideTime,
                departureInterval,
                passengerCapacity,
                publisher,
                clock);
        lift.addStartEdge();
        return lift;
    }

    Slope loadSlope(final String line, final int identifier) {
        final Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        final Vertex start = vertices.fetch(lineScanner.nextInt());
        final Vertex end = vertices.fetch(lineScanner.nextInt());
        final int difficulty = lineScanner.nextInt();
        final int rideTime = lineScanner.nextInt();
        final double baseAppeal = lineScanner.nextDouble();
        final double wearResistance = lineScanner.nextDouble();

        lineScanner.close();
        final Slope slope = new Slope(
                identifier,
                start,
                end,
                rideTime,
                wearResistance,
                difficulty,
                baseAppeal);
        slope.addStartEdge();
        return slope;
    }

    Time loadTime(final String timeString) {
        final Scanner scanner = new Scanner(timeString);
        scanner.useLocale(Locale.ENGLISH);
        scanner.useDelimiter(":");

        final int hours = scanner.nextInt();
        final int minutes = scanner.nextInt();
        final int seconds = scanner.nextInt();

        scanner.close();
        return new Time(hours, minutes, seconds);
    }

    Skier[] loadSkierGroup(final Scanner scanner) {
        Scanner line = new Scanner(getLine(scanner));
        line.useLocale(Locale.ENGLISH);

        final int skierCount = line.nextInt();
        final int proficiency = line.nextInt();
        final double spontaneity = line.nextDouble();
        final boolean tracked = line.hasNext() && line.next().charAt(0) == 's';

        line.close();
        line = new Scanner(getLine(scanner));
        line.useLocale(Locale.ENGLISH);

        final double difficultyWeight = line.nextDouble();
        final double surfaceWeight = line.nextDouble();

        line.close();
        line = new Scanner(getLine(scanner));
        line.useLocale(Locale.ENGLISH);

        final Vertex startPoint = vertices.fetch(line.nextInt());
        final Time firstStartTime = loadTime(line.next());

        int interval = 0;
        if (skierCount > 1) {
            interval = line.nextInt();
        }

        final Skier[] skiers = new Skier[skierCount];

        Time startTime = firstStartTime;
        for (int i = 0; i < skierCount; i++) {
            final int skierIdentifier = nextSkierIdentifier++;
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

    private String getLine(final Scanner scanner) {
        String line = scanner.nextLine().trim();
        if (line == "") {
            line = scanner.nextLine().trim();
        }
        return line;
    }
}
