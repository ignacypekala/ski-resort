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

    public SimulationLoader(Simulation simulation) {
        vertexRegistry = simulation.getVertexRegistry();
        edgeRegistry = simulation.getEdgeRegistry();
        clock = simulation;
        publisher = simulation.getEventBroker();
        reporter = simulation;
    }

    public void load(Scanner scanner) {
        int vertexCount = scanner.nextInt();
        vertexRegistry.initialize(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            vertexRegistry.register(loadVertex(scanner));
        }

        int liftCount = scanner.nextInt();
        edgeRegistry.initialize(liftCount);
        for (int i = 0; i < liftCount; i++) {
            edgeRegistry.register(loadLift(scanner));
        }

        int slopeCount = scanner.nextInt();
        edgeRegistry.resize(slopeCount);
        for (int i = 0; i < slopeCount; i++) {
            loadSlope(scanner);
        }

        for (int i = 0; i < scanner.nextInt(); i++) {
            loadSkierGroup(scanner);
        }
    }

    @VisibleForTesting
    Vertex loadVertex(Scanner scanner) {
        Scanner line = new Scanner(scanner.nextLine());
        line.useLocale(Locale.ENGLISH);

        int altitude = line.nextInt();
        Coordinates position = new Coordinates(
                line.nextInt(),
                line.nextInt());

        boolean accessible = line.hasNext() && line.next().charAt(0) == 's';

        Vertex vertex;
        if (accessible) {
            vertex = new VertexAccessible(altitude, position);
        } else {
            vertex = new Vertex(altitude, position);
        }

        line.close();
        return vertex;
    }

    @VisibleForTesting
    Lift loadLift(Scanner scanner) {
        Vertex start = vertexRegistry.fetch(scanner.nextInt());
        Vertex end = vertexRegistry.fetch(scanner.nextInt());
        int waitTime = scanner.nextInt();
        int passengerCapacity = scanner.nextInt();
        int rideTime = scanner.nextInt();
        return new Lift(
                start,
                end,
                rideTime,
                waitTime,
                passengerCapacity,
                publisher,
                clock);
    }

    @VisibleForTesting
    Slope loadSlope(Scanner scanner) {
        Vertex start = vertexRegistry.fetch(scanner.nextInt());
        Vertex end = vertexRegistry.fetch(scanner.nextInt());
        int difficulty = scanner.nextInt();
        int rideTime = scanner.nextInt();
        double baseAppeal = scanner.nextDouble();
        double durability = scanner.nextDouble();
        return new Slope(
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

        int skierCount = line.nextInt();
        int proficiency = line.nextInt();
        double spontaneity = line.nextDouble();
        boolean tracked = line.hasNext() && line.next().charAt(0) == 's';

        line.close();
        line = new Scanner(scanner.nextLine());
        double difficultyWeight = line.nextDouble();
        double surfaceWeight = line.nextDouble();

        line.close();
        line = new Scanner(scanner.nextLine());
        Vertex startPoint = vertexRegistry.fetch(line.nextInt());

        Time firstStartTime = loadTime(scanner);

        int interval = 0;
        if (skierCount > 1) {
            interval = line.nextInt();
        }

        Skier[] skiers = new Skier[skierCount];

        Time previousStartTime = firstStartTime;
        for (int i = 0; i < skierCount; i++) {
            Time currentStartTime = Time.secondsLater(previousStartTime, interval);
            if (tracked) {
                skiers[i] = new SkierTracked(
                        startPoint,
                        proficiency,
                        spontaneity,
                        difficultyWeight,
                        surfaceWeight,
                        currentStartTime,
                        publisher,
                        clock,
                        reporter);

            } else {
                skiers[i] = new Skier(
                        startPoint,
                        proficiency,
                        spontaneity,
                        difficultyWeight,
                        surfaceWeight,
                        currentStartTime,
                        publisher,
                        clock);
            }
            previousStartTime = currentStartTime;
        }
        line.close();
        return skiers;
    }
}
