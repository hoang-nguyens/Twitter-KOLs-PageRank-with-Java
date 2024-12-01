package chromehandler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginManager {

    private WebDriver driver;
    private WaitUtils waitUtils;

    // Constructor - WebDriver and WebDriverWait are provided by Driver class
    public LoginManager() {
        this.driver = Driver.getDriver();
        this.waitUtils = new WaitUtils(driver);
    }

    // Method to log in to Twitter
    public void login(String username, String password) {
        driver.get("https://twitter.com/login");

        // Enter username
        WebElement usernameField = waitUtils.waitForVisibilityOfElement(By.cssSelector("[name='text']"));
        usernameField.sendKeys(username);

        // Click Next button
        WebElement nextButton = waitUtils.waitForElementToBeClickable(By.cssSelector("[role='button'][type='button'][class=\"css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l\"]"));
        nextButton.click();

        // Enter password
        WebElement passwordField = waitUtils.waitForVisibilityOfElement(By.cssSelector("[name=\"password\"][spellcheck=\"true\"]"));
        passwordField.sendKeys(password);

        // Click Login button
        WebElement loginButton = waitUtils.waitForElementToBeClickable(By.cssSelector("[data-testid=\"LoginForm_Login_Button\"][type=\"button\"]"));
        loginButton.click();
    }
}
