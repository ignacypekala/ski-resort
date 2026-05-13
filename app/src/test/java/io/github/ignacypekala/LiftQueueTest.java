package io.github.ignacypekala;

import org.junit.jupiter.api.Test;

import io.github.ignacypekala.utils.Coordinates;

import static org.junit.jupiter.api.Assertions.*;

public class LiftQueueTest {
    @Test
    void test() {
        LiftQueue queue = new LiftQueue();
        assertNotNull(queue, "Queue shouldn't be null.");
        assertTrue(queue.isEmpty(), "Queue should be empty.");

        Skier[] sportsmen = new Skier[42];
        int j = 0;
        for (int i = 0; i < 42; i++) {
            Skier sportsman = new Skier(
                new Vertex(0, new Coordinates(0, 0), 0),
                0, 0, 0, 0.1 
            );
            sportsmen[i] = sportsman;
            queue.push(sportsman);
            assertSame(queue.front(), sportsmen[j]);
            if (i % 3 == 0) {
                queue.pop();
                j++;
            }
        }
        while(j < 42) {
            assertFalse(queue.isEmpty());
            queue.pop();
            j++;
        }
        assertTrue(queue.isEmpty(), "Queue should be empty.");
    }
}
