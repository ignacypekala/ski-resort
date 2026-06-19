package io.github.ignacypekala.skier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import io.github.ignacypekala.*;
import io.github.ignacypekala.event.*;
import io.github.ignacypekala.simulation.*;
import io.github.ignacypekala.utils.*;

public class SkierTest {
    private Vertex startVertex;
    private Vertex endVertex;
    private Time startTime;
    private TestClock clock;
    private TestPublisher publisher;
    private SimulationContext context;

    @BeforeEach
    void setUp() {
        startVertex = new Vertex(1, 100, new Coordinates(0, 0));
        endVertex = new Vertex(2, 50, new Coordinates(10, 10));
        startTime = new Time(9, 0, 0);
        clock = new TestClock(startTime);
        publisher = new TestPublisher();
        context = new SimulationContext(clock, publisher);
    }

    @Test
    void constructor() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.6, 0.7);
        Skier skier = new TestSkier(42, profile, startTime, context);

        assertEquals(42, skier.getIdentifier());
        assertEquals(startVertex, skier.getStartPoint());
        assertEquals(startVertex, skier.getLocation());
        assertEquals(5, skier.getProficiency());
        assertEquals(0.5, skier.getSpontaneity());
        assertEquals(0.6, skier.getDifficultyWeight());
        assertEquals(0.7, skier.getSurfaceWeight());
        assertEquals(startTime, skier.getStartTime());
    }

    @Test
    void schedulesDayStart() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        new TestSkier(1, profile, startTime, context);

        assertEquals(1, publisher.publishedEvents.size());
        Event event = publisher.publishedEvents.get(0);
        assertTrue(event instanceof DayStart);
        assertEquals(startTime, event.getTime());
    }

    @Test
    void rideStart() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        TestSkier skier = new TestSkier(1, profile, startTime, context);
        Slope slope = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 1.0);

        skier.rideStarted(slope);

        assertNull(skier.getLocation());
        assertEquals(slope, skier.lastStartedHookEdge);
        assertEquals(1, slope.getRideCount());
    }

    @Test
    void rideFinish() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        TestSkier skier = new TestSkier(1, profile, startTime, context);
        Slope slope = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 1.0);
        skier.rideStarted(slope);

        clock.setTimeUp(true);
        skier.rideFinished(slope);

        assertEquals(endVertex, skier.getLocation());
        assertEquals(slope, skier.lastFinishedHookEdge);
    }

    @Test
    void rideFinishContinuesRiding() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        TestSkier skier = new TestSkier(1, profile, startTime, context);
        Slope slope = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 1.0);
        Slope nextSlope = new Slope(102, endVertex, startVertex, 60, 1.0, 5, 1.0);

        skier.setNextChosenEdge(nextSlope);
        clock.setTimeUp(false);

        skier.rideFinished(slope);

        assertEquals(1, nextSlope.getRideCount());
        assertNull(skier.getLocation());
    }

    @Test
    void rideFinishStopsWhenTimeIsUp() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        TestSkier skier = new TestSkier(1, profile, startTime, context);
        Slope slope = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 1.0);
        Slope nextSlope = new Slope(102, endVertex, startVertex, 60, 1.0, 5, 1.0);

        skier.setNextChosenEdge(nextSlope);
        clock.setTimeUp(true);

        skier.rideFinished(slope);

        assertEquals(0, nextSlope.getRideCount());
        assertEquals(endVertex, skier.getLocation());
    }

    @Test
    void rideSlope() {
        SkierGroupProfile profile = new SkierGroupProfile(startVertex, 5, 0.5, 0.5, 0.5);
        TestSkier skier = new TestSkier(1, profile, startTime, context);
        Slope slope = new Slope(101, startVertex, endVertex, 60, 1.0, 5, 1.0);

        publisher.publishedEvents.clear();
        skier.rideSlope(slope);

        assertEquals(1, publisher.publishedEvents.size());
        Event event = publisher.publishedEvents.get(0);
        
        assertEquals(RideFinished.class, event.getClass());
        assertEquals(Time.secondsLater(startTime, slope.getRideTime()), event.getTime());

        assertNull(skier.getLocation());
        assertEquals(slope, skier.lastStartedHookEdge);
        assertEquals(1, slope.getRideCount());
    }

    private static class TestSkier extends Skier {
        private Edge nextChosenEdge;
        private Edge lastStartedHookEdge;
        private Edge lastFinishedHookEdge;

        public TestSkier(
                final int identifier,
                final SkierGroupProfile groupProfile,
                final Time startTime,
                final SimulationContext simulationContext) {
            super(identifier, groupProfile, startTime, simulationContext);
        }

        public void setNextChosenEdge(Edge edge) {
            this.nextChosenEdge = edge;
        }

        @Override
        Edge chooseEdge() {
            return nextChosenEdge;
        }

        @Override
        protected Edge chooseBestEdge() {
            return null;
        }

        @Override
        public void rideStarted(final Edge edge) {
            super.rideStarted(edge);
            this.lastStartedHookEdge = edge;
        }

        @Override
        public void rideFinished(final Edge edge) {
            super.rideFinished(edge);
            this.lastFinishedHookEdge = edge;
        }
    }

    private static class TestPublisher implements Publisher {
        private final List<Event> publishedEvents = new ArrayList<>();

        @Override
        public void publish(Event event) {
            publishedEvents.add(event);
        }
    }

    private static class TestClock implements Clock {
        private Time time;
        private final Time startTime;
        private final Time endTime;
        private boolean timeUp = false;

        public TestClock(Time startTime) {
            this.time = startTime;
            this.startTime = startTime;
            this.endTime = Time.secondsLater(startTime, 6 * 3600);
        }

        @Override
        public Time getTime() {
            return time;
        }

        @Override
        public Time getStartTime() {
            return startTime;
        }

        @Override
        public Time getEndTime() {
            return endTime;
        }

        @Override
        public boolean isTimeUp() {
            return timeUp;
        }

        public void setTimeUp(boolean timeUp) {
            this.timeUp = timeUp;
        }

        public void setTime(Time time) {
            this.time = time;
        }
    }
}
