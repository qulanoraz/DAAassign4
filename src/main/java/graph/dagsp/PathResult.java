package graph.dagsp;

import java.util.*;

public class PathResult {
    private final int[] distances;
    private final int[] predecessors;
    private final int source;

    public PathResult(int[] distances, int[] predecessors, int source) {
        this.distances = distances;
        this.predecessors = predecessors;
        this.source = source;
    }

    public int[] getDistances() {
        return distances;
    }

    public int[] getPredecessors() {
        return predecessors;
    }

    public int getSource() {
        return source;
    }

    public List<Integer> reconstructPath(int dest) {
        if (distances[dest] == Integer.MAX_VALUE / 2 ||
                distances[dest] == Integer.MIN_VALUE / 2) {
            return null; // No path exists
        }

        List<Integer> path = new ArrayList<>();
        int current = dest;

        while (current != -1) {
            path.add(current);
            current = predecessors[current];
        }

        Collections.reverse(path);
        return path;
    }

    public int findCriticalPathEnd() {
        int maxDist = Integer.MIN_VALUE;
        int maxVertex = -1;

        for (int v = 0; v < distances.length; v++) {
            if (distances[v] > maxDist && distances[v] != Integer.MIN_VALUE / 2) {
                maxDist = distances[v];
                maxVertex = v;
            }
        }

        return maxVertex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shortest paths from source ").append(source).append(":\n");
        for (int v = 0; v < distances.length; v++) {
            sb.append("  To ").append(v).append(": ");
            if (distances[v] == Integer.MAX_VALUE / 2 ||
                    distances[v] == Integer.MIN_VALUE / 2) {
                sb.append("unreachable\n");
            } else {
                sb.append("distance = ").append(distances[v]);
                List<Integer> path = reconstructPath(v);
                sb.append(", path = ").append(path).append("\n");
            }
        }
        return sb.toString();
    }
}
