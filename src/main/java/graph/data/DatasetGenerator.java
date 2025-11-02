package graph.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {

    public static JsonObject generateGraph(int n, double edgeProbability, int maxWeight, boolean ensureCycle) {

        JsonObject graph = new JsonObject();
        graph.addProperty("directed", true);
        graph.addProperty("n", n);
        graph.addProperty("weight_model", "edge");

        Random random = new Random();
        JsonArray edges = new JsonArray();
        Set<String> edgeSet = new HashSet<>();

        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (u != v && random.nextDouble() < edgeProbability) {
                    String edgeKey = u + "->" + v;
                    if (!edgeSet.contains(edgeKey)) {
                        JsonObject edge = new JsonObject();
                        edge.addProperty("u", u);
                        edge.addProperty("v", v);
                        edge.addProperty("w", random.nextInt(maxWeight) + 1);
                        edges.add(edge);
                        edgeSet.add(edgeKey);
                    }
                }
            }
        }

        if (ensureCycle && n >= 3) {
            int[] cycle = {0, 1, 2};
            for (int i = 0; i < cycle.length; i++) {
                int u = cycle[i];
                int v = cycle[(i + 1) % cycle.length];
                String edgeKey = u + "->" + v;
                if (!edgeSet.contains(edgeKey)) {
                    JsonObject edge = new JsonObject();
                    edge.addProperty("u", u);
                    edge.addProperty("v", v);
                    edge.addProperty("w", random.nextInt(maxWeight) + 1);
                    edges.add(edge);
                    edgeSet.add(edgeKey);
                }
            }
        }

        graph.add("edges", edges);
        graph.addProperty("source", 0);

        return graph;
    }

    public static JsonObject generateDAG(int n, double edgeProbability, int maxWeight) {
        JsonObject graph = new JsonObject();
        graph.addProperty("directed", true);
        graph.addProperty("n", n);
        graph.addProperty("weight_model", "edge");

        Random random = new Random();
        JsonArray edges = new JsonArray();

        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (random.nextDouble() < edgeProbability) {
                    JsonObject edge = new JsonObject();
                    edge.addProperty("u", u);
                    edge.addProperty("v", v);
                    edge.addProperty("w", random.nextInt(maxWeight) + 1);
                    edges.add(edge);
                }
            }
        }

        graph.add("edges", edges);
        graph.addProperty("source", 0);

        return graph;
    }

    public static void saveToFile(JsonObject graph, String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(graph, writer);
        }
    }

    public static void main(String[] args) {
        try {
            saveToFile(generateGraph(8, 0.3, 10, true), "output/small_cyclic_1.json");
            saveToFile(generateDAG(7, 0.4, 8), "output/small_dag_1.json");
            saveToFile(generateGraph(10, 0.25, 12, true), "output/small_cyclic_2.json");

            saveToFile(generateGraph(15, 0.2, 15, true), "output/medium_mixed_1.json");
            saveToFile(generateGraph(18, 0.25, 20, true), "output/medium_mixed_2.json");
            saveToFile(generateDAG(20, 0.15, 18), "output/medium_dag_1.json");

            saveToFile(generateGraph(30, 0.1, 25, true), "output/large_sparse_1.json");
            saveToFile(generateGraph(40, 0.15, 30, true), "output/large_dense_1.json");
            saveToFile(generateDAG(50, 0.08, 35), "output/large_dag_1.json");

            System.out.println("All datasets generated successfully!");

        } catch (IOException e) {
            System.err.println("Error generating datasets: " + e.getMessage());
        }
    }
}
