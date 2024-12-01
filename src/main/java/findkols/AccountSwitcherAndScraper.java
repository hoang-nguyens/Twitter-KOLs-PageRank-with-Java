package findkols;

import chromehandler.Driver;
import chromehandler.LoginManager;
import chromehandler.WaitUtils;
import java.util.List;

public class AccountSwitcherAndScraper {

    private List<String> usernames;
    private List<String> passwords;
    private List<String> keywords;
    private LoginManager loginManager;

    // Constructor to initialize lists of usernames, passwords, and keywords
    public AccountSwitcherAndScraper(List<String> usernames, List<String> passwords, List<String> keywords) {
        this.usernames = usernames;
        this.passwords = passwords;
        this.keywords = keywords;
        this.loginManager = new LoginManager();  // Initialize LoginManager once
    }

    // Method to login and scrape for each keyword with a customizable number of loops
    public void loginAndScrape(int numLoops) {
        int accountIndex = 0;  // Start with the first account

        // Loop through each keyword
        for (String keyword : keywords) {
            // Initialize the driver at the start of each iteration
            Driver.initializeDriver();

            // Reinitialize WaitUtils with the new driver
            WaitUtils waitUtils = new WaitUtils(Driver.getDriver());

            // Fetch the current account details
            String username = usernames.get(accountIndex);
            String password = passwords.get(accountIndex);

            // Login with the current account
            loginManager.login(username, password);
            System.out.println("Logged in with: " + username);

            // Scrape users based on the current keyword
            HashtagUserScraper hashtagUserScraper = new HashtagUserScraper();
            hashtagUserScraper.scrapeUsersFromHashtags(List.of(keyword), numLoops);  // Use custom numLoops

            // Close the current driver session after scraping the current keyword
            Driver.closeDriver();
            System.out.println("Driver closed after scraping " + keyword);

            // Move to the next account (cycling through)
            accountIndex = (accountIndex + 1) % usernames.size();
        }
    }
}
