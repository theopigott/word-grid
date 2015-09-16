import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class WordGrid {
	// Dimension of grid (n rows, m columns)
	int n;
	int m;
	// Minimum length of words
	int minLength;
	// Array of grid squares
	Square[][] squares;
	// Object to look up words
	WordChecker dict;
	// Set of found words
	TreeSet<String> words;
	// Name of file containing letter distribution (null for equal weighting)
	String distFilename;
	
	/*
	 * Full constructor
	 */
	private WordGrid(int n, int m, int minLength, String dictFilename, String distFilename) {
		this.n = n;
		this.m = m;
		this.minLength = minLength;
		this.dict = new WordChecker(dictFilename);
		this.distFilename = distFilename;
	}
	
	/*
	 * Default constructors
	 */
	private WordGrid(int n, int m, int minLength, String dictFilename) {
		this(n, m, minLength, dictFilename, "distribution.txt");
	}
	
	private WordGrid(int n, int m, int minLength) {
		this(n, m, minLength, "SOWPODS.txt");
	}
	
	private WordGrid() {
		this(4, 4, 4);
	}
	
	/*
	 * Main program
	 */
	public static void main(String[] args) {
		WordGrid grid = new WordGrid();
		grid.initialise();
		grid.findWords();
		grid.play();
	}
	
	/*
	 * Console-based game play
	 */
	private void play() {
		if (words == null) findWords();
		
		Scanner in = new Scanner(System.in);
		
		HashSet<String> correctWords = new HashSet<String>();
		HashSet<String> incorrectWords = new HashSet<String>();
		
		int score = 0;
		String guess;
		while (true) {
			System.out.println("\nScore: " + score + "\n");
			printGrid();
			System.out.print("\nEnter word: ");
			guess = in.nextLine();
			if (guess.equals("")) break;
			else if (!dict.isInAlphabet(guess)) System.out.println("Please only enter letters!");
			else if (guess.length() < minLength) System.out.println("Too short!");
			else if (correctWords.contains(guess) || incorrectWords.contains(guess)) {
				System.out.println("Already guessed that!");
			}
			else if (words.contains(guess)) {
				int guessScore = wordScore(guess);
				score += wordScore(guess);
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
		System.out.println("Total possible: " + words.size());
		System.out.println("\nPress [Enter] to see missed words ...");
		in.nextLine();
		for (String word : words) {
			if (!correctWords.contains(word)) {
				System.out.println(word);
			}
		}
		
		in.close();
	}
	
	/*
	 * Print all found words in grid to console
	 */
	public void printWords() {
		if (words == null) findWords();
		for (String word : words) {
			System.out.println(word);
		}
	}
	
	/*
	 * Print grid to console
	 */
	public void printGrid() {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				System.out.print(squares[x][y] + " ");
			}
			System.out.println();
		}
	}
	
	/*
	 * Setup a grid with random letters
	 */
	private void initialise() {
		RandomLetter rl;
		if (distFilename != null) {
			rl = new RandomLetter(distFilename);
		}
		else {
			rl = new RandomLetter();
		}
		// Setup squares
		squares = new Square[n][m];
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				squares[x][y] = new Square(rl.next());
			}
		}
		// Set next and neighbours
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				Square sq = squares[x][y];
				// Set next square
				if (y < m-1) {
					sq.next = squares[x][y+1];
				}
				else if (x < n-1) {
					sq.next = squares[x+1][0];
				}
				else sq.next = null;
				
				// Set neighbours
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (i == 0 && j == 0) continue;
						int xN = x + i;
						int yN = y + j;
						if (xN >= 0 && xN < n && yN >= 0 && yN < m) {
							sq.neighbours.add(squares[xN][yN]);
						}
					}
				}
			}
		}
	}
	
	/*
	 * Calculate score for a word
	 */
	private int wordScore(String s) {
		int N = s.length();
		if (N <= 4) return 1;
		else if (N <= 5) return 2;
		else if (N <= 6) return 3;
		else if (N <= 7) return 5;
		else return 11;
	}
	
	/*
	 * Find all words in the grid
	 */
	private void findWords() {
		words = new TreeSet<String>();
		Square square = squares[0][0];
		do {
			square.search("", words, dict, minLength);
			square = square.next;
		} while (square != null);
	}

}
