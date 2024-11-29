package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
}