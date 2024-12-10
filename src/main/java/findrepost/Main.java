package findrepost;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
    	// Step 1: Define usernames and passwords
    	List<String> usernames = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403", "phan315918");  // Add your actual usernames here
		List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis", "stopthis");  // Add your actual passwords here

        // Step 2: Initialize FindRepostUsers instance
        FindRepostUsers findRepostUsers = new FindRepostUsers(usernames, passwords);

        // Step 3: Define the file path for KOLs and tweet IDs
        String filePath = "D:\\OOP Project 2024.1\\projecttest\\all.json"; // Replace with the actual file path

        // Step 4: Call processRepostsWithAccountSwitching method
        findRepostUsers.processRepostsWithAccountSwitching(filePath);

        // Output should save the results to "repost.json"
        System.out.println("Processing complete. Check 'repost.json' for results.");
    }
}
