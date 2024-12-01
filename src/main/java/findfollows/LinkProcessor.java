package findfollows;

import utils.FileWriters;

import java.util.List;

import chromehandler.TabManager;

public class LinkProcessor {

    private final FindFollowers findFollowers;

    // Constructor to initialize FindFollowers dependency
    public LinkProcessor() {
        this.findFollowers = new FindFollowers();
    }

    /**
     * Processes a range of links to fetch followers, verified followers, and following.
     * 
     * @param links      the list of user profile links
     * @param startIndex the starting index of the range
     * @param endIndex   the ending index of the range
     */
    public void processLinksRange(List<String> links, int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            String link = links.get(i).replace("\"", "").trim(); // Remove quotes and trim spaces
            System.out.println("Processing link: " + link);

            try {
                // Concatenate the URLs correctly
                String followersUrl = link + "/followers";
                String verifiedFollowersUrl = link + "/verified_followers";
                String followingUrl = link + "/following";

                // Debugging print statements to verify the URLs
                System.out.println("Followers URL: " + followersUrl);
                System.out.println("Verified Followers URL: " + verifiedFollowersUrl);
                System.out.println("Following URL: " + followingUrl);

                // Collect the data
                List<String> followers = findFollowers.findFollowerAndFollowing(followersUrl);
                List<String> verifiedFollowers = findFollowers.findFollowerAndFollowing(verifiedFollowersUrl);
                List<String> following = findFollowers.findFollowerAndFollowing(followingUrl);

                // Write the results to CSV
                FileWriters.writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);
            } catch (Exception e) {
                // Handle any error, log it, and proceed to the next link
                System.err.println("Error processing link: " + link);
                e.printStackTrace();
            } finally {
                // Close all tabs except the original one to save memory
                TabManager.closeTabsExceptOriginal();
            }
        }
    }
}
