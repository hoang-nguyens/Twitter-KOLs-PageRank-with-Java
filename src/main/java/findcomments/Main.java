package findcomments;

import java.util.Arrays;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		List<String> usernames = Arrays.asList("hihihahade31600", "HoangNg31600", "Hngy0403", "phan315918");  // Add your actual usernames here
		List<String> passwords = Arrays.asList("stopthis", "stopthis", "stopthis", "stopthis");  // Add your actual passwords here
		List<String> keywords = Arrays.asList("blockchain", "bitcoin", "crypto", "DeFi", "Ethereum", "NFT", "SmartContract", "Web3");
		
		FindCommentsUsers findCommentUsers = new FindCommentsUsers(usernames, passwords);
		
		findCommentUsers.processWithAccountSwitching("result.csv", 0, 100, 10);
		
	}
}
