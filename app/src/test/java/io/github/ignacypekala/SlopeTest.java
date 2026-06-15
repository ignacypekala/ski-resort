package io.github.ignacypekala;

import org.junit.jupiter.api.Test;
import io.github.ignacypekala.skier.Skier;

import io.github.ignacypekala.utils.*;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.simulation.Simulation;
import io.github.ignacypekala.simulation.SimulationContext;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class SlopeTest {
    private Vertex a;
    private Vertex b;
    private SkierGroupProfile groupProfile;
    private Simulation simulation;
    private Time startTime;
    private SimulationContext simulationContext;
    private Skier skier;

    @BeforeEach
    void initializeEnvironment() {
        a = new Vertex(0, 0, new Coordinates(0, 0));
        b = new Vertex(0, 0, new Coordinates(0, 0));
        groupProfile = new SkierGroupProfile(a, 0, 0.0, 0.0, 0.0);
        simulation = new Simulation();
        startTime = simulation.getClock().getStartTime();
        simulationContext = simulation.getContext();
        skier = new Skier(
            0,
            groupProfile,
            simulation.getClock().getStartTime(),
            simulation.getContext()
        );
    }

    @Test
    void constructor() {
        assertEquals(0, a.getSlopeCount());
        Slope slope = new Slope(0, a, b, 10, 1.0, 5, 1.0);
        slope.addStartEdge();
        assertEquals(1, a.getSlopeCount());
        assertEquals(slope, a.getSlopes()[0]);
    }

    @Test
    void rideTime() {
        Slope slope = new Slope(0, a, b, 420, 0, 0, 0);
        assertEquals(420, slope.getRideTime(), 0.000001);
    }

    @Test
    void badInput() {
        Class<IllegalArgumentException> badArg = IllegalArgumentException.class;
        assertThrows(badArg, () -> new Slope(0, a, b, 0, -1, 0, 0));
        assertThrows(badArg, () -> new Slope(0, a, b, 0, 2, 0, 0));
        assertThrows(badArg, () -> new Slope(0, a, b, 0, 0, -1, 0));
        assertThrows(badArg, () -> new Slope(0, a, b, 0, 0, 11, 0));
        assertThrows(badArg, () -> new Slope(0, a, b, 0, 0, 0, -1));
        assertThrows(badArg, () -> new Slope(0, a, b, 0, 0, 0, 2));
    }

    @Test
    void difficultyAppealVeryHard() {
        Slope slope = new Slope(0, a, b, 0, 0, 5, 0);
        assertEquals(0, slope.difficultyAppeal(0));
    }

    @Test
    void difficultyAppealHard() {
        Slope slopeA = new Slope(0, a, b, 0, 0, 3, 0);
        assertEquals(3.0 / 5.0, slopeA.difficultyAppeal(1), 0.000001);

        Slope slopeB = new Slope(0, a, b, 0, 0, 5, 0);
        assertEquals(1.0 / 5.0, slopeB.difficultyAppeal(1), 0.000001);

        Slope slopeC = new Slope(0, a, b, 0, 0, 1, 0);
        assertEquals(1, slopeC.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealEasy() {
        Slope slope = new Slope(0, a, b, 0, 0, 0, 0);
        assertEquals(1.0 - 1.0 / 7.0, slope.difficultyAppeal(1), 0.000001);
    }

    @Test
    void difficultyAppealVeryEasy() {
        Slope slope = new Slope(0, a, b, 0, 0, 0, 0);
        assertEquals(0.2, slope.difficultyAppeal(6), 0.000001);
    }

    @Test
    void surfaceInvulnerable() {
        Slope slope = new Slope(0, a, b, 0, 1, 0, 0);
        assertEquals(1.0, slope.surfaceAppeal());

        slope.ride(skier);
        assertEquals(1.0, slope.surfaceAppeal());
        for (int i = 0; i < 25; i++) {
            slope.ride(skier);
        }

        assertEquals(1.0, slope.surfaceAppeal());
    }

    @Test
    void surfaceVulnerable() {
        Slope slope = new Slope(0, a, b, 0, 0.5, 0, 0.75);
        assertEquals(0.75 + 0.25 * 1, slope.surfaceAppeal());

        slope.rideStarted();
        assertEquals(0.75 + 0.25 * 0.5, slope.surfaceAppeal());

        slope.rideStarted();
        assertEquals(0.75 + 0.25 * 0.5 * 0.5, slope.surfaceAppeal());
    }

    @Test
    void accumulativeAppeal() {
        Slope surfaceSlope = new Slope(0, a, b, 0, 1, 10, 1);
        SkierGroupProfile surfaceSkierGroup = new SkierGroupProfile(a, 0, 0, 0, 1);
        Skier surfaceSkier = new Skier(0, surfaceSkierGroup, startTime, simulationContext);
        assertEquals(1.0, surfaceSlope.calculateAppeal(surfaceSkier));

        Slope difficultySlope = new Slope(0, a, b, 0, 0, 10, 0);
        SkierGroupProfile proficiencyGroup = new SkierGroupProfile(a, 10, 0.0, 1.0, 0.0);
        Skier proficientSkier = new Skier(1, proficiencyGroup, startTime, simulationContext);
        assertEquals(1.0, difficultySlope.calculateAppeal(proficientSkier));

        Slope allRoundSlope = new Slope(0, a, b, 0, 0.5, 5, 0.5);

        SkierGroupProfile allRoundGroup = new SkierGroupProfile(a, 5, 0, 0.5, 0.5);
        Skier allRoundSkier = new Skier(2, allRoundGroup, startTime, simulationContext);
        assertEquals(1.0, allRoundSlope.calculateAppeal(allRoundSkier));
    }
}
