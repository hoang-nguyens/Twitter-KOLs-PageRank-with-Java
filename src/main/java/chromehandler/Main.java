package chromehandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import findcomments.FindCommentsUsers;
import findfollows.AccountSwitcherProcessor;
import findfollows.LinkProcessor;
import findkols.AccountSwitcherAndScraper;
import utils.FileReader;

public class Main {

	public static void main(String[] args) {
//		
//		List<String> usernames = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403", "phan315918");  // Add your actual usernames here
//        List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis", "stopthis");  // Add your actual passwords here
//        List<String> keywords = Arrays.asList("blockchain", "bitcoin", "crypto", "DeFi", "Ethereum", "NFT", "SmartContract", "Web3");  // Define the keywords/hashtags you want to scrape
//        
//        
//        // Define custom number of loops for scraping
//        int numLoops = 2;  // Customize the number of loops for scraping
//
//        // Initialize AccountSwitcherAndScraper
//        AccountSwitcherAndScraper accountSwitcherAndScraper = new AccountSwitcherAndScraper(usernames, passwords, keywords);
//        
//        // Start the login and scrape process
//        accountSwitcherAndScraper.loginAndScrape(numLoops);
		
		
		
//		LoginManager login = new LoginManager();
//		login.login("HoangNg31600", "stopthis");
//		
//		List<String> links = FileReader.readLinksFromCSV("D:\\OOP Project 2024.1\\oopfinal\\userlink\\blockchain.csv");
//		
//		
//		
//		int startIndex = 0;
//        int endIndex = 6;
//
//        LinkProcessor linkProcessor = new LinkProcessor();
//
//        // Process the links in the specified range
//        linkProcessor.processLinksRange(links, startIndex, endIndex);
//
//		List<String> accounts = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403");
//        List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis");
//        
//        // Step 2: Define the list of CSV file paths
//        List<String> csvFiles = Arrays.asList(
//            "D:\\OOP Project 2024.1\\oopfinal\\userlink\\blockchain.csv"
//        );
//
//        // Step 3: Create an instance of AccountSwitcherProcessor
//        AccountSwitcherProcessor processor = new AccountSwitcherProcessor(accounts, passwords);
//
//        // Step 4: Call the method to process multiple CSV files
//        processor.processMultipleCSVFiles(csvFiles);
		
		
		int startIndex = 638;
        int batchSize = 3; // Number of KOLs to process per iteration
        int totalIterations = 50;
        int accountsCount = 5; // Total accounts available for login
        LoginManager login = new LoginManager();
        for (int i = 0; i < totalIterations; i++) {
            int accountIndex = i % accountsCount; // Cycle through accounts
            try {
                // Login using the appropriate account index (implement login logic in Driver)
                login.login("hihihahade31600", "stopthis"); // Assuming login(int accountIndex) is defined in the Driver class

                // Create an instance of FindCommentsUsers to process the data
                FindCommentsUsers findComments = new FindCommentsUsers();
                findComments.processKOLsData(
                        "D:\\OOP Project 2024.1\\projecttest\\result.csv",
                        startIndex,
                        startIndex + batchSize - 1
                );

                // Update the start index for the next batch
                startIndex += batchSize;
                System.out.println("Processed up to startIndex: " + startIndex);
            } catch (Exception e) {
                System.err.println("Error during iteration " + i + ": " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Ensure the driver is closed after each iteration to free resources
                if (Driver.getDriver() != null) {
                    try {
                        Driver.getDriver().quit();
                    } catch (Exception e) {
                        System.err.println("Error closing driver: " + e.getMessage());
                    }
                }
            }
        }
		
	}

}
