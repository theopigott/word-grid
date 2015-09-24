import java.util.ArrayList;
import java.util.TreeSet;

public class Square {
	// Letter
	char c;
	// Next Square when traversing grid
	Square next = null;
	// Already visited square during search?
	boolean alreadyVisited = false;
	// Neighbours
	ArrayList<Square> neighbours;
	
	public Square(char c) {
		this.c = c;
		neighbours = new ArrayList<Square>();
	}
	
	/*
	 * Search for words starting with start, looking up in dict and adding valid words
	 * of length at least minLength to words
	 */
	public void search(String start, TreeSet<String> words, WordChecker dict, int minLength) {
		String s = start + c;
		int result = dict.lookup(s);
		if (result == 0) return;
		else {
			if (result == 1 && s.length() >= minLength) {
				words.add(s);
			}
			alreadyVisited = true;
			for (Square square : neighbours) {
				if (square.alreadyVisited) continue;
				square.search(s, words, dict, minLength);
			}
			alreadyVisited = false;
		}
	}
	
	@Override
	public String toString() {
		return Character.toString(c);
	}
	
	
}
