package io.github.ignacypekala.skier;

import io.github.ignacypekala.Edge;
import io.github.ignacypekala.Vertex;
import io.github.ignacypekala.simulation.SimulationContext;
import io.github.ignacypekala.utils.Time;

public class LocalSkier extends Skier {
    public LocalSkier(
            int identifier,
            SkierGroupProfile groupProfile,
            Time startTime,
            SimulationContext simulationContext) {
        super(identifier, groupProfile, startTime, simulationContext);
    }

    @Override
    protected Edge chooseBestEdge() {
        Vertex location = getLocation();
        Edge[] edges = location.getEdges();
        double maxAppeal = -1;
        Edge mostAppealing = null;
        for (int i = 0; i < location.getEdgeCount(); i++) {
            final Edge edge = edges[i];
            final double appeal = edge.calculateAppeal(this);
            if (appeal > maxAppeal) {
                maxAppeal = appeal;
                mostAppealing = edge;
            }
        }
        return mostAppealing;
    }
}
