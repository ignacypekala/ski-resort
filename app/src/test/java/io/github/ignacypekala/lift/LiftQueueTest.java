package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiftQueueTest {
    @Test
    void popEmpty() {
        LiftQueue queue = new LiftQueue();
        assertThrows(IllegalStateException.class, () -> queue.dequeue());
    }

    @Test
    void simple() {
        LiftQueue queue = new LiftQueue();
        Skier[] skiers = new Skier[5];
        for (int i = 0; i < 5; i++) {
            skiers[i] = new TestClass.TestSkier(i, 0, 0, 0);
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
    }

    @Test
    void arbitrary() {
        LiftQueue queue = new LiftQueue();

        Skier[] sportsmen = new Skier[42];
        int j = 0;
        for (int i = 0; i < 42; i++) {
            Skier sportsman = new TestClass.TestSkier(i, 0, 0, 0);
            sportsmen[i] = sportsman;
            queue.enqueue(sportsman);
            assertSame(queue.peek(), sportsmen[j]);
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
