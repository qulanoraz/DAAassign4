package graph.topo;

import graph.common.Edge;
import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;

import java.util.*;

public class DFSTopologicalSort {

    private final Graph graph;
    private final Metrics metrics;
    private boolean[] visited;
    private Stack<Integer> stack;

    public DFSTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    public List<Integer> sort() {
        int n = graph.getVertices();
        visited = new boolean[n];
        stack = new Stack<>();

        metrics.startTiming();

        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs(v);
            }
        }

        metrics.stopTiming();

        List<Integer> topoOrder = new ArrayList<>();
        while (!stack.isEmpty()) {
            topoOrder.add(stack.pop());
            metrics.incrementOperation("stack_pops");
        }

        return topoOrder;
    }

    private void dfs(int u) {
        visited[u] = true;
        metrics.incrementOperation("dfs_visits");

        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            if (!visited[v]) {
                dfs(v);
            }
        }

        stack.push(u);
        metrics.incrementOperation("stack_pushes");
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
