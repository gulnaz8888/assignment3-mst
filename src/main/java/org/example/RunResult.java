package org.example;

import java.util.List;

public class RunResult {
    public String graphId;
    public String algorithm;
    public int vertices;
    public int edges;
    public int mstCost;
    public int mstEdgeCount;
    public boolean connected;
    public long comparisons;
    public long unions;
    public long finds;
    public long execMs;

    public List<MstEdgeOut> mstEdgesDetailed;

    public static class MstEdgeOut {
        public String from;
        public String to;
        public int weight;
        public MstEdgeOut() {}
        public MstEdgeOut(String from, String to, int weight) {
            this.from = from; this.to = to; this.weight = weight;
        }
    }

    public RunResult() {}

    public RunResult(String graphId, String algorithm, int vertices, int edges,
                     int mstCost, int mstEdgeCount, boolean connected,
                     long comparisons, long unions, long finds, long execMs) {
        this.graphId = graphId;
        this.algorithm = algorithm;
        this.vertices = vertices;
        this.edges = edges;
        this.mstCost = mstCost;
        this.mstEdgeCount = mstEdgeCount;
        this.connected = connected;
        this.comparisons = comparisons;
        this.unions = unions;
        this.finds = finds;
        this.execMs = execMs;
    }
}