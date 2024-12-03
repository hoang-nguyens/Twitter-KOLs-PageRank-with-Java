package findkols;

import chromehandler.Driver;
import chromehandler.LoginManager;
import chromehandler.WaitUtils;
import java.util.List;

import org.openqa.selenium.WebDriver;

public class AccountSwitcherAndScraper {

    private List<String> usernames;
    private List<String> passwords;
    private List<String> keywords;
    private LoginManager loginManager;
    private WebDriver driver;
    private WaitUtils waitUtils;

    // Constructor to initialize lists of usernames, passwords, and keywords
    public AccountSwitcherAndScraper(List<String> usernames, List<String> passwords, List<String> keywords) {
        this.usernames = usernames;
        this.passwords = passwords;
        this.keywords = keywords;
        this.driver = Driver.getDriver();  // Single driver instance
        this.waitUtils = new WaitUtils(this.driver);  // Single WaitUtils instance
        this.loginManager = new LoginManager(this.driver, this.waitUtils);  // Single LoginManager instance
    }

    // Method to login, scrape, and switch accounts for each keyword
    public void loginAndScrape(int numLoops) {
        int accountIndex = 0;  // Start with the first account

        // Loop through each keyword
        for (String keyword : keywords) {
            // Fetch the current account details
            String username = usernames.get(accountIndex);
            String password = passwords.get(accountIndex);

            // Login with the current account
            loginManager.login(username, password);
            System.out.println("Logged in with: " + username);

            // Scrape users based on the current keyword
            HashtagUserScraper hashtagUserScraper = new HashtagUserScraper(this.driver, this.waitUtils);
            hashtagUserScraper.scrapeUsersFromHashtags(List.of(keyword), numLoops);  // Use custom numLoops

            // Logout to prepare for the next account
            loginManager.logout();
            System.out.println("Logged out of: " + username);

            // Move to the next account (cycling through)
            accountIndex = (accountIndex + 1) % usernames.size();
        }

        // Close the driver after all keywords have been processed
        Driver.closeDriver();
        System.out.println("Driver closed after completing all keywords.");
    }
}
