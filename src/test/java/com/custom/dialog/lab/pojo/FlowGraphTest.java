package com.custom.dialog.lab.pojo;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.asser;


import org.junit.BeforeClass;
// import org.junit.Test;
import java.util.*;


/**
 * FlowGraphTest
 */
public class FlowGraphTest {

    public static FlowGraph flow = new FlowGraph();


    @BeforeClass
    public static void beforeAllTestMethods() {
        // Only used in the children nodes
        List<String> NodeData1 = new ArrayList<>();
        NodeData1.add("start");
        NodeData1.add("Exit");
        // ***************************
        HashMap<String,String> NodeExtraData1 = new HashMap<>();
        ScreenNode node1 = new ScreenNode("start page", 1, true, "Enter your name ", NodeData1, NodeExtraData1, "IM");


        List<String> NodeData2 = new ArrayList<>();
        NodeData2.add("Payment Services");
        NodeData2.add("Validation Services");
        NodeData2.add("Financial Services");

        HashMap<String,String> NodeExtraData2 = new HashMap<>();
        ScreenNode node2 = new ScreenNode("age", 1, true, "Enter your Age", NodeData2, NodeExtraData2, "IM");

        // FlowGraph flow = new FlowGraph();
        flow.addNode(node2);
        flow.addNode(node1);

        flow.addLink(node1, node2);
    }

    // @Test
    // public void test(){
    //     assertEquals("expected", flow., messageSupplier);
    // }
}