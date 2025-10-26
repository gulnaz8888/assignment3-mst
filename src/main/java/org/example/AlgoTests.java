package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AlgoTests {

    @Test
    void costsEqualAndSizeVminus1_onConnected() {
        Graph g = new Graph(4);
        g.addEdge(0,1,4);
        g.addEdge(0,2,3);
        g.addEdge(1,2,2);
        g.addEdge(1,3,5);
        g.addEdge(2,3,7);

        PerformanceTracker tp = new PerformanceTracker();
        PerformanceTracker tk = new PerformanceTracker();
        var prim = new PrimsAlgorithm(tp);
        var kr = new KruskalsAlgorithm(tk);

        var pmst = prim.findMST(g);
        var kmst = kr.findMST(g);

        int pc = pmst.stream().mapToInt(Edge::getWeight).sum();
        int kc = kmst.stream().mapToInt(Edge::getWeight).sum();

        assertEquals(g.getVertices()-1, pmst.size());
        assertEquals(g.getVertices()-1, kmst.size());
        assertEquals(pc, kc);
    }

    @Test
    void disconnectedHandled() {
        Graph g = new Graph(3);
        g.addEdge(0,1,1);

        var pmst = new PrimsAlgorithm(new PerformanceTracker()).findMST(g);
        var kmst = new KruskalsAlgorithm(new PerformanceTracker()).findMST(g);

        assertTrue(pmst.size() < g.getVertices()-1);
        assertTrue(kmst.size() < g.getVertices()-1);
    }

    @Test
    void metricsNonNegativeAndTimeMs() {
        Graph g = new Graph(4);
        g.addEdge(0,1,1);
        g.addEdge(1,2,2);
        g.addEdge(2,3,3);
        g.addEdge(0,3,10);

        PerformanceTracker tp = new PerformanceTracker();
        PerformanceTracker tk = new PerformanceTracker();

        new PrimsAlgorithm(tp).findMST(g);
        new KruskalsAlgorithm(tk).findMST(g);

        assertTrue(tp.getExecutionTimeMillis() >= 0);
        assertTrue(tp.getComparisons() >= 0);
        assertTrue(tp.getUnionOperations() >= 0);
        assertTrue(tp.getFindOperations() >= 0);

        assertTrue(tk.getExecutionTimeMillis() >= 0);
        assertTrue(tk.getComparisons() >= 0);
        assertTrue(tk.getUnionOperations() >= 0);
        assertTrue(tk.getFindOperations() >= 0);
    }
}