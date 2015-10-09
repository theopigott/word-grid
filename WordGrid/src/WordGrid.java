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
	public WordGrid(int n, int m, int minLength, String dictFilename, String distFilename) {
		this.n = n;
		this.m = m;
		this.minLength = minLength;
		this.dict = new WordChecker(dictFilename);
		this.distFilename = distFilename;
	}
	
	/*
	 * Default constructors
	 */
	public WordGrid(int n, int m, int minLength, String dictFilename) {
		this(n, m, minLength, dictFilename, "distribution.txt");
	}
	
	public WordGrid(int n, int m, int minLength) {
		this(n, m, minLength, "SOWPODS.txt");
	}
	
	public WordGrid() {
		this(4, 4, 4);
	}
	
	/*
	 * Unit testing
	 */
	public static void main(String[] args) {

	}
	
	public void setSquares(String[][] letters) {
		squares = new Square[n][m];
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				squares[x][y] = new Square(letters[x][y]);
			}
		}
	}
	
	/*
	 * Randomize letters in grid
	 */
	public void randomize() {
		// Initialize random number generator
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
	}
	
	/*
	 * Setup relations in grid
	 */
	public void initialize() {
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
	public int wordScore(String s) {
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
	public void findWords() {
		words = new TreeSet<String>();
		Square square = squares[0][0];
		do {
			square.search("", words, dict, minLength);
			square = square.next;
		} while (square != null);
	}

}
