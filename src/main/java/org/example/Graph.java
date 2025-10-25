package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);
        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(edge);
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        return adjacencyList.get(vertex);
    }
}