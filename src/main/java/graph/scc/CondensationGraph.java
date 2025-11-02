package graph.scc;

import graph.common.Edge;
import graph.common.Graph;

import java.util.*;

public class CondensationGraph {

    public static Graph build(Graph original, SCCResult sccResult) {

        int numComponents = sccResult.getComponentCount();
        int[] componentId = sccResult.getComponentId();

        Graph condensation = new Graph(numComponents, true, original.getWeightModel());

        Set<String> addedEdges = new HashSet<>();

        Map<String, Integer> edgeWeights = new HashMap<>();

        for (int u = 0; u < original.getVertices(); u++) {
            int compU = componentId[u];

            for (Edge edge : original.getNeighbors(u)) {
                int v = edge.to;
                int compV = componentId[v];

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;

                    if (!addedEdges.contains(edgeKey)) {
                        edgeWeights.put(edgeKey, edge.weight);
                        addedEdges.add(edgeKey);
                    } else {
                        edgeWeights.put(edgeKey,
                                Math.min(edgeWeights.get(edgeKey), edge.weight));
                    }
                }
            }
        }

        for (String edgeKey : addedEdges) {
            String[] parts = edgeKey.split("->");
            int compU = Integer.parseInt(parts[0]);
            int compV = Integer.parseInt(parts[1]);
            int weight = edgeWeights.get(edgeKey);
            condensation.addEdge(compU, compV, weight);
        }

        return condensation;
    }

    public static int mapSource(int originalSource, int[] componentId) {
        return componentId[originalSource];
    }
}
