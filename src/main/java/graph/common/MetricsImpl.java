package graph.common;

import java.util.HashMap;
import java.util.Map;

public class MetricsImpl implements Metrics {

    private long startTime;
    private long endTime;
    private final Map<String, Long> operationCounts;

    public MetricsImpl() {
        this.operationCounts = new HashMap<>();
    }

    @Override
    public void startTiming() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTiming() {
        endTime = System.nanoTime();
    }

    @Override
    public long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    @Override
    public double getElapsedTimeMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    @Override
    public void incrementOperation(String operation) {
        operationCounts.put(operation, operationCounts.getOrDefault(operation, 0L) + 1);
    }

    @Override
    public long getOperationCount(String operation) {
        return operationCounts.getOrDefault(operation, 0L);
    }

    @Override
    public void reset() {
        startTime = 0;
        endTime = 0;
        operationCounts.clear();
    }

    @Override
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Execution time: ").append(getElapsedTimeMillis()).append(" ms\n");
        sb.append("Operations:\n");
        for (Map.Entry<String, Long> entry : operationCounts.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
