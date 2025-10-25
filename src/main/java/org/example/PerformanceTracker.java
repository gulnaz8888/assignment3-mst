package org.example;

public class PerformanceTracker {
    private long comparisons;
    private long unionOperations;
    private long findOperations;
    private long startTime;
    private long endTime;

    public PerformanceTracker() {
        reset();
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    public void countComparison() {
        comparisons++;
    }

    public void countUnion() {
        unionOperations++;
    }

    public void countFind() {
        findOperations++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getUnionOperations() {
        return unionOperations;
    }

    public long getFindOperations() {
        return findOperations;
    }

    public long getTotalOperations() {
        return comparisons + unionOperations + findOperations;
    }

    public long getExecutionTimeMillis() {
        return endTime - startTime;
    }

    public void reset() {
        comparisons = 0;
        unionOperations = 0;
        findOperations = 0;
        startTime = 0;
        endTime = 0;
    }
}