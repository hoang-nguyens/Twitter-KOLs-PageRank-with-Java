package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    private Map<String, Set<String>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    public void addNode(String node) {
        graph.putIfAbsent(node, new HashSet<>());
    }

    public void addEdge(String fromNode, String toNode) {
        graph.putIfAbsent(fromNode, new HashSet<>());
        graph.putIfAbsent(toNode, new HashSet<>());
        graph.get(fromNode).add(toNode);
    }

    public Map<String, Set<String>> getGraph() {
        return graph;
    }
}
