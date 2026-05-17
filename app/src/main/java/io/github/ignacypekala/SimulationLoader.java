package io.github.ignacypekala;

import java.util.Locale;
import java.util.Scanner;
import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.lift.*;

public class SimulationLoader {
    private Clock clock;
    private EventPublisher publisher;
    private Reporter reporter;
    private Vertex[] vertices;
    
    public SimulationLoader(
            Clock clock,
            EventPublisher publisher,
            Reporter reporter
    ) {
        this.clock = clock;
        this.publisher = publisher;
        this.reporter = reporter;
    }

    public void load(Scanner scanner) {
        int vertexCount = scanner.nextInt();
        vertices = new Vertex[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = loadVertex(scanner);
        }

        for (int i = 0; i < scanner.nextInt(); i++) {
            loadLift(scanner);
        }

        for (int i = 0; i < scanner.nextInt(); i++) {
            loadSlope(scanner);
        }


    }

    private Vertex loadVertex(String line) {
        Scanner scanner = new Scanner(line);
        scanner.useLocale(Locale.ENGLISH);

        int altitude = scanner.nextInt();
        Coordinates position = new Coordinates(
            scanner.nextInt(),
            scanner.nextInt()
        );

        boolean accessible = scanner.hasNext() && scanner.next().charAt(0) == 's';

        Vertex vertex;
        if (accessible) {
            vertex = new VertexAccessible(altitude, position);
        } else {
            vertex = new Vertex(altitude, position);
        }

        scanner.close();
        return vertex;
    }

    private void loadLift(Scanner scanner) {
        Vertex start = vertices[scanner.nextInt()];
        Vertex end = vertices[scanner.nextInt()];
        int waitTime = scanner.nextInt(); 
        int passengerCapacity = scanner.nextInt(); 
        int rideTime = scanner.nextInt();
        new Lift(
            start,
            end,
            rideTime,
            waitTime,
            passengerCapacity,
            publisher,
            clock
        );
    }

    private void loadSlope(Scanner scanner) {
        Vertex start = vertices[scanner.nextInt()];
        Vertex end = vertices[scanner.nextInt()];
        int difficulty = scanner.nextInt();
        int rideTime = scanner.nextInt();
        double baseAppeal = scanner.nextDouble();
        double durability = scanner.nextDouble();
        new Slope(
            start,
            end,
            rideTime,
            durability,
            difficulty,
            baseAppeal
        );
    }

    private void loadSkierGroup(Scanner scanner) {
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
        Vertex start = vertices[line.nextInt()];
        
        // TODO Add time parsing and delay inclusion

        int interval = 0;
        if (skierCount > 1) {
            interval = line.nextInt();
        }

        for (int i = 0; i < skierCount; i++) {
            new Skier(start, proficiency, spontaneity, difficultyWeight, surfaceWeight );
        }

    }
}
