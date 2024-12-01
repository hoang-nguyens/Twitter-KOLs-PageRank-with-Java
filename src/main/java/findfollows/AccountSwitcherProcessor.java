package findfollows;

import chromehandler.Driver;
import chromehandler.LoginManager;
import utils.FileReader;
import chromehandler.TabManager;

import java.util.List;

public class AccountSwitcherProcessor {
    private List<String> accounts;
    private List<String> passwords;
    private int currentAccountIndex;

    // Constructor to initialize account and password lists
    public AccountSwitcherProcessor(List<String> accounts, List<String> passwords) {
        this.accounts = accounts;
        this.passwords = passwords;
        this.currentAccountIndex = 0;
    }

    /**
     * Processes a single CSV file by scraping users in batches of 10,
     * switching accounts and resetting the driver after each batch.
     *
     * @param csvFilePath the path to the CSV file containing links
     */
    public void processLinksWithAccountSwitching(String csvFilePath) {
        List<String> links = FileReader.readLinksFromCSV(csvFilePath);

        for (int i = 0; i < links.size(); i += 10) {
            int endIndex = Math.min(i + 9, links.size() - 1);
            System.out.println("Processing links from index " + i + " to " + endIndex);

            // Initialize WebDriver and log in
            Driver.initializeDriver();
            loginWithCurrentAccount();

            // Process the current batch of links
            LinkProcessor linkProcessor = new LinkProcessor();
            linkProcessor.processLinksRange(links, i, endIndex);

            // Close the driver after processing the batch
            Driver.closeDriver();

            // Move to the next account
            switchAccount();
        }
    }
    
    /**
     * Processes multiple CSV files by calling the method to process each file individually.
     *
     * @param csvFiles a list of CSV file paths to process
     */
    public void processMultipleCSVFiles(List<String> csvFiles) {
        for (String csvFile : csvFiles) {
            System.out.println("Processing file: " + csvFile);
            processLinksWithAccountSwitching(csvFile);
        }
    }

    // Logs in with the current account using the WebDriver
    private void loginWithCurrentAccount() {
        String currentAccount = accounts.get(currentAccountIndex);
        String currentPassword = passwords.get(currentAccountIndex);

        LoginManager login = new LoginManager();
        login.login(currentAccount, currentPassword);
        System.out.println("Logged in with account: " + currentAccount);
    }

    // Switches to the next account in the list
    private void switchAccount() {
        currentAccountIndex = (currentAccountIndex + 1) % accounts.size();  // Cycle through accounts
    }
}
