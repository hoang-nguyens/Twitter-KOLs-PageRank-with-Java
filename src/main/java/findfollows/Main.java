 package findfollows;

//import findfollows.FindFollowersAndSwitchAccount;
import java.util.List;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Initialize the list of usernames and passwords
    	List<String> usernames = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403", "phan315918");  // Add your actual usernames here
    	List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis", "stopthis");  // Add your actual passwords here

        // Specify the path to the CSV file containing the links
        String csvFilePath = "userlink/bitcoin.csv"; // Change this to the actual path of your CSV file

        // Create an instance of FindFollowersAndSwitchAccount
        FindFollowersAndSwitchAccount findFollowersAndSwitchAccount = new FindFollowersAndSwitchAccount(usernames, passwords);

        // Call the method to start scraping and switch accounts after processing a specified number of users
        int numLoops = 2; // Number of users to scrape before switching accounts
        findFollowersAndSwitchAccount.switchAccountAfterProcessingUsers(csvFilePath, numLoops);

        // Optional: Print message to confirm completion
        System.out.println("Completed scraping all links and switched accounts.");
    }
}
