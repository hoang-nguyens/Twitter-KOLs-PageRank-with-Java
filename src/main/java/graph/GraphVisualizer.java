package graph;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.*;

public class GraphVisualizer {
    private final Map<String, Set<String>> graph;

    public GraphVisualizer(Graph graphManager) {
        this.graph = graphManager.getGraph();
    }

    public void visualize() {
        mxGraph mxGraph = new mxGraph();
        Object parent = mxGraph.getDefaultParent();

        mxGraph.getModel().beginUpdate();
        try {
            Map<String, Object> vertexMap = new HashMap<>();
            for (String node : graph.keySet()) {
                Object vertex = mxGraph.insertVertex(parent, null, node, 0, 0, 80, 30);
                vertexMap.put(node, vertex);
            }

            for (String node : graph.keySet()) {
                for (String connectedNode : graph.get(node)) {
                    mxGraph.insertEdge(parent, null, "", vertexMap.get(node), vertexMap.get(connectedNode));
                }
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }

        mxFastOrganicLayout layout = new mxFastOrganicLayout(mxGraph);
        layout.execute(mxGraph.getDefaultParent());

        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new mxGraphComponent(mxGraph));
        frame.setSize(1200, 900);
        frame.setVisible(true);
    }
}
