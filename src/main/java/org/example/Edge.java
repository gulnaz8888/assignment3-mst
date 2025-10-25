package org.example;
import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final int source;
    private final int destination;
    private final int weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d-%d, %d)", source, destination, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return source == edge.source &&
                destination == edge.destination &&
                weight == edge.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, weight);
    }
}
