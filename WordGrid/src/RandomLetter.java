import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomLetter {
	private Random rng = new Random();
	private String[] alphabet;
	private double[] cumDist;
	
	/*
	 * Create random letter generator with distribution specified in filename
	 * where lines are pairs of letter and weighting, e.g. "c, 4.6"
	 */
	public RandomLetter(String filename) {
		readDistribution(filename);
	}
	
	/*
	 * Create random letter generator which equal weighting for all letters
	 */
	public RandomLetter() {
		String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
		int N = alphabet.length;
		double sum = 0;
		for (int i = 0; i < N; i++) {
			sum = sum + 1.0 / N;
			cumDist[i] += sum;
		}
	}
	
	/*
	 * Get next random letter
	 */
	public String next() {
		// Generate probability in [0, 1]
		double p = rng.nextDouble();
		// Find 1st letter with cumulative probability greater than p
		for (int i = 0; i < alphabet.length - 1; i++) {
			if (p < cumDist[i]) return alphabet[i];
		}
		return alphabet[alphabet.length - 1];
	}
	
	/*
	 * Read letter weightings from text file
	 */
	private void readDistribution(String filename) {
		try {
			// Read lines from distribution file
			FileReader fileReader = new FileReader(new File(filename));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> lines = bufferedReader.lines().collect(Collectors.toList());
			// Initialise distribution and alphabet arrays
			int N = lines.size();
			double[] dist = new double[N];
			cumDist = new double[N];
			alphabet = new String[N];
			// Populate arrays
			double sum = 0;
			for (int i = 0; i < N; i++){
				String[] lineSplit = lines.get(i).split(",");
				alphabet[i] = lineSplit[0].toLowerCase();
				dist[i] = Double.parseDouble(lineSplit[1]);
				sum += dist[i];
				
			}
			// Calculate cumulative probabilities
			cumDist[0] = dist[0] / sum;
			for (int i = 1; i < alphabet.length; i++) {
				cumDist[i] = cumDist[i-1] + (dist[i] / sum);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Unit testing
	 */
	public static void main(String[] args) {
		RandomLetter rl = new RandomLetter("distribution.txt");
		System.out.println(Arrays.toString(rl.cumDist));
		for (int i = 0; i < 26; i++) {
			System.out.println(rl.next());
		}
	}
}
