package chromehandler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginManager {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor - WebDriver and WebDriverWait are provided from BrowserManager
    public LoginManager() {
        this.driver = Driver.getDriver();
        this.wait = Driver.getWait();
    }

    // Method to log in to Twitter
    public void login(String username, String password) {
        driver.get("https://twitter.com/login");

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='text']")));
        usernameField.sendKeys(username);

        WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='button'][type='button']")));
        nextButton.click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='password']")));
        passwordField.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='LoginForm_Login_Button']")));
        loginButton.click();
    }
}

