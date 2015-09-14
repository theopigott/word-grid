import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class WordGrid {
	int n;
	int m;
	Square[][] squares;
	WordChecker dict;
	int minLength;
	
	private WordGrid(int n, int m, int minLength) {
		this.n = n;
		this.m = m;
		this.minLength = minLength;
		this.dict = new WordChecker();
	}
	
	public static void main(String[] args) {
		WordGrid grid = new WordGrid(4, 4, 4);
		grid.initialise();
		
		ArrayList<String> words = grid.findWords();
//		Collections.sort(words);
		System.out.println("\n\nFound words:");
		for (String word : words) {
			System.out.println(word);
		}
		
		grid.play();
		

	}
	
	private void play() {
		ArrayList<String> words = findWords();
		Scanner in = new Scanner(System.in);
		
		ArrayList<String> correctWords = new ArrayList<String>();
		ArrayList<String> incorrectWords = new ArrayList<String>();
		
		String guess;
		while (true) {
			System.out.println();
			print();
			System.out.print("\nEnter word: ");
			guess = in.nextLine();
			if (guess.equals("")) break;
			else if (correctWords.contains(guess) || incorrectWords.contains(guess)) {
				System.out.println("Already guessed that!");
			}
			else if (words.contains(guess)) {
				System.out.println("Correct!");
				correctWords.add(guess);
			}
			else {
				System.out.println("Incorrect!");
				incorrectWords.add(guess);
			}
		}
		
		System.out.println("\nGuessed: " + (correctWords.size() + incorrectWords.size()));
		System.out.println("Correct: " + correctWords.size());
		System.out.println("Total possible: " + words.size());
		System.out.println("\nYou missed: ");
		for (String word : words) {
			if (!correctWords.contains(word)) {
				System.out.println(word);
			}
		}
		
		in.close();
	}
	
	public void print() {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				System.out.print(squares[x][y] + " ");
			}
			System.out.println();
		}
	}
	
	private void initialise() {
		RandomLetter rl = new RandomLetter(dict.list);
		// Setup squares
		squares = new Square[n][m];
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				squares[x][y] = new Square(x, y, rl.next());
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
	
	private ArrayList<String> findWords() {
		ArrayList<String> words = new ArrayList<String>();
		Square square = squares[0][0];
		do {
			square.search("", words, dict, minLength);
			square = square.next;
		} while (square != null);
		return words;
	}

}
