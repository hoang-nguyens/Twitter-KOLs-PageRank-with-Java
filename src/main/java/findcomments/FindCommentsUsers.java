package findcomments;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import chromehandler.Driver;
import chromehandler.LoginManager;
import chromehandler.TabManager;
import chromehandler.WaitUtils;
import utils.InfoHandling;

public class FindCommentsUsers {
	
	private static WebDriver driver;
    private static WaitUtils waitUtils;
    private static WebDriverWait wait;
    private LoginManager loginManager;
    
    private List<String> usernames;
    private List<String> passwords;
  
    // Constructor - WebDriver and WaitUtils are initialized via the Driver class
    public FindCommentsUsers(List<String> usernames, List<String> passwords) {

    	this.usernames = usernames;
    	this.passwords = passwords;
        this.loginManager = new LoginManager(driver, waitUtils);
    }
    
    public void processWithAccountSwitching(
    	    String filePath, 
    	    int startIndex, 
    	    int totalRows, 
    	    int batchSize
    	) {
    	    int accountIndex = 0;
    	    int currentIndex = startIndex;

    	    while (currentIndex < totalRows) {
    	        // Get the current account credentials
    	        String username = usernames.get(accountIndex);
    	        String password = passwords.get(accountIndex);

    	        // Initialize login manager and login
//    	        LoginManager loginManager = new LoginManager();
    	        
    	        loginManager.login(username, password);
    	        driver = loginManager.driver;
//    	        waitUtils = new WaitUtils(driver);
    	        waitUtils = loginManager.waitUtils;

    	        System.out.println("Logged in with: " + username);

    	        // Calculate endIndex for the current batch
    	        int endIndex = Math.min(currentIndex + batchSize - 1, totalRows - 1);

    	        // Process data for this batch
    	        try {
    	            FindCommentsUsers.processKOLsData(filePath, currentIndex, endIndex);
    	        } catch (Exception e) {
    	            System.err.println("Error processing data from index " + currentIndex + " to " + endIndex + ": " + e.getMessage());
    	        }

    	        // Logout and prepare for the next account
    	        loginManager.logout();
    	        driver.quit();
    	        System.out.println("Logged out of: " + username);

    	        // Move to the next account
    	        accountIndex = (accountIndex + 1) % usernames.size();

    	        // Update the current index for the next batch
    	        currentIndex += batchSize;
    	    }

    	    // Close the driver after all processing is complete
    	    Driver.closeDriver();
    	    System.out.println("Driver closed after processing all data.");
    	}

	
    public static void processKOLsData(String filePath, int startIndex, int endIndex) {
	    List<String[]> rows = utils.FileReader.readCSV1(filePath); // Assuming readCSV method exists to read the CSV data.
	    File jsonFile = new File("all.json");

	    JSONObject allData = new JSONObject();

	    // Load or initialize JSON data
	    if (jsonFile.exists()) {
	        try (FileReader reader = new FileReader(jsonFile)) {
	            StringBuilder content = new StringBuilder();
	            int ch;
	            while ((ch = reader.read()) != -1) {
	                content.append((char) ch);
	            }

	            // Parse existing JSON data if not empty
	            if (content.length() > 0) {
	                allData = new JSONObject(content.toString());
	            }
	        } catch (Exception e) {
	            System.err.println("Error reading or parsing all.json: " + e.getMessage());
	            allData = new JSONObject(); // Initialize as empty JSONObject if parsing fails
	        }
	    } else {
	        // If file doesn't exist, start with an empty JSON object
	        allData = new JSONObject();
	    }

	    // Process rows from startIndex to endIndex
	    for (int i = startIndex; i <= endIndex && i < rows.size(); i++) {
	        try {
	            String[] row = rows.get(i);
	            String kolUsername = row[0].replace("\"", "").trim();
	            System.out.println("Processing KOL: " + kolUsername);

	            // Fetch tweets, retweets, and followers
	            Map<String, Object> tweetData = findTweetsAndRetweetsWithLinks("https://x.com/" + kolUsername);

	            // Extract follower count as a string
	            String followers = (String) tweetData.get("followers");

	            // Maps to store processed data for this KOL
	            Map<String, String> repostOwnerMap = new HashMap<>();
	            Map<String, List<String>> tweetCommentsMap = new HashMap<>();
	            Map<String, List<String>> retweetCommentsMap = new HashMap<>();

	            // Process retweets
	            List<String> retweets = (List<String>) tweetData.get("retweets");
	            for (int j = 0; j < Math.min(retweets.size(), 5); j++) {
	                String retweetLink = retweets.get(j);
	                String retweetId = InfoHandling.extractPostId(retweetLink); // Extract post ID
	                Map<String, Set<String>> retweetComments = findTweetsAndRetweetsComment(retweetLink);

	                // Only add post owners who are not the KOL
	                Set<String> postOwners = retweetComments.get("PostOwner");
	                for (String owner : postOwners) {
	                    if (!owner.equals(kolUsername)) {
	                        repostOwnerMap.put(retweetId, owner);
	                    }
	                }

	                // Add comments for this retweet
	                Set<String> comments = retweetComments.get("UserComments");
	                retweetCommentsMap.put(retweetId, new ArrayList<>(comments));
	            }

	            // Process tweets
	            List<String> tweets = (List<String>) tweetData.get("tweets");
	            for (int j = 0; j < Math.min(tweets.size(), 5); j++) {
	                String tweetLink = tweets.get(j);
	                String tweetId = InfoHandling.extractPostId(tweetLink); // Extract post ID
	                Map<String, Set<String>> tweetComments = findTweetsAndRetweetsComment(tweetLink);

	                // Add comments for this tweet
	                Set<String> comments = tweetComments.get("UserComments");
	                tweetCommentsMap.put(tweetId, new ArrayList<>(comments));
	            }

	            // Create JSON object for this KOL
	            JSONObject kolData = new JSONObject();
	            kolData.put("followers", followers); // Add follower count
	            kolData.put("repostOwner", repostOwnerMap);
	            kolData.put("tweetComments", tweetCommentsMap);
	            kolData.put("retweetComments", retweetCommentsMap);

	            // Add to main JSON object
	            allData.put(kolUsername, kolData);

	        } catch (Exception e) {
	            System.err.println("Error processing KOL at row " + i + ": " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    // Write updated JSON data to the file
	    try (FileWriter writer = new FileWriter(jsonFile)) {
	        writer.write(allData.toString(4)); // Pretty-print JSON with 4-space indentation
	        System.out.println("Data successfully written to all.json");
	    } catch (IOException e) {
	        System.err.println("Error writing to all.json: " + e.getMessage());
	    }
	}
	
    
    public static Map<String, Set<String>> findTweetsAndRetweetsComment(String link) {
    	TabManager.driver = driver;
	    TabManager.openTab(link);

	    Set<String> userNamesSet = new HashSet<>();
	    String postOwner = null; // Variable to store the post owner's username

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    try {
	        Thread.sleep(2000); // Wait for new tweets to load (adjust as needed)
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Restore interrupted status
	    }

	    for (int i = 0; i < 1; i++) {
	        waitUtils.waitForVisibilityOfElement(By.cssSelector("[data-testid='User-Name']"));

	        List<WebElement> usersComment = driver.findElements(By.cssSelector("[data-testid='User-Name']"));

	        for (WebElement user : usersComment) {
	            try {
	                // Debugging: Print user element to inspect structure
	                System.out.println(user.getAttribute("innerHTML"));

	                // Wait for the <a> tag inside the user element to be visible
	                WebElement linkElement = user.findElement(By.cssSelector("a[href*='/']"));

	                // Ensure that the link element is visible
	                wait.until(ExpectedConditions.visibilityOf(linkElement));

	                String linkUser = linkElement.getAttribute("href");
	                String username = linkUser.substring(linkUser.lastIndexOf("/") + 1);

	                // If it's the first loop (i == 0) and postOwner is not set, save the post owner's username
	                if (i == 0 && postOwner == null) {
	                    postOwner = username;
	                    System.out.println("Post owner: " + postOwner);
	                }

	                // Only add the username to the set if it's not the post owner
	                if (!username.equals(postOwner)) {
	                    userNamesSet.add(username);  // Add to set to ensure uniqueness
	                }

	            } catch (Exception e) {
	                System.out.println("Error retrieving username: " + e.getMessage());
	            }
	        }

	        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        try {
	            Thread.sleep(2000); // Wait for new tweets to load
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Restore interrupted status
	        }
	    }

	    // Create a Map to return both postOwner and user comments
	    Map<String, Set<String>> result = new HashMap<>();
	    
	    
	    
	    // Add post owner first
	    if (postOwner != null) {
	        Set<String> postOwnerSet = new HashSet<>();
	        postOwnerSet.add(postOwner);
	        result.put("PostOwner", postOwnerSet); // Store post owner in the map
	    }

	    // Add all user comments
	    result.put("UserComments", userNamesSet);
	    TabManager.closeTabsExceptOriginal();

	    // Return the map with post owner and user comments
	    return result;
	}
    
    
    
	public static Map<String, Object> findTweetsAndRetweetsWithLinks(String link) {
		TabManager.driver = driver;
        TabManager.openTab(link);

        // Extract follower count as a string
        String followerCountStr = "";
        WebElement followerContainer = waitUtils.waitForVisibilityOfElement(By.xpath("//a[contains(., 'Followers')]"));
        WebElement followerCountElement = followerContainer.findElement(By.xpath(".//span[1]"));
        followerCountStr = followerCountElement.getText();

        // Convert follower count string to a numeric string
        String numericFollowerCountStr = String.valueOf(InfoHandling.parseNumber(followerCountStr));

        List<String> tweetLinks = new ArrayList<>();
        List<String> retweetLinks = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Extract the username from the provided link (e.g., https://x.com/elonmusk -> elonmusk)
        String username = link.substring(link.lastIndexOf("/") + 1);

        // Initial scroll to load some tweets
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try {
            Thread.sleep(2000); // Wait for new tweets to load (adjust as needed)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }

        // Loop to keep scrolling and collecting tweets
        for (int i = 0; i < 1; i++) { // Adjust the number of iterations to load more tweets
            waitUtils.waitForVisibilityOfElement(By.cssSelector("[data-testid='tweet']"));

            // Collect current tweets
            List<WebElement> tweetElements = driver.findElements(By.cssSelector("[data-testid='tweet']"));

            for (WebElement tweetElement : tweetElements) {
                // Extract the link from the tweet or retweet
                try {
                    WebElement linkElement = tweetElement.findElement(By.xpath(".//a[contains(@href, '/status/')]"));
                    String postLink = linkElement.getAttribute("href");

                    // Check if the post link contains the username (indicating it's from the correct user)
                    if (postLink.contains(username)) {
                        // If the link contains the username, it's a tweet
                        tweetLinks.add(postLink);
                    } else {
                        // If the link doesn't contain the username, it's a retweet
                        retweetLinks.add(postLink);
                    }
                } catch (NoSuchElementException e) {
                    // If no link is found, skip this element
                    continue;
                }
            }

            // Scroll down to load more tweets
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(2000); // Wait for new tweets to load
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }

        // Print collected tweet links count and follower count
        System.out.println("Total tweet links: " + tweetLinks.size());
        System.out.println("Total retweet links: " + retweetLinks.size());
        System.out.println("Follower count: " + numericFollowerCountStr);

        // Return a map containing tweet links, retweet links, and follower count
        Map<String, Object> tweetDataWithLinks = new HashMap<>();
        tweetDataWithLinks.put("tweets", tweetLinks);
        tweetDataWithLinks.put("retweets", retweetLinks);
        tweetDataWithLinks.put("followers", numericFollowerCountStr); // Store as a plain string

        return tweetDataWithLinks;
    }
}
