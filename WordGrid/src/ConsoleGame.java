import java.util.HashSet;
import java.util.Scanner;

public class ConsoleGame {

	public static void main(String[] args) {
		WordGrid grid;
		if (args.length > 4) {
			grid = new WordGrid(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 
					Integer.parseInt(args[2]), args[3], args[4]);
		}
		else if (args.length > 3) {
			grid = new WordGrid(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 
					Integer.parseInt(args[2]), args[3]);
		}
		else if (args.length > 2) {
			grid = new WordGrid(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 
					Integer.parseInt(args[2]));
		}
		else {
			grid = new WordGrid();
		}
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Welcome to Word Grid!");
		
		while (true) {
			System.out.println("\nPlay or solve or quit?");
			String reponse = in.nextLine();
			if (reponse.equalsIgnoreCase("p") || reponse.equalsIgnoreCase("play")) {
				play(grid, in);
			}
			else if (reponse.equalsIgnoreCase("s") || reponse.equalsIgnoreCase("solve")) {
				solve(grid, in);
			}
			else break;
		}
		
		in.close();
	}

	/*
	 * Main game loop
	 */
	private static void play(WordGrid grid, Scanner in) {
		grid.randomize();
		grid.initialize();
		grid.findWords();
		
		HashSet<String> correctWords = new HashSet<String>();
		HashSet<String> incorrectWords = new HashSet<String>();
		
		int score = 0;
		String guess;
		while (true) {
			System.out.println("\nScore: " + score + "\n");
			printGrid(grid);
			System.out.print("\nEnter word: ");
			guess = in.nextLine();
			if (guess.equals("")) break;
			else if (!grid.dict.isInAlphabet(guess)) System.out.println("Please only enter letters!");
			else if (guess.length() < grid.minLength) System.out.println("Too short!");
			else if (correctWords.contains(guess) || incorrectWords.contains(guess)) {
				System.out.println("Already guessed that!");
			}
			else if (grid.words.contains(guess)) {
				int guessScore = grid.wordScore(guess);
				score += guessScore;
				System.out.println("Correct (+" + guessScore + ")!");
				correctWords.add(guess);
			}
			else {
				System.out.println("Incorrect!");
				incorrectWords.add(guess);
			}
		}
		
		System.out.println("\nGuessed: " + (correctWords.size() + incorrectWords.size()));
		System.out.println("Correct: " + correctWords.size());
		System.out.println("Final score: " + score);
		System.out.println("Total possible: " + grid.words.size());
		System.out.println("\nPress [Enter] to see missed words ...");
		in.nextLine();
		for (String word : grid.words) {
			if (!correctWords.contains(word)) {
				System.out.println(word);
			}
		}
	}
	
	/*
	 * Ask user for input of letters, display all words
	 */
	public static void solve(WordGrid grid, Scanner in) {		
		String[][] letters = new String[grid.n][grid.m];
		System.out.printf("\nGrid size: %d x %d\n", grid.n, grid.m);
		System.out.println("Enter letters, separated by spaces: ");
		for (int x = 0; x < grid.n; x++) {
			System.out.printf("Row %d: ", x+1);
			String[] line = in.nextLine().split(" ");
			// Check correct number of letters, otherwise repeat row
			if (line.length != grid.m) {
				System.out.printf("Must enter %d letters separated by space!\n", grid.m);
				x--;
				continue;
			}
			letters[x] = line;
		}
		
		grid.setSquares(letters);
		grid.initialize();
		grid.findWords();
		System.out.printf("\n\nFound %d words: \n", grid.words.size());
		printWords(grid);
	}
	
	/*
	 * Print all found words in grid to console
	 */
	public static void printWords(WordGrid grid) {
		if (grid.words == null) grid.findWords();
		for (String word : grid.words) {
			System.out.println(word);
		}
	}
	
	/*
	 * Print grid to console
	 */
	public static void printGrid(WordGrid grid) {
		for (int x = 0; x < grid.n; x++) {
			for (int y = 0; y < grid.m; y++) {
				System.out.print(String.format("%3s", grid.squares[x][y]));
			}
			System.out.println();
		}
	}
}
