package chromehandler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Driver {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // Private constructor to prevent instantiation
    private Driver() {}

    // Initialize WebDriver and WebDriverWait
    public static void initializeDriver() {
        if (driver == null) {  // Only initialize if driver is null
            driver = new ChromeDriver();  // Create a new ChromeDriver instance
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Set up wait
        }
    }

    // Get WebDriver instance
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();  // Ensure driver is initialized
        }
        return driver;
    }

    // Get WebDriverWait instance
    public static WebDriverWait getWait() {
        if (wait == null) {
            initializeDriver();  // Ensure wait is initialized with driver
        }
        return wait;
    }

    // Close the WebDriver instance
    public static void closeDriver() {
        if (driver != null) {
            driver.quit();  // Close the browser and end the session
            driver = null;  // Reset the driver reference for future use
            wait = null;    // Reset wait reference to avoid memory leaks
        }
    }
}
