package graph.scc;

import graph.common.Edge;
import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;

import java.util.*;

public class TarjanSCC {

    private final Graph graph;
    private final Metrics metrics;

    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int time;
    private List<List<Integer>> components;
    private int[] componentId;

    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    public SCCResult findSCCs() {
        int n = graph.getVertices();

        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        componentId = new int[n];
        components = new ArrayList<>();
        time = 0;

        Arrays.fill(disc, -1);
        Arrays.fill(componentId, -1);

        metrics.startTiming();

        for (int v = 0; v < n; v++) {
            if (disc[v] == -1) {
                dfs(v);
            }
        }

        metrics.stopTiming();

        return new SCCResult(components, componentId);
    }

    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;

        metrics.incrementOperation("dfs_visits");

        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            metrics.incrementOperation("edge_traversals");

            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> component = new ArrayList<>();
            int componentIdx = components.size();

            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                component.add(v);
                componentId[v] = componentIdx;
            } while (v != u);

            components.add(component);
        }
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
