package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileReader {

    // Reads a CSV file and returns the data as a List of rows
    public List<CSVRecord> readCSV(String csvFile) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = new ArrayList<>(csvParser.getRecords());
        csvParser.close();
        return records;
    }

    // Reads a JSON file and returns its content as a JSONObject
    public JSONObject readJSON(String jsonFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(jsonFile)));
        return new JSONObject(content);
    }

    // Method to read links from a CSV file
    public static List<String> readLinksFromCSV(String filePath) {
        List<String> links = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                links.add(line);  // Add each link to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }
    

    public static List<String[]> readCSV1(String filePath) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }
}