package org.example;

import java.util.*;

public class PrimsAlgorithm implements MSTAlgorithm {
    private final PerformanceTracker tracker;

    public PrimsAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public String getAlgorithmName() {
        return "Prim's Algorithm";
    }

    private static class PQEdge {
        final int from, to, w;
        PQEdge(int from, int to, int w) { this.from = from; this.to = to; this.w = w; }
    }

    @Override
    public List<Edge> findMST(Graph graph) {
        tracker.reset();
        tracker.startTimer();

        int n = graph.getVertices();
        List<Edge> mst = new ArrayList<>();
        if (n == 0) {
            tracker.stopTimer();
            return mst;
        }

        boolean[] inMST = new boolean[n];

        PriorityQueue<PQEdge> pq = new PriorityQueue<>((a, b) -> {
            tracker.countComparison();
            return Integer.compare(a.w, b.w);
        });

        inMST[0] = true;
        pushAdjEdges(0, graph, inMST, pq);

        while (!pq.isEmpty() && mst.size() < n - 1) {
            PQEdge e = pq.poll();
            int v = inMST[e.from] && !inMST[e.to] ? e.to
                    : inMST[e.to] && !inMST[e.from] ? e.from
                    : -1;

            if (v == -1) continue;

            int u = (v == e.to) ? e.from : e.to;
            mst.add(new Edge(u, v, e.w));
            inMST[v] = true;
            pushAdjEdges(v, graph, inMST, pq);
        }

        tracker.stopTimer();
        return mst;
    }

    private void pushAdjEdges(int u, Graph graph, boolean[] inMST, PriorityQueue<PQEdge> pq) {
        for (Edge e : graph.getAdjacentEdges(u)) {
            int v = (e.getSource() == u) ? e.getDestination() : e.getSource();
            if (!inMST[v]) {
                pq.offer(new PQEdge(u, v, e.getWeight()));
            }
        }
    }
}