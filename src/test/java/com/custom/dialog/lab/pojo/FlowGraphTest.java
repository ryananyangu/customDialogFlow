package com.custom.dialog.lab.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.asser;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FlowGraphTest
 */
public class FlowGraphTest {

    public static FlowGraph graph = new FlowGraph();

    @BeforeClass
    public static void beforeAllTestMethods() {
        graph.addScreenNode("Bob");
        graph.addScreenNode("Alice");
        graph.addScreenNode("Mark");
        graph.addScreenNode("Rob");
        graph.addScreenNode("Maria");
        graph.addNodeLink("Bob", "Alice");
        graph.addNodeLink("Bob", "Rob");
        graph.addNodeLink("Alice", "Mark");
        graph.addNodeLink("Rob", "Mark");
        graph.addNodeLink("Alice", "Maria");
        graph.addNodeLink("Rob", "Maria");
    }

    @Test
    public void testDepthFirstSearch() {
        assertEquals("[Bob, Rob, Maria, Alice, Mark]", FlowGraph.depthFirstTraversal(graph, "Bob").toString());

    }

    @Test
    public void testBreadthFirstSearch() {
        assertEquals("[Bob, Alice, Rob, Mark, Maria]", FlowGraph.breadthFirstTraversal(graph, "Bob").toString());

    }

}