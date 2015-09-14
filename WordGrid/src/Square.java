import java.util.ArrayList;

public class Square {
	// Position
	int x;
	int y;
	// Letter
	char c;
	// Next Square when traversing grid
	Square next;
	// Visited Square in current word?
	boolean visited;
	// Neighbours
	ArrayList<Square> neighbours;
	
	public Square(int x, int y, char c) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.visited = false;
		neighbours = new ArrayList<Square>();
	}
	
	public void search(String root, ArrayList<String> words, WordChecker dict, int minLength) {
		String s = root + c;
		int result = dict.lookup(s);
		if (result == 0) return;
		else {
			if (result == 1 && s.length() >= minLength) {
				words.add(s);
			}
			visited = true;
			for (Square square : neighbours) {
				if (square.visited) continue;
				square.search(s, words, dict, minLength);
			}
			visited = false;
		}
	}
	
	@Override
	public String toString() {
		return Character.toString(c);
	}
	
	
}