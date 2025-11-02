package graph.common;

public interface Metrics {

    void startTiming();

    void stopTiming();

    long getElapsedTimeNanos();

    double getElapsedTimeMillis();

    void incrementOperation(String operation);

    long getOperationCount(String operation);

    void reset();

    String getSummary();
}
