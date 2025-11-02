package graph.common;

import java.util.*;

public class Graph {

    private final int vertices;
    private final List<List<Edge>> adjacencyList;
    private final boolean directed;
    private final String weightModel;
    private int source;

    public Graph(int vertices, boolean directed, String weightModel) {
        this.vertices = vertices;
        this.directed = directed;
        this.weightModel = weightModel;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adjacencyList.get(u).add(new Edge(v, weight));
        if (!directed) {
            adjacencyList.get(v).add(new Edge(u, weight));
        }
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean isDirected() {
        return directed;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Graph reverse() {
        Graph reversed = new Graph(vertices, directed, weightModel);
        reversed.setSource(source);
        for (int u = 0; u < vertices; u++) {
            for (Edge edge : adjacencyList.get(u)) {
                reversed.addEdge(edge.to, u, edge.weight);
            }
        }
        return reversed;
    }

    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjacencyList) {
            count += edges.size();
        }
        return directed ? count : count / 2;
    }
}
