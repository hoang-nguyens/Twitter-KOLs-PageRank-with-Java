package findkols;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;

//import chromehandler.Driver;

public class Main {
	public static WebDriver driver;
	public static void main(String[] args) {
		
	List<String> usernames = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403", "phan315918");  // Add your actual usernames here
	List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis", "stopthis");  // Add your actual passwords here
	List<String> keywords = Arrays.asList("blockchain", "bitcoin", "crypto", "DeFi", "Ethereum", "NFT", "SmartContract", "Web3");  // Define the keywords/hashtags you want to scrape
      
      
	int numLoops = 2;

    // Initialize the HashtagScraperManager with test data
    HashtagScraperManager scraperManager = new HashtagScraperManager(usernames, passwords, keywords);

    // Start the login and scraping process
    scraperManager.loginAndScrape(numLoops);
		
		

	}

}
