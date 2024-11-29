package graph;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import utils.FileReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize components
            FileReader fileReader = new FileReader();
            Graph graph = new Graph();
            GraphBuilder graphBuilder = new GraphBuilder(graph);

            // File paths
            String csvFile = "D:\\file link\\result.csv";
            String jsonFile = "D:\\file link\\all.json";

            // Read CSV and build graph
            List<CSVRecord> csvRecords = fileReader.readCSV(csvFile);
            graphBuilder.buildGraphFromCSV(csvRecords);

            // Read JSON and build graph
            JSONObject jsonObject = fileReader.readJSON(jsonFile);
            graphBuilder.buildGraphFromJSON(jsonObject);

            // Visualize graph or compute PageRank
            System.out.println("Graph structure: " + graph.getGraph());
            
            GraphVisualizer visualizer = new GraphVisualizer(graph);
            visualizer.visualize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
