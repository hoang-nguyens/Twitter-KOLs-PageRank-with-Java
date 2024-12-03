package findkols;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.FileWriters;
import chromehandler.WaitUtils;
import chromehandler.Driver;

import java.util.List;
import java.util.Set;

public class HashtagUserScraper {

    private WebDriver driver;
    private WaitUtils waitUtils;
    private HashtagSearcher hashtagSearcher;
    private FileWriters fileWriter;

    // Constructor - WebDriver, WaitUtils, HashtagSearcher, and FileWriter initialized via Driver class
    public HashtagUserScraper() {
        this.driver = Driver.getDriver(); // Get driver from Driver class
        this.waitUtils = new WaitUtils(driver); // Initialize WaitUtils with the driver
        this.hashtagSearcher = new HashtagSearcher(); // Initialize HashtagSearcher
        this.fileWriter = new FileWriters(); // Initialize FileWriter
    }
    
    public HashtagUserScraper(WebDriver driver, WaitUtils waitUtils) {
    	this.driver = driver;
    	this.waitUtils = waitUtils;
    	this.hashtagSearcher = new HashtagSearcher(driver, waitUtils);
    	this.fileWriter = new FileWriters();
    }

    // Method to scrape users from hashtags
    public void scrapeUsersFromHashtags(List<String> keywords, int numLoops) {
        for (String keyword : keywords) {
            String hashtag = "#" + keyword;
            scrapeUsers(hashtag, keyword, numLoops);
        }
    }

    // Method to scrape users for a specific hashtag
    private void scrapeUsers(String hashtag, String keyword, int numLoops) {
        try {
            // Search and navigate to the 'People' filter
            hashtagSearcher.searchAndFilterByHashtag(hashtag, keyword);

            // Wait for the page to load and ensure some users are visible (dynamic wait)
            waitUtils.waitForVisibilityOfElement(By.cssSelector("[data-testid='UserCell'][role='button']"));

            // Set to store unique user links
            Set<String> listLink = hashtagSearcher.getUserLinks(numLoops); // Pass the numLoops for custom iteration

            // Write the links to a CSV file
            fileWriter.appendLinksToCSV(listLink, keyword);
        } catch (Exception e) {
            System.err.println("Error during hashtag scraping: " + e.getMessage());
        }
    }
}
