package graph;

import java.util.HashMap;
import java.util.Map;

public class PageRank {

    private Graph graph;
    private double dampingFactor;
    private int maxIterations;
    private double tolerance;

    public PageRank(Graph graph) {
        this.graph = graph;
        this.dampingFactor = 0.85;
        this.maxIterations = 1;
        this.tolerance = 1e-6;
    }

    public PageRank(Graph graph, double dampingFactor, int maxIterations, double tolerance) {
        this.graph = graph;
        this.dampingFactor = dampingFactor;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
    }

    // Calculates PageRank for all nodes in the graph and also KOL ranks
    public Map<String, Double> calculatePageRank() {
        Map<String, Double> ranks = new HashMap<>();
        Map<String, Double> previousRanks = new HashMap<>();
        double danglingSum = 0.0;

        // Initialize ranks
        for (String node : graph.getNodes()) {
            ranks.put(node, 1.0);
            previousRanks.put(node, 1.0 / graph.getNodeCount());
        }

        // Iteratively update ranks
        for (int i = 0; i < maxIterations; i++) {
            // Calculate the sum of the ranks of dangling nodes
            danglingSum = 0.0;
            for (String node : graph.getNodes()) {
                if (graph.getOutDegree(node) == 0) {
                    danglingSum += previousRanks.get(node);
                }
            }

            // Update ranks for each node
            for (String node : graph.getNodes()) {
                double newRank = (1 - dampingFactor) / graph.getNodeCount();
                double sum = 0.0;

                // For each inbound edge, distribute the rank
                for (String neighbor : graph.getGraph().get(node)) {
                    int outDegree = graph.getOutDegree(neighbor);
                    
                    // Prevent division by zero
                    if (outDegree != 0) {
                        sum += previousRanks.get(neighbor) / outDegree;
                    } else {
                        // Handle dangling nodes by adding their rank to the total sum
                        System.out.println(neighbor + " is a dangling node with no outgoing edges.");
                        sum += previousRanks.get(neighbor) / graph.getNodeCount(); // Redistribute rank evenly across all nodes
                    }
                }

                // Add the contribution from dangling nodes
                newRank += dampingFactor * sum + dampingFactor * (danglingSum / graph.getNodeCount());
                ranks.put(node, newRank);
            }

            // Check for convergence
            boolean converged = true;
            for (String node : graph.getNodes()) {
                if (Math.abs(ranks.get(node) - previousRanks.get(node)) > tolerance) {
                    converged = false;
                }
            }

            if (converged) {
                break;
            }

            // Update previous ranks for the next iteration
            previousRanks.putAll(ranks);
        }

        return ranks;  // Return the rank of all nodes
    }
    
    

    // Get the rank of a specific user
    public double getRankOfUser(String user, Map<String, Double> ranks) {
        return ranks.getOrDefault(user, 0.0);
    }
}
