package io.github.ignacypekala.lift;

import io.github.ignacypekala.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiftQueueTest {
    @Test
    void popEmpty() {
        Queue queue = new Queue();
        assertThrows(IllegalStateException.class, () -> queue.dequeue());
    }

    @Test
    void arbitrary() {
        Queue queue = new Queue();
        assertNotNull(queue, "Queue shouldn't be null.");
        assertTrue(queue.empty(), "Queue should be empty.");

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
