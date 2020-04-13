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
import lombok.Data;

@Data
public class FlowGraph {

    private Map<ScreenNode, List<ScreenNode>> adjScreenNodes = new HashMap<>();

    /**
     * @return the adjVertices
     */
    public Map<ScreenNode, List<ScreenNode>> getAdjScreenNodes() {
        return adjScreenNodes;
    }

    List<ScreenNode> getAdjScreenNodes(String nodeName) {
        ScreenNode node = new ScreenNode();
        node.setNodeName(nodeName);
        return adjScreenNodes.get(node);
    }

    List<ScreenNode> getAdjScreenNodes(ScreenNode node) {
        return adjScreenNodes.get(node);
    }

    void addScreenNode(String nodeName) {
        ScreenNode node = new ScreenNode();
        node.setNodeName(nodeName);
        adjScreenNodes.putIfAbsent(node, new ArrayList<>());
    }

    void addScreenNode(ScreenNode screenNode) {
        adjScreenNodes.putIfAbsent(screenNode, new ArrayList<>());
    }

    void removeScreenNode(String nodeName) {
        ScreenNode node = new ScreenNode();
        node.setNodeName(nodeName);
        adjScreenNodes.values().stream().forEach(e -> e.remove(node));
        adjScreenNodes.remove(node);
    }

    void removeScreenNode(ScreenNode node) {
        adjScreenNodes.values().stream().forEach(e -> e.remove(node));
        adjScreenNodes.remove(node);
    }

    void addNodeLink(String nodeName1, String nodeName2) {
        ScreenNode node1 = new ScreenNode();
        node1.setNodeName(nodeName1);
        ScreenNode node2 = new ScreenNode();
        node2.setNodeName(nodeName2);
        adjScreenNodes.get(node1).add(node2);
        adjScreenNodes.get(node2).add(node1);
    }

    void addNodeLink(ScreenNode node1, ScreenNode node2) {
        adjScreenNodes.get(node1).add(node2);
        adjScreenNodes.get(node2).add(node1);
    }

    void removeNodeLink(String nodeName1, String nodeName2) {
        ScreenNode node1 = new ScreenNode();
        node1.setNodeName(nodeName1);
        ScreenNode node2 = new ScreenNode();
        node2.setNodeName(nodeName2);
        List<ScreenNode> eV1 = adjScreenNodes.get(node1);
        List<ScreenNode> eV2 = adjScreenNodes.get(node2);
        if (eV1 != null) {
            eV1.remove(node1);
        }
        if (eV2 != null) {
            eV2.remove(node2);
        }
    }

    void removeNodeLink(ScreenNode node1, ScreenNode node2) {
        List<ScreenNode> eV1 = adjScreenNodes.get(node1);
        List<ScreenNode> eV2 = adjScreenNodes.get(node2);
        if (eV1 != null) {
            eV1.remove(node1);
        }
        if (eV2 != null) {
            eV2.remove(node2);
        }
    }

    public static Set<String> depthFirstTraversal(FlowGraph graph, String root) {
        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                graph.getAdjScreenNodes(vertex).forEach((node) -> {
                    stack.push(node.getNodeName());
                });
            }
        }
        return visited;
    }

    public static Set<String> depthFirstTraversal(FlowGraph graph, ScreenNode root) {
        Set<String> visited = new LinkedHashSet<>();
        Stack<ScreenNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            ScreenNode node = stack.pop();
            if (!visited.contains(node.getNodeName())) {
                visited.add(node.getNodeName());
                graph.getAdjScreenNodes(node).forEach((node_tmp) -> {
                    stack.push(node_tmp);
                });
            }
        }
        return visited;
    }

    public static Set<String> breadthFirstTraversal(FlowGraph graph, String root) {
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            graph.getAdjScreenNodes(vertex).stream().filter((v) -> (!visited.contains(v.getNodeName()))).map((v) -> {
                visited.add(v.getNodeName());
                return v;
            }).forEachOrdered((v) -> {
                queue.add(v.getNodeName());
            });
        }
        return visited;
    }
}
