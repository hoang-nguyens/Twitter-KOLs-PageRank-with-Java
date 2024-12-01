package findfollows;

import chromehandler.TabManager;
import chromehandler.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class FindFollowers {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Constructor to initialize driver and wait
    public FindFollowers() {
        this.driver = Driver.getDriver();
        this.wait = Driver.getWait();
    }

    /**
     * Extracts the list of usernames (followers, verified followers, or following) from the given link.
     * 
     * @param link the URL of the page to scrape
     * @return a list of usernames
     */
    public List<String> findFollowerAndFollowing(String link) {
        TabManager.openTab(link); // Open the link in a new tab
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='cellInnerDiv']")));
        
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
}
