import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class WordChecker {
	List<String> list;
	
	public WordChecker() {
        try {
            list = Files.readAllLines(Paths.get("dictionary.txt"), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	// This is horribly inefficient
	// Update implementation to trie?
	public int lookup(String s) {
		if (list.contains(s)) {
			return 1;
		}
		else {
			for (String word : list) {
				if (word.startsWith(s)) {
					return 2;
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		WordChecker dict = new WordChecker();
		System.out.println(dict.lookup("dog"));
		System.out.println(dict.lookup("penc"));
		System.out.println(dict.lookup("water"));
	}
}
