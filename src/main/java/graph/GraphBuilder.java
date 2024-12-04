package graph;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GraphBuilder {

    private final Graph graph;

    public GraphBuilder(Graph graph) {
        this.graph = graph;
    }

    // Processes CSV records to add vertices and edges to the graph
    public void buildGraphFromCSV(List<CSVRecord> records) {
        for (CSVRecord record : records) {
            String kol = record.get(0); // KOL
            String followers = record.get(1); // Followers
            String verifiedFollowers = record.get(2); // Verified Followers
            String following = record.get(3); // Following

            // Add the KOL node
            graph.addNode(kol);

            // Process and add followers
            String[] followerList = followers.split(",\\s*");
            for (String follower : followerList) {
                graph.addNode(follower);
                graph.addEdge(follower, kol);
            }

            // Process and add verified followers
            String[] verifiedFollowerList = verifiedFollowers.split(",\\s*");
            for (String verifiedFollower : verifiedFollowerList) {
                graph.addNode(verifiedFollower);
                graph.addEdge(verifiedFollower, kol);
            }

            // Process and add followings
            String[] followingList = following.split(",\\s*");
            for (String follow : followingList) {
                graph.addNode(follow);
                graph.addEdge(kol, follow);
            }
        }
    }


    // Processes JSON data to add vertices and edges to the graph
    public void buildGraphFromJSON(JSONObject jsonObject) {
        for (String kol : jsonObject.keySet()) {
            JSONObject kolData = jsonObject.getJSONObject(kol);

            // Process retweet comments
            JSONObject retweetComments = kolData.getJSONObject("retweetComments");
            for (String tweetId : retweetComments.keySet()) {
                JSONArray commentorsArray = retweetComments.getJSONArray(tweetId);
                for (int i = 0; i < commentorsArray.length(); i++) {
                    String commentor = commentorsArray.getString(i);
                    graph.addNode(commentor);
                    graph.addNode(kol);
                    graph.addEdge(commentor, tweetId);
                    graph.addEdge(kol, tweetId);
                    graph.addEdge(tweetId, kol);
                }
            }

            // Process tweet comments
            JSONObject tweetComments = kolData.getJSONObject("tweetComments");
            for (String tweetId : tweetComments.keySet()) {
                JSONArray commentorsArray = tweetComments.getJSONArray(tweetId);
                for (int i = 0; i < commentorsArray.length(); i++) {
                    String commentor = commentorsArray.getString(i);
                    graph.addNode(commentor);
                    graph.addNode(kol);
                    graph.addEdge(commentor, tweetId);
                    graph.addEdge(tweetId, kol);
                }
            }

            // Process reposts
            JSONObject repostOwner = kolData.getJSONObject("repostOwner");
            for (String tweetId : repostOwner.keySet()) {
                String repostedBy = repostOwner.getString(tweetId);
                graph.addNode(repostedBy);
                graph.addNode(kol);
                graph.addEdge(repostedBy, tweetId);
                graph.addEdge(kol, tweetId);
                graph.addEdge(tweetId, kol);
            }
        }
    }
}
