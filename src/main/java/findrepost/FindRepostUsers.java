package findrepost;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import chromehandler.Driver;
import chromehandler.LoginManager;
import chromehandler.TabManager;
import chromehandler.WaitUtils;
import utils.FileWriters;

public class FindRepostUsers {
	private static WebDriver driver;
	private static WaitUtils waitUtils;
	private static WebDriverWait wait;
	private LoginManager loginManager;
	
	private List<String> usernames;
	private List<String> passwords;
	
	public FindRepostUsers(List<String> usernames, List<String> passwords) {
		
		this.usernames = usernames;
		this.passwords = passwords;
		this.loginManager = new LoginManager(driver, waitUtils);
	}
	
	public void processRepostsWithAccountSwitching(String filePath) {
	    try {
	        // Step 1: Extract KOLs and tweet IDs
	        Map<String, List<String>> kolPostMap = getKolsWithPostIds(filePath);
	        Map<String, Map<String, Set<String>>> result = new HashMap<>();
	        int kolCount = 0;
	        int accountIndex = 0;

	        for (String kol : kolPostMap.keySet()) {
	            // Step 2: Switch accounts after every 10 KOLs
	            if (kolCount % 10 == 0) {
	                String username = usernames.get(accountIndex);
	                String password = passwords.get(accountIndex);

	                loginManager.login(username, password);
	                driver = loginManager.driver;
	                waitUtils = loginManager.waitUtils;
	                TabManager.driver = driver;

	                accountIndex = (accountIndex + 1) % usernames.size();
	            }

	            // Step 3: Process tweets for this KOL
	            Map<String, Set<String>> kolResults = new HashMap<>();
	            for (String postId : kolPostMap.get(kol)) {
	                String link = "https://x.com/" + kol + "/status/" + postId + "/retweets";
	                TabManager.openTab(link);

	                // Collect users who reposted this tweet
	                Set<String> repostUsers = searchRepost(link);
	                kolResults.put(postId, repostUsers);

	                // Close the tab to manage resource usage
	                TabManager.closeTabsExceptOriginal();
	            }

	            // Add the current KOL's results to the main result map
	            result.put(kol, kolResults);

	            // Save the result incrementally after processing each KOL
	            FileWriters.saveResultToJson(result, "repost.json");

	            kolCount++;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Ensure resources are cleaned up
	        Driver.closeDriver();
	    }
	}

	
	public Set<String> searchRepost(String link) {
        Set<String> users = new HashSet<>();
        try {
//        	loginManager.login(usernames.get(0), passwords.get(0));
//	        driver = loginManager.driver;
//	        waitUtils = loginManager.waitUtils;
//            // Open the link in a new tab
//	        
//	        TabManager.driver = driver;
            TabManager.openTab(link);

            // Scroll and load more content three times
            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 0; i < 2; i++) {
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000); // Wait for content to load
            }
            
            waitUtils.waitForVisibilityOfElement(By.cssSelector("[class=\"css-1jxf684 r-bcqeeo r-1ttztb7 r-qvutc0 r-poiln3\"]"));
            // Locate all span elements with the desired class
            List<WebElement> elements = driver.findElements(By.cssSelector("[class=\"css-1jxf684 r-bcqeeo r-1ttztb7 r-qvutc0 r-poiln3\"]"));

            // Extract usernames from the elements
            for (WebElement element : elements) {
                String username = element.getText();
                if (username.startsWith("@")) { // Ensure it is a username
                    users.add(username.substring(1)); // Remove the "@" character
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
	
	
	
	public static Map<String, List<String>> getKolsWithPostIds(String filePath) throws IOException {
        Map<String, List<String>> kolPostMap = new HashMap<>();

        // Read JSON file into a string
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        }

        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(jsonContent.toString());

        // Iterate through KOLs
        for (String kol : jsonObject.keySet()) {
            JSONObject kolDetails = jsonObject.getJSONObject(kol);

            // Collect all post IDs from retweetComments and tweetComments
            Set<String> postIds = new HashSet<>();
            // Comment this because only take tweets id
//            if (kolDetails.has("retweetComments")) {
//                postIds.addAll(kolDetails.getJSONObject("retweetComments").keySet());
//            }
            if (kolDetails.has("tweetComments")) {
                postIds.addAll(kolDetails.getJSONObject("tweetComments").keySet());
            }

            // Add KOL ID and associated post IDs to the map
            kolPostMap.put(kol, new ArrayList<>(postIds));
        }

        return kolPostMap;
    }
	
	
}
