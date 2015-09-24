import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class WordChecker {
	// First Node in tree
	Node root;
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	private class Node {
		private boolean isWord;
		private Node[] next = new Node[alphabet.length()];
	}
	
	/*
	 * Construct word tree from words in given file
	 */
	public WordChecker(String filename) {
		try {
			FileReader fileReader = new FileReader(new File(filename));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				add(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Add word to tree
	 */
	private void add(String word) {
		root = put(root, word, 0);
	}
	
	/*
	 * Create branch of tree from Node x corresponding to dth letter of word
	 */
	private Node put(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (d == word.length()) {
            x.isWord = true;
            return x;
        }
        char c = word.charAt(d);
        int i = alphabet.indexOf(c);
        x.next[i] = put(x.next[i], word, d+1);
        return x;
    }
	
	/*
	 * Look up string s in tree
	 * @returns 0 if s not in tree
	 * @returns 1 if s is a word
	 * @returns 2 if s is a prefix
	 */
	public int lookup(String s) {
		return lookup(root, s, 0);
	}
	
	/*
	 * Look down branch corresponding to dth letter of s from Node x
	 */
	public int lookup(Node x, String s, int d) {
		if (x == null) return 0;
		if (d == s.length()) {
			if (x.isWord) return 1;
			return 2;
		}
        char c = s.charAt(d);
        int i = alphabet.indexOf(c);
		return lookup(x.next[i], s, d+1);
	}
	
	/*
	 * Check whether s consists of letters from alphabet
	 */
	public boolean isInAlphabet(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!alphabet.contains(s.substring(i, i+1))) return false;
		}
		return true;
	}
	
	/*
	 * Unit testing
	 */
	public static void main(String[] args) {
		WordChecker dict = new WordChecker("dictionary.txt");
		System.out.println(dict.lookup("dog"));
		System.out.println(dict.lookup("asdf"));
		System.out.println(dict.lookup("penc"));
		System.out.println(dict.lookup("water"));
	}
}
