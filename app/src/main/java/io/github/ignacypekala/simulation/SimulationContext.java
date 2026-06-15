package io.github.ignacypekala.simulation;

import io.github.ignacypekala.event.Publisher;

public record SimulationContext(Clock clock, Publisher publisher) {
};
