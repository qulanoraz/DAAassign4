import graph.common.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String DATA_DIR = "data/";
    private static final String REPORT_FILE = "ANALYSIS_REPORT.txt";

    private static final String[] DATASETS = {
            // Small datasets
            "small_cyclic_1.json",
            "small_dag_1.json",
            "small_cyclic_2.json",
            // Medium datasets
            "medium_mixed_1.json",
            "medium_mixed_2.json",
            "medium_dag_1.json",
            // Large datasets
            "large_sparse_1.json",
            "large_dense_1.json",
            "large_dag_1.json"
    };

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("    SMART CITY SCHEDULING - COMPREHENSIVE ANALYSIS");
        System.out.println("    Processing 9 Datasets");
        System.out.println("=".repeat(70));
        System.out.println();

        List<DatasetResult> results = new ArrayList<>();

        for (int i = 0; i < DATASETS.length; i++) {
            String filename = DATA_DIR + DATASETS[i];
            System.out.println("Processing Dataset " + (i + 1) + "/" + DATASETS.length +
                    ": " + DATASETS[i]);
            System.out.println("-".repeat(70));

            try {
                DatasetResult result = processDataset(filename, DATASETS[i]);
                results.add(result);
                System.out.println("Successfully processed\n");
            } catch (Exception e) {
                System.err.println("Error processing " + DATASETS[i] + ": " +
                        e.getMessage());
                System.out.println();
            }
        }

        try {
            generateReport(results);
            System.out.println("=".repeat(70));
            System.out.println("Analysis complete! Report saved to: " + REPORT_FILE);
            System.out.println("=".repeat(70));
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private static DatasetResult processDataset(String filepath, String datasetName)
            throws IOException {
        DatasetResult result = new DatasetResult(datasetName);

        Graph graph = GraphLoader.loadFromJson(filepath);
        result.vertices = graph.getVertices();
        result.edges = graph.getEdgeCount();
        result.weightModel = graph.getWeightModel();

        System.out.println("  Vertices: " + result.vertices);
        System.out.println("  Edges: " + result.edges);

        TarjanSCC sccFinder = new TarjanSCC(graph);
        SCCResult sccResult = sccFinder.findSCCs();
        result.sccCount = sccResult.getComponentCount();
        result.sccTime = sccFinder.getMetrics().getElapsedTimeMillis();
        result.sccDfsVisits = sccFinder.getMetrics().getOperationCount("dfs_visits");
        result.sccEdgeTraversals = sccFinder.getMetrics()
                .getOperationCount("edge_traversals");

        result.largestSccSize = 0;
        for (List<Integer> component : sccResult.getComponents()) {
            if (component.size() > result.largestSccSize) {
                result.largestSccSize = component.size();
            }
        }

        System.out.println("  SCCs found: " + result.sccCount +
                " (largest: " + result.largestSccSize + " vertices)");

        Graph condensation = CondensationGraph.build(graph, sccResult);
        result.condensationVertices = condensation.getVertices();
        result.condensationEdges = condensation.getEdgeCount();

        TopologicalSort topoSort = new TopologicalSort(condensation);
        List<Integer> topoOrder = topoSort.sort();
        result.topoTime = topoSort.getMetrics().getElapsedTimeMillis();
        result.topoOperations = topoSort.getMetrics().getOperationCount("queue_pushes");

        if (topoOrder != null) {
            System.out.println("  Topological sort: SUCCESS");
        } else {
            System.out.println("  Topological sort: FAILED (cycle detected)");
        }

        if (topoOrder != null && condensation.getVertices() > 0) {
            int source = CondensationGraph.mapSource(graph.getSource(),
                    sccResult.getComponentId());

            if (source >= 0 && source < condensation.getVertices()) {
                DAGShortestPath dagsp = new DAGShortestPath(condensation);

                PathResult shortestPaths = dagsp.findShortestPaths(source);
                result.shortestPathTime = dagsp.getMetrics().getElapsedTimeMillis();
                result.shortestPathRelaxations = dagsp.getMetrics()
                        .getOperationCount("relaxations");

                result.reachableVertices = 0;
                for (int dist : shortestPaths.getDistances()) {
                    if (dist != Integer.MAX_VALUE / 2) {
                        result.reachableVertices++;
                    }
                }

                PathResult longestPaths = dagsp.findLongestPath(source);
                int criticalEnd = longestPaths.findCriticalPathEnd();
                if (criticalEnd != -1) {
                    result.criticalPathLength = longestPaths.getDistances()[criticalEnd];
                    result.criticalPath = longestPaths.reconstructPath(criticalEnd);
                }

                System.out.println("  Shortest paths computed from source " + source);
                System.out.println("  Critical path length: " + result.criticalPathLength);
            }
        }

        return result;
    }

    private static void generateReport(List<DatasetResult> results) throws IOException {

        PrintWriter writer = new PrintWriter(new File(REPORT_FILE));

        writer.println("=".repeat(80));
        writer.println("           SMART CITY SCHEDULING - COMPREHENSIVE ANALYSIS REPORT");
        writer.println("=".repeat(80));
        writer.println();

        writer.println("SUMMARY STATISTICS");
        writer.println("-".repeat(80));
        writer.println(String.format("Total datasets processed: %d", results.size()));
        writer.println();

        writer.println("DATASET OVERVIEW");
        writer.println("-".repeat(80));
        writer.println(String.format("%-25s | %8s | %8s | %8s | %12s",
                "Dataset", "Vertices", "Edges", "SCCs", "Largest SCC"));
        writer.println("-".repeat(80));

        for (DatasetResult result : results) {
            writer.println(String.format("%-25s | %8d | %8d | %8d | %12d",
                    result.datasetName,
                    result.vertices,
                    result.edges,
                    result.sccCount,
                    result.largestSccSize));
        }
        writer.println();

        writer.println("SCC ALGORITHM PERFORMANCE (Tarjan's Algorithm)");
        writer.println("-".repeat(80));
        writer.println(String.format("%-25s | %10s | %12s | %15s",
                "Dataset", "Time (ms)", "DFS Visits", "Edge Traversals"));
        writer.println("-".repeat(80));

        for (DatasetResult result : results) {
            writer.println(String.format("%-25s | %10.3f | %12d | %15d",
                    result.datasetName,
                    result.sccTime,
                    result.sccDfsVisits,
                    result.sccEdgeTraversals));
        }
        writer.println();

        writer.println("TOPOLOGICAL SORT PERFORMANCE (Kahn's Algorithm)");
        writer.println("-".repeat(80));
        writer.println(String.format("%-25s | %10s | %15s | %12s",
                "Dataset", "Time (ms)", "Queue Ops", "Cond. Edges"));
        writer.println("-".repeat(80));

        for (DatasetResult result : results) {
            writer.println(String.format("%-25s | %10.3f | %15d | %12d",
                    result.datasetName,
                    result.topoTime,
                    result.topoOperations,
                    result.condensationEdges));
        }
        writer.println();

        writer.println("DAG SHORTEST PATH PERFORMANCE");
        writer.println("-".repeat(80));
        writer.println(String.format("%-25s | %10s | %12s | %10s | %15s",
                "Dataset", "Time (ms)", "Relaxations", "Reachable", "Critical Path"));
        writer.println("-".repeat(80));

        for (DatasetResult result : results) {
            writer.println(String.format("%-25s | %10.3f | %12d | %10d | %15d",
                    result.datasetName,
                    result.shortestPathTime,
                    result.shortestPathRelaxations,
                    result.reachableVertices,
                    result.criticalPathLength));
        }
        writer.println();

        writer.println("ANALYSIS");
        writer.println("-".repeat(80));
        writer.println();

        writer.println("1. SCC Algorithm Bottlenecks:");
        writer.println("   - Tarjan's algorithm shows O(V+E) complexity as expected");
        writer.println("   - Time increases proportionally with edge count");
        writer.println("   - Dense graphs have more edge traversals but similar DFS visits");
        writer.println();

        writer.println("2. Effect of Graph Structure:");
        writer.println("   - Cyclic graphs compress into fewer components");
        writer.println("   - Large SCCs indicate tightly coupled task dependencies");
        writer.println("   - Pure DAGs have SCC count equal to vertex count");
        writer.println();

        writer.println("3. Topological Sort Performance:");
        writer.println("   - Kahn's algorithm performance depends on condensation edge count");
        writer.println("   - Queue operations correlate with condensation vertex count");
        writer.println("   - Sparse condensation graphs sort faster");
        writer.println();

        writer.println("4. DAG Shortest Path Insights:");
        writer.println("   - Number of relaxations depends on edge density in condensation");
        writer.println("   - Critical path identifies longest task dependency chain");
        writer.println("   - Reachability varies based on graph connectivity structure");
        writer.println();

        writer.println("CONCLUSIONS");
        writer.println("-".repeat(80));
        writer.println();
        writer.println("• For strongly connected task dependencies:");
        writer.println("  - Use Tarjan's SCC to identify tightly coupled task groups");
        writer.println("  - Compress into meta-tasks to simplify scheduling");
        writer.println();
        writer.println("• For dependency ordering:");
        writer.println("  - Kahn's algorithm provides intuitive level-by-level processing");
        writer.println("  - DFS variant is simpler but less intuitive for scheduling");
        writer.println();
        writer.println("• For critical path analysis:");
        writer.println("  - DAG longest path identifies project completion time");
        writer.println("  - Useful for resource allocation and deadline planning");
        writer.println();
        writer.println("• Practical recommendation:");
        writer.println("  - Always detect cycles first (SCC) before scheduling");
        writer.println("  - Use condensation graph for scalable dependency management");
        writer.println("  - Critical path guides priority assignment");
        writer.println();

        writer.println("End of Report");

        writer.close();
    }

    private static class DatasetResult {
        String datasetName;
        int vertices;
        int edges;
        String weightModel;

        int sccCount;
        int largestSccSize;
        double sccTime;
        long sccDfsVisits;
        long sccEdgeTraversals;

        int condensationVertices;
        int condensationEdges;

        double topoTime;
        long topoOperations;

        double shortestPathTime;
        long shortestPathRelaxations;
        int reachableVertices;
        int criticalPathLength;
        List<Integer> criticalPath;

        DatasetResult(String name) {
            this.datasetName = name;
            this.criticalPathLength = 0;
        }
    }
}
