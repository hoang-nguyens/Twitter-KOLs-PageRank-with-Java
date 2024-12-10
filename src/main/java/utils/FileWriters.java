package utils;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileWriters {

    // Method to append links to a CSV file in the userlink folder
    public void appendLinksToCSV(Set<String> listLink, String keyword) {
        // Create the 'userlink' folder if it doesn't exist
        File directory = new File("userlink");
        if (!directory.exists()) {
            directory.mkdir();  // Create the folder if it doesn't exist
        }

        // Set the CSV file name without the word 'UsersLinks'
        String fileName = "userlink/" + keyword + ".csv";  // CSV file name will be just the keyword

        try (CSVWriter writer = new CSVWriter(new java.io.FileWriter(fileName, true))) { // Set to true for append mode
            for (String element : listLink) {
                String[] record = { element };
                writer.writeNext(record); // Write the record to the CSV
            }
            System.out.println("Set appended to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // Write the results (followers, verified followers, following) to a CSV file
    public static void writeResultsToCSV(String filePath, String link, List<String> followers, List<String> verifiedFollowers, List<String> following) {
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(filePath, true))) {
            // Extract username from the link (e.g., "https://x.com/Bitcoin" -> "Bitcoin")
            String username = link.substring(link.lastIndexOf("/") + 1);

            // Prepare the CSV row with the required columns
            String row = String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    username, // KOLs column now contains just the username
                    String.join(", ", followers),
                    String.join(", ", verifiedFollowers),
                    String.join(", ", following));

            // Write the row to the CSV file
            bw.write(row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveRanksToCSV(String filePath, Map<String, Double> ranks) {
        // Sort the ranks in descending order
        List<Map.Entry<String, Double>> sortedRanks = ranks.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header
            bw.write("Rank,Node,PageRank\n");

            // Write the sorted ranks to the file
            int rank = 1;
            for (Map.Entry<String, Double> entry : sortedRanks) {
                bw.write(rank + "," + entry.getKey() + "," + entry.getValue() + "\n");
                rank++;
            }

            System.out.println("Ranks successfully saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public static void writeToJSONFile(String filePath, JSONArray jsonArray) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {  // Corrected here
            fileWriter.write(jsonArray.toString(4)); // Pretty-print with indentation
            System.out.println("Data successfully saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + filePath);
            e.printStackTrace();
        }
    }
    
    public static void saveResultToJson(Object result, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            JSONObject jsonObject = new JSONObject(result);
            file.write(jsonObject.toString(4)); // Pretty print with an indentation of 4
            System.out.println("Results saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
