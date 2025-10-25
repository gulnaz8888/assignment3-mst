package org.example;

import java.util.List;

public interface MSTAlgorithm {
    List<Edge> findMST(Graph graph);
    String getAlgorithmName();
}