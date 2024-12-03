package findfollows;

import chromehandler.Driver;
import chromehandler.LoginManager;
import chromehandler.TabManager;
import chromehandler.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.FileWriters;
import utils.FileReader;

import java.util.ArrayList;
import java.util.List;

public class FindFollowersAndSwitchAccount {

    private static WebDriver driver;
//    private WebDriverWait wait;
    private static WaitUtils waitUtils;
    private final List<String> usernames; // List of usernames for accounts
    private final List<String> passwords; // List of passwords for accounts
    private LoginManager loginManager;
//    private final FindFollowers findFollowers;
    private int accountIndex = 0; // Start with the first account

    // Constructor to initialize dependencies and WebDriver
    public FindFollowersAndSwitchAccount(List<String> usernames, List<String> passwords) {
//        this.driver = Driver.getDriver();
//        this.wait = Driver.getWait();
        this.usernames = usernames;
        this.passwords = passwords;
        this.loginManager = new LoginManager(driver, waitUtils);
//        this.findFollowers = new FindFollowers();
    }
    
    /**
     * Switches accounts after processing a specified number of users.
     * 
     * @param csvFilePath the CSV file containing links
     * @param numLoops    the number of users to scrape per account before switching
     */
    public void switchAccountAfterProcessingUsers(String csvFilePath, int numLoops) {
        // Read the links from the CSV file
        List<String> links = FileReader.readLinksFromCSV(csvFilePath);

        int usersProcessed = 0; // Count how many users have been processed

        for (int i = 0; i < links.size(); i++) {
            String link = links.get(i).replace("\"", "").trim(); // Remove quotes and trim spaces
            System.out.println("Processing link: " + link);

            // Log in with the current account
            String username = usernames.get(accountIndex);
            String password = passwords.get(accountIndex);
            
            loginManager.login(username, password);
            driver = loginManager.driver;
            waitUtils = loginManager.waitUtils;
            
            System.out.println("Logged in with: " + username);

            try {
                // Concatenate the URLs correctly
                String followersUrl = link + "/followers";
                String verifiedFollowersUrl = link + "/verified_followers";
                String followingUrl = link + "/following";

                // Collect the data
                List<String> followers = findFollowerAndFollowing(followersUrl);
                List<String> verifiedFollowers = findFollowerAndFollowing(verifiedFollowersUrl);
                List<String> following = findFollowerAndFollowing(followingUrl);

                // Write the results to CSV
                FileWriters.writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);

                usersProcessed++;

                // Switch accounts after processing the specified number of users
                if (usersProcessed >= numLoops) {
                    // Logout current account
                    loginManager.logout();
                    System.out.println("Logged out of: " + username);

                    // Switch to the next account
                    accountIndex = (accountIndex + 1) % usernames.size(); // Loop back to the first account if necessary
                    usersProcessed = 0; // Reset counter
                }

            } catch (Exception e) {
                // Handle any error, log it, and proceed to the next link
                System.err.println("Error processing link: " + link);
                e.printStackTrace();
            } finally {
                // Close all tabs except the original one to save memory
                TabManager.closeTabsExceptOriginal();
            }
        }

        // After completing all the links, logout from the last account
        loginManager.logout();
        System.out.println("Logged out of last account.");
    }

    /**
     * Extracts the list of usernames (followers, verified followers, or following) from the given link.
     * 
     * @param link the URL of the page to scrape
     * @return a list of usernames
     */
    public List<String> findFollowerAndFollowing(String link) {
    	TabManager.driver = driver;
        TabManager.openTab(link); // Open the link in a new tab
        waitUtils.waitForVisibilityOfElement(By.cssSelector("[data-testid='cellInnerDiv']"));
        
        List<WebElement> followers = driver.findElements(By.cssSelector("[data-testid='cellInnerDiv']"));
        System.out.println("Total followers found: " + followers.size());

        List<String> usernames = new ArrayList<>();

        for (int i = 0; i < followers.size(); i++) {
            WebElement user = followers.get(i);

            try {
                // Extract the href from the user link
                String href = user.findElement(By.tagName("a")).getAttribute("href");

                // Extract the username from the href
                String username = href.substring(href.lastIndexOf('/') + 1); // Get everything after the last '/'
                usernames.add(username);
            } catch (NoSuchElementException e) {
                System.out.println("Element not found for user at index: " + i);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        return usernames; // Return the list of usernames
    }

    /**
     * Processes a range of links to fetch followers, verified followers, and following.
     * 
     * @param links      the list of user profile links
     * @param startIndex the starting index of the range
     * @param endIndex   the ending index of the range
     */
    public void processLinksRange(List<String> links, int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            String link = links.get(i).replace("\"", "").trim(); // Remove quotes and trim spaces
            System.out.println("Processing link: " + link);

            try {
                // Concatenate the URLs correctly
                String followersUrl = link + "/followers";
                String verifiedFollowersUrl = link + "/verified_followers";
                String followingUrl = link + "/following";

                // Collect the data
                List<String> followers = findFollowerAndFollowing(followersUrl);
                List<String> verifiedFollowers = findFollowerAndFollowing(verifiedFollowersUrl);
                List<String> following = findFollowerAndFollowing(followingUrl);

                // Write the results to CSV
                FileWriters.writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);
            } catch (Exception e) {
                // Handle any error, log it, and proceed to the next link
                System.err.println("Error processing link: " + link);
                e.printStackTrace();
            } finally {
                // Close all tabs except the original one to save memory
                TabManager.closeTabsExceptOriginal();
            }
        }
    }

    
}
