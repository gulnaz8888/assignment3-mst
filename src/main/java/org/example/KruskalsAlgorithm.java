package org.example;

import java.util.*;

public class KruskalsAlgorithm implements MSTAlgorithm {
    private final PerformanceTracker tracker;

    public KruskalsAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public String getAlgorithmName() {
        return "Kruskal's Algorithm";
    }

    @Override
    public List<Edge> findMST(Graph graph) {
        tracker.reset();
        tracker.startTimer();

        List<Edge> mst = new ArrayList<>();
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        edges.sort((a, b) -> {
            tracker.countComparison();
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        UnionFind uf = new UnionFind(graph.getVertices(), tracker);

        for (Edge edge : edges) {
            if (mst.size() == graph.getVertices() - 1) break;

            if (uf.unionIfDifferent(edge.getSource(), edge.getDestination())) {
                mst.add(edge);
            } else {
            }
        }

        tracker.stopTimer();
        return mst;
    }
}