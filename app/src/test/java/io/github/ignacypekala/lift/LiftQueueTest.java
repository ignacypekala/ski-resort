package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;
import io.github.ignacypekala.simulation.SimulationClock;
import io.github.ignacypekala.simulation.SimulationContext;
import io.github.ignacypekala.skier.*;
import io.github.ignacypekala.utils.Coordinates;
import io.github.ignacypekala.utils.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

public class LiftQueueTest {
    private SimulationContext context;
    private SimulationClock clock;
    private Time startTime;
    private static SkierGroupProfile groupProfile = new SkierGroupProfile(
                new Vertex(0, 0, new Coordinates(0, 0)), 0, 0, 0, 0);

    @BeforeEach
    void initializeEnvironment() {
        clock = new SimulationClock();
        context = new SimulationContext(clock, event -> {});
        startTime = clock.getStartTime();
    }

    @Test
    void popEmptyShouldThrow() {
        LiftQueue queue = new LiftQueue(clock);
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @Test
    void simple() {
        LiftQueue queue = new LiftQueue(clock);
        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            skiers[i] = new LocalSkier(
                    i,
                    groupProfile,
                    context.clock().getStartTime(),
                    context);
        }

        assertTrue(queue.empty());
        queue.enqueue(skiers[0]);
        assertFalse(queue.empty());
        queue.enqueue(skiers[1]);

        assertSame(skiers[0], queue.peek());
        assertDoesNotThrow(() -> queue.dequeue());
        assertFalse(queue.empty());

        queue.enqueue(skiers[2]);
        queue.enqueue(skiers[3]);
        queue.enqueue(skiers[4]);

        assertSame(skiers[1], queue.peek());
        assertDoesNotThrow(() -> queue.dequeue());

        assertSame(skiers[2], queue.peek());
        assertDoesNotThrow(() -> queue.dequeue());

        assertFalse(queue.empty());
        assertDoesNotThrow(() -> queue.dequeue());
        assertDoesNotThrow(() -> queue.dequeue());
        assertTrue(queue.empty());

        assertEquals(4, queue.maxSize());
    }

    @Test
    void averageSize() {
        LiftQueue queue = new LiftQueue(clock);
        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            skiers[i] = new LocalSkier(i, groupProfile, startTime, context);
        }

        queue.enqueue(skiers[0]);

        clock.setTime(Time.secondsLater(startTime, 10));
        queue.enqueue(skiers[1]);

        clock.setTime(Time.secondsLater(startTime, 30));
        queue.dequeue();

        clock.setTime(Time.secondsLater(startTime, 60));

        assertEquals(80.0 / 60.0, queue.averageSize(), 1e-9);
    }

    @Test
    void arbitrary() {
        LiftQueue queue = new LiftQueue(clock);
        assertNotNull(queue, "Queue shouldn't be null.");
        assertTrue(queue.empty(), "Queue should be empty.");

        Skier[] skiers = new Skier[42];
        int j = 0;
        SkierGroupProfile groupProfile = new SkierGroupProfile(
                new Vertex(0, 0, new Coordinates(0, 0)), 0, 0, 0, 0);
        for (int i = 0; i < 42; i++) {
            Skier skeir = new LocalSkier(i, groupProfile, startTime, context);
            skiers[i] = skeir;
            queue.enqueue(skeir);
            assertSame(queue.peek(), skiers[j]);
            if (i % 3 == 0) {
                assertDoesNotThrow(() -> queue.dequeue());
                j++;
            }
        }
        while (j < 42) {
            assertFalse(queue.empty());
            assertDoesNotThrow(() -> queue.dequeue());
            j++;
        }
        assertTrue(queue.empty(), "Queue should be empty.");
    }
}
