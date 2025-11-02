package graph.scc;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TarjanSCCTest {

    @Test
    void testSimpleCycle() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TarjanSCC scc = new TarjanSCC(graph);
        SCCResult result = scc.findSCCs();

        assertEquals(1, result.getComponentCount());
        assertEquals(3, result.getComponents().get(0).size());
    }

    @Test
    void testDAG() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        TarjanSCC scc = new TarjanSCC(graph);
        SCCResult result = scc.findSCCs();

        assertEquals(3, result.getComponentCount());
    }

    @Test
    void testMultipleSCCs() {
        Graph graph = new Graph(6, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 3, 1);

        TarjanSCC scc = new TarjanSCC(graph);
        SCCResult result = scc.findSCCs();

        assertEquals(2, result.getComponentCount());
    }

    @Test
    void testSingleVertex() {
        Graph graph = new Graph(1, true, "edge");

        TarjanSCC scc = new TarjanSCC(graph);
        SCCResult result = scc.findSCCs();

        assertEquals(1, result.getComponentCount());
    }
}
