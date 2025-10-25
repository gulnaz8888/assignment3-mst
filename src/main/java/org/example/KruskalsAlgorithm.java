package org.example;

import java.util.*;

public class KruskalsAlgorithm implements MSTAlgorithm {
    private PerformanceTracker tracker;

    public KruskalsAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public String getAlgorithmName() {
        return "Kruskal's Algorithm";
    }

    @Override
    public List<Edge> findMST(Graph graph) {
        tracker.startTimer();

        List<Edge> mst = new ArrayList<>();
        List<Edge> edges = new ArrayList<>(graph.getEdges());
        Collections.sort(edges);

        UnionFind uf = new UnionFind(graph.getVertices());

        for (Edge edge : edges) {
            if (mst.size() == graph.getVertices() - 1) {
                break;
            }

            tracker.countComparison();
            if (!uf.connected(edge.getSource(), edge.getDestination())) {
                tracker.countUnion();
                uf.union(edge.getSource(), edge.getDestination());
                mst.add(edge);
            }
        }

        tracker.stopTimer();
        return mst;
    }
}