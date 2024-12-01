package findkols;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import chromehandler.WaitUtils;
import chromehandler.Driver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashtagSearcher {

    private WebDriver driver;
    private WaitUtils waitUtils;

    // Constructor - WebDriver and WaitUtils are initialized via the Driver class
    public HashtagSearcher() {
        this.driver = Driver.getDriver(); // Get driver from Driver class
        this.waitUtils = new WaitUtils(driver); // Initialize WaitUtils with the driver
    }

    // Method to search for a hashtag and click on 'People' filter
    public void searchAndFilterByHashtag(String hashtag, String keyword) {
        WebElement searchField = waitUtils.waitForVisibilityOfElement(By.cssSelector("[placeholder='Search']"));
        searchField.sendKeys(hashtag);
        searchField.submit();

        // Click on the 'People' filter link
        WebElement people = waitUtils.waitForVisibilityOfElement(By.cssSelector("a[href='/search?q=%23" + keyword + "&src=typed_query&f=user']"));
        people.click();
    }

    // Method to get user links by scrolling and collecting links with custom loop count
    public Set<String> getUserLinks(int numLoops) {
        Set<String> listLink = new HashSet<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < numLoops; i++) {
            try {
                Thread.sleep(2000);  // Adding a delay between scrolls
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Wait for the user elements to be present
            waitUtils.waitForVisibilityOfElement(By.cssSelector("[data-testid='UserCell'][role='button']"));

            // Get the list of user elements
            List<WebElement> users = driver.findElements(By.cssSelector("[data-testid='UserCell'][role='button']"));
            for (WebElement user : users) {
                String href = user.findElement(By.tagName("a")).getAttribute("href");
                listLink.add(href);
                System.out.println(href);
            }

            // Scroll down using JavaScript
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        }
        return listLink;
    }
}
