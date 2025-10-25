package org.example;

import java.util.*;

public class PrimsAlgorithm implements MSTAlgorithm {
    private PerformanceTracker tracker;

    public PrimsAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public String getAlgorithmName() {
        return "Prim's Algorithm";
    }

    @Override
    public List<Edge> findMST(Graph graph) {
        tracker.startTimer();

        int vertices = graph.getVertices();
        List<Edge> mst = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        visited[0] = true;
        addEdgesToQueue(0, graph, pq);

        while (!pq.isEmpty() && mst.size() < vertices - 1) {
            Edge edge = pq.poll();
            tracker.countComparison();

            int nextVertex = getUnvisitedVertex(edge, visited);

            if (nextVertex != -1) {
                visited[nextVertex] = true;
                mst.add(edge);
                addEdgesToQueue(nextVertex, graph, pq);
            }
        }

        tracker.stopTimer();
        return mst;
    }

    private void addEdgesToQueue(int vertex, Graph graph, PriorityQueue<Edge> pq) {
        for (Edge edge : graph.getAdjacentEdges(vertex)) {
            pq.offer(edge);
        }
    }

    private int getUnvisitedVertex(Edge edge, boolean[] visited) {
        int source = edge.getSource();
        int destination = edge.getDestination();

        tracker.countComparison();
        if (!visited[source] && visited[destination]) {
            return source;
        }

        tracker.countComparison();
        if (visited[source] && !visited[destination]) {
            return destination;
        }

        return -1;
    }
}