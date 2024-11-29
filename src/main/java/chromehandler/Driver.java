package chromehandler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Driver {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // Method to initialize WebDriver and WebDriverWait
    public static void initializeDriver() {
        if (driver == null) {
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
    }

    // Method to get WebDriver
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    // Method to get WebDriverWait
    public static WebDriverWait getWait() {
        if (wait == null) {
            initializeDriver();
        }
        return wait;
    }

    // Method to close the driver
    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}