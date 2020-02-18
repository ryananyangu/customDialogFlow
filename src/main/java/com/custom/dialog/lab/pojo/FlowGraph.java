package com.custom.dialog.lab.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.custom.dialog.lab.utils.Utils;

import org.json.JSONObject;

public class FlowGraph {

    /**
     * Used to load from file in system
     * 
     * @param path
     * @param fileName
     */
    public FlowGraph(String path, String fileName) {

    }

    public FlowGraph(String jsonFlow) {

    }

    public FlowGraph() {

    }

    private Map<ScreenNode, List<ScreenNode>> adjNodes = new HashMap<>();

    public List<ScreenNode> getAdjNodes(ScreenNode node) {
        return adjNodes.get(node);
    }

    public void addNode(ScreenNode node) {
        adjNodes.putIfAbsent(node, new ArrayList<>());
    }

    public void removeNode(ScreenNode node) {
        adjNodes.values().stream().forEach(e -> e.remove(node));
        adjNodes.remove(node);
    }

    public void addLink(ScreenNode node, ScreenNode node2) {
        adjNodes.get(node).add(node2);
        adjNodes.get(node2).add(node);
    }

    public void removeLink(ScreenNode node, ScreenNode node2) {
        List<ScreenNode> eN1 = adjNodes.get(node);
        List<ScreenNode> eN2 = adjNodes.get(node2);
        if (eN1 != null)
            eN1.remove(node2);
        if (eN2 != null)
            eN2.remove(node);
    }

    public Set<String> depthFirstTraversal(FlowGraph graph, ScreenNode RootNode) {
        Set<String> visited = new LinkedHashSet<String>();
        Stack<ScreenNode> stack = new Stack<ScreenNode>();
        stack.push(RootNode);
        while (!stack.isEmpty()) {
            ScreenNode node = stack.pop();
            if (!visited.contains(node.getNodeName())) {
                visited.add(node.getNodeName());
                for (ScreenNode n : graph.getAdjNodes(node)) {
                    stack.push(n);
                }
            }
        }
        return visited;
    }

    public Set<String> breadthFirstTraversal(FlowGraph graph, ScreenNode RootNode) {
        Set<String> visited = new LinkedHashSet<String>();
        Queue<ScreenNode> queue = new LinkedList<ScreenNode>();
        queue.add(RootNode);
        visited.add(RootNode.getNodeName());
        while (!queue.isEmpty()) {
            ScreenNode node = queue.poll();
            for (ScreenNode n : graph.getAdjNodes(node)) {
                if (!visited.contains(n.getNodeName())) {
                    visited.add(n.getNodeName());
                    queue.add(n);
                }
            }
        }
        return visited;
    }


}