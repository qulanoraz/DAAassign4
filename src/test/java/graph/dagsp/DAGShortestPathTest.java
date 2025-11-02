package graph.dagsp;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(0, 2, 10);

        DAGShortestPath dagsp = new DAGShortestPath(graph);
        PathResult result = dagsp.findShortestPaths(0);

        assertEquals(0, result.getDistances()[0]);
        assertEquals(5, result.getDistances()[1]);
        assertEquals(8, result.getDistances()[2]); // 0 -> 1 -> 2
    }

    @Test
    void testLongestPath() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(0, 2, 10);

        DAGShortestPath dagsp = new DAGShortestPath(graph);
        PathResult result = dagsp.findLongestPath(0);

        assertEquals(0, result.getDistances()[0]);
        assertEquals(5, result.getDistances()[1]);
        assertEquals(10, result.getDistances()[2]); // 0 -> 2 directly
    }

    @Test
    void testPathReconstruction() {
        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 1);

        DAGShortestPath dagsp = new DAGShortestPath(graph);
        PathResult result = dagsp.findShortestPaths(0);

        List<Integer> path = result.reconstructPath(3);
        assertNotNull(path);
        assertEquals(List.of(0, 1, 2, 3), path);
    }
}
