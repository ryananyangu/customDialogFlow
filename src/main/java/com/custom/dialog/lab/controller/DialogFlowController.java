package com.custom.dialog.lab.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.custom.dialog.lab.pojo.FlowGraph;
import com.custom.dialog.lab.pojo.ScreenNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DialogFlowController{

    @GetMapping("/get/adjacent/nodes")
    public List<ScreenNode> getAdjacent(){
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

        FlowGraph flow = new FlowGraph();
        flow.addNode(node2);
        flow.addNode(node1);

        flow.addLink(node1, node2);
        return flow.getAdjNodes(node2);
    }
    
}