package graph.topo;

import graph.common.Edge;
import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;

import java.util.*;

public class TopologicalSort {

    private final Graph graph;
    private final Metrics metrics;

    public TopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    public List<Integer> sort() {
        int n = graph.getVertices();
        int[] inDegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementOperation("queue_pushes");
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        metrics.startTiming();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementOperation("queue_pops");
            topoOrder.add(u);

            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                inDegree[v]--;

                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementOperation("queue_pushes");
                }
            }
        }

        metrics.stopTiming();

        if (topoOrder.size() != n) {
            return null;
        }

        return topoOrder;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
