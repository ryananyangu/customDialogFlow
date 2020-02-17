package com.custom.dialog.lab.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.asser;


import org.junit.BeforeClass;
import org.junit.Test;
import java.util.*;


/**
 * ScreenModeTest
 */
public class ScreenModeTest {

    public static ScreenNode node;

    @BeforeClass
    public static void beforeAllTestMethods() {
        List<String> NodeData1 = new ArrayList<>();
        NodeData1.add("start");
        NodeData1.add("Exit");
        HashMap<String,String> NodeExtraData1 = new HashMap<>();
        node = new ScreenNode("start page", 1, true, "Enter your name ", NodeData1, NodeExtraData1, "IM");
    }

    @Test
    public void testGetNodeActiveStatus(){
        assertTrue(node.getNodeActiveStatus());
    }

    @Test
    public void testGetNodeName(){
        assertEquals("start page", node.getNodeName());
    }

    @Test
    public void testNodeCurrentPage(){
        assertEquals(1, node.getNodeCurrentPage());
    }

    @Test
    public void testGetNodeData(){
        assertEquals(2,node.getNodeData().size());
        assertEquals("start", node.getNodeData().get(0));
        // assertNot 
    }


    
}