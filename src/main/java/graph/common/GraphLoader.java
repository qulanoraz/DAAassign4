package graph.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.IOException;

public class GraphLoader {

    public static Graph loadFromJson(String filePath) throws IOException {

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader(filePath), JsonObject.class);

        boolean directed = json.get("directed").getAsBoolean();
        int n = json.get("n").getAsInt();
        String weightModel = json.get("weight_model").getAsString();

        Graph graph = new Graph(n, directed, weightModel);

        JsonArray edges = json.getAsJsonArray("edges");
        for (JsonElement edgeElement : edges) {
            JsonObject edge = edgeElement.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            graph.addEdge(u, v, w);
        }

        if (json.has("source")) {
            graph.setSource(json.get("source").getAsInt());
        }

        return graph;
    }
}
