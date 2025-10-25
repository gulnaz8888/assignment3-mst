import org.example.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    private PerformanceTracker tracker;

    @BeforeEach
    public void setup() {
        tracker = new PerformanceTracker();
    }

    @Test
    public void testSmallGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        PrimsAlgorithm prim = new PrimsAlgorithm(tracker);
        KruskalsAlgorithm kruskal = new KruskalsAlgorithm(tracker);

        List<Edge> primMST = prim.findMST(graph);
        tracker.reset();
        List<Edge> kruskalMST = kruskal.findMST(graph);

        assertEquals(calculateCost(primMST), calculateCost(kruskalMST));

        assertEquals(3, primMST.size());
        assertEquals(3, kruskalMST.size());
    }

    @Test
    public void testFiveVertices() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 5);

        PrimsAlgorithm prim = new PrimsAlgorithm(tracker);
        List<Edge> mst = prim.findMST(graph);

        assertEquals(4, mst.size());
    }

    @Test
    public void testTimeAndOperations() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 3);

        PrimsAlgorithm prim = new PrimsAlgorithm(tracker);
        prim.findMST(graph);

        assertTrue(tracker.getComparisons() >= 0);
        assertTrue(tracker.getExecutionTimeMillis() >= 0);
    }

    @Test
    public void testKruskalUnions() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 4);

        KruskalsAlgorithm kruskal = new KruskalsAlgorithm(tracker);
        kruskal.findMST(graph);

        assertTrue(tracker.getUnionOperations() > 0);
    }

    private int calculateCost(List<Edge> mst) {
        int total = 0;
        for (Edge edge : mst) {
            total += edge.getWeight();
        }
        return total;
    }
}