package org.example;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;
    private final PerformanceTracker tracker; // может быть null

    public UnionFind(int size) {
        this(size, null);
    }

    public UnionFind(int size, PerformanceTracker tracker) {
        this.tracker = tracker;
        this.parent = new int[size];
        this.rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (tracker != null) tracker.countFind();
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public void union(int x, int y) {
        if (tracker != null) tracker.countUnion();
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) return;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
    }

    public boolean unionIfDifferent(int x, int y) {
        if (tracker != null) tracker.countUnion();
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }
}