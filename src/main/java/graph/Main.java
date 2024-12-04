package graph;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import utils.FileReader;

import java.util.List;
import java.util.Map;

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
            
            
//            System.out.println(graph.getNodeCount());

            // Visualize graph or compute PageRank
//            System.out.println("Graph structure: " + graph.getGraph());
            
//         // Step 3: Compute PageRank
            PageRank pageRank = new PageRank(graph);
            Map<String, Double> pageRankScores = pageRank.calculatePageRank();
//
            // Step 4: Display the results
            System.out.println("PageRank Scores:");
            for (Map.Entry<String, Double> entry : pageRankScores.entrySet()) {
                System.out.println("Node: " + entry.getKey() + ", PageRank: " + entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
