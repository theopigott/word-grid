import java.util.List;
import java.util.Random;


public class RandomLetter {
	Random rng;
	String alphabet = "abcdefghijklmnopqrstuvwxyz";
	int[] dist;
	
	public RandomLetter() {
		rng = new Random();
	}
	
	public RandomLetter(List<String> list) {
		int[] freq = new int[alphabet.length()];
		for (String word : list) {
			for (char c : word.toCharArray()) {
				freq[alphabet.indexOf(c)]++;
			}
		}
		dist = new int[alphabet.length()];
		dist[0] = freq[0];
		for (int i = 1; i < alphabet.length(); i++) {
			dist[i] = dist[i-1] + freq[i];
//			System.out.println(alphabet.charAt(i) + ": " + freq[i]);
		}
		
		rng = new Random();
	}
	
	public char next() {
//		return (char) (rng.nextInt(26) + 'a');
		int r = rng.nextInt(dist[alphabet.length()-1]) + 1;
		for (int i = 0; i < alphabet.length(); i++) {
			if (r <= dist[i]) {
				return alphabet.charAt(i);
			}
		}
		return ' ';
	}
}