package graph.topo;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TopologicalSortTest {

    @Test
    void testSimpleDAG() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNotNull(order);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    void testCyclicGraph() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNull(order);
    }

    @Test
    void testDisconnectedDAG() {
        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        TopologicalSort topo = new TopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNotNull(order);
        assertEquals(4, order.size());
    }
}
