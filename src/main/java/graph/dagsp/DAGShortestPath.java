package graph.dagsp;

import graph.common.Edge;
import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;
import graph.topo.TopologicalSort;

import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    private static final int INF = Integer.MAX_VALUE / 2;

    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    public PathResult findShortestPaths(int source) {
        int n = graph.getVertices();

        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> topoOrder = topoSort.sort();

        if (topoOrder == null) {
            throw new IllegalArgumentException("Graph contains a cycle");
        }

        int[] dist = new int[n];
        int[] pred = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(pred, -1);
        dist[source] = 0;

        metrics.startTiming();

        for (int u : topoOrder) {
            if (dist[u] != INF) {
                for (Edge edge : graph.getNeighbors(u)) {
                    int v = edge.to;
                    int weight = edge.weight;

                    metrics.incrementOperation("relaxations");

                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        pred[v] = u;
                    }
                }
            }
        }

        metrics.stopTiming();

        return new PathResult(dist, pred, source);
    }

    public PathResult findLongestPath(int source) {
        int n = graph.getVertices();

        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> topoOrder = topoSort.sort();

        if (topoOrder == null) {
            throw new IllegalArgumentException("Graph contains a cycle");
        }

        int[] dist = new int[n];
        int[] pred = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE / 2);
        Arrays.fill(pred, -1);
        dist[source] = 0;

        metrics.startTiming();

        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE / 2) {
                for (Edge edge : graph.getNeighbors(u)) {
                    int v = edge.to;
                    int weight = edge.weight;

                    metrics.incrementOperation("relaxations");

                    if (dist[u] + weight > dist[v]) {
                        dist[v] = dist[u] + weight;
                        pred[v] = u;
                    }
                }
            }
        }

        metrics.stopTiming();

        return new PathResult(dist, pred, source);
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
