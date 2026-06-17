package io.github.ignacypekala.input;

import java.util.Scanner;
import java.util.Locale;

import io.github.ignacypekala.*;
import io.github.ignacypekala.resort.Resort;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;
import io.github.ignacypekala.simulation.*;

public class InputLoader {
    private final Scanner scanner;
    private int nextSkierIdentifier;

    public InputLoader(final Scanner scanner) {
        this.scanner = scanner;
    }

    public InputData load() {
        nextSkierIdentifier = 0;
        final SimulationClock clock = new SimulationClock();
        final EventQueue eventQueue = new EventQueue();
        final SimulationContext context = new SimulationContext(clock, eventQueue);
        final DelegatingReporter reporter = new DelegatingReporter();

        final int vertexCount = Integer.parseInt(getLine(scanner));
        final Vertex[] vertices = new Vertex[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = loadVertex(getLine(scanner), i);
        }

        final int liftCount = Integer.parseInt(getLine(scanner));
        final Lift[] lifts = new Lift[liftCount];
        for (int i = 0; i < liftCount; i++) {
            lifts[i] = loadLift(getLine(scanner), i, vertices, context);
        }

        final int slopeCount = Integer.parseInt(getLine(scanner));
        final Slope[] slopes = new Slope[slopeCount];
        for (int i = 0; i < slopeCount; i++) {
            slopes[i] = loadSlope(scanner.nextLine(), i, vertices);
        }

        final int skierGroupCount = Integer.parseInt(getLine(scanner));
        for (int i = 0; i < skierGroupCount; i++) {
            loadSkierGroup(scanner, vertices, context, reporter);
        }

        final Resort resort = new Resort(vertices, slopes, lifts);
        return new InputData(resort, clock, eventQueue, reporter);
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

    Lift loadLift(final String line, final int identifier, final Vertex[] vertices, final SimulationContext context) {
        final Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        final Vertex start = vertices[lineScanner.nextInt()];
        final Vertex end = vertices[lineScanner.nextInt()];
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
                context);
        lift.addStartEdge();
        return lift;
    }

    Slope loadSlope(final String line, final int identifier, final Vertex[] vertices) {
        final Scanner lineScanner = new Scanner(line);
        lineScanner.useLocale(Locale.ENGLISH);

        final Vertex start = vertices[lineScanner.nextInt()];
        final Vertex end = vertices[lineScanner.nextInt()];
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
        final Scanner timeScanner = new Scanner(timeString);
        timeScanner.useLocale(Locale.ENGLISH);
        timeScanner.useDelimiter(":");

        final int hours = timeScanner.nextInt();
        final int minutes = timeScanner.nextInt();
        final int seconds = timeScanner.nextInt();

        timeScanner.close();
        return new Time(hours, minutes, seconds);
    }

    Skier[] loadSkierGroup(final Scanner scanner, final Vertex[] vertices, final SimulationContext context, final Reporter reporter) {
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

        final Vertex startPoint = vertices[line.nextInt()];
        final Time firstStartTime = loadTime(line.next());

        int interval = 0;
        if (skierCount > 1) {
            interval = line.nextInt();
        }

        final Skier[] skiers = new Skier[skierCount];

        final SkierGroupProfile groupProfile = new SkierGroupProfile(
                startPoint,
                proficiency,
                spontaneity,
                difficultyWeight,
                surfaceWeight);

        Time startTime = firstStartTime;
        for (int i = 0; i < skierCount; i++) {
            final int skierIdentifier = nextSkierIdentifier++;
            if (tracked) {
                skiers[i] = new TrackedSkier(
                        skierIdentifier,
                        groupProfile,
                        startTime,
                        context,
                        reporter);
            } else {
                skiers[i] = new Skier(
                        skierIdentifier,
                        groupProfile,
                        startTime,
                        context);
            }
            startTime = Time.secondsLater(startTime, interval);
        }
        line.close();
        return skiers;
    }

    private String getLine(final Scanner scanner) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            line = scanner.nextLine().trim();
        }
        return line;
    }
}
