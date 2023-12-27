import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class CS1003P1 {
	public static void main(String[] args) {
		// Make sure the filename and query command-line arguments are given
		if (args.length != 2) {
			System.out.println("Expected 2 arguments, but got: " + args.length);
			System.out.println("Usage: java CS1003P1 <word_file> <query>");
			return;
		}

		// Get filename and query from command line arguments
		String filename = args[0];
		String query = args[1];

		// -- Try to open file and read
		Path filepath = Paths.get(filename);
		List<String> words;
		try {
			words = Files.readAllLines(filepath);

			// Make sure the file isn't empty
			if (words.size() == 0) {
				System.out.println("No words in file: " + filename);
				return;
			}

		}
		// Catch a read IO error for when the file doesn't exist
		catch (IOException e) {
			System.out.println("File does not exist: " + filename);
			return;
		}

		// Highest jaccard index score and word variables
		float score = 0;
		String result = "";

		// Get Bigrams for the query
		HashSet<String> queryBigrams = getBigrams(query);

		// Get Bigrams for each word in the wordlist
		for (String checkWord : words) {
			HashSet<String> checkWordBigrams = getBigrams(checkWord);
			float jaccardIndex = jaccardIndex(checkWordBigrams, queryBigrams);

			// Keep track of the highest scoring word and it's score
			if (jaccardIndex > score) {
				score = jaccardIndex;
				result = checkWord;
			}
		}

		// Print out the highest scoring word and it's Jaccard score.
		System.out.println("Result: " + result);
		System.out.println("Score: " + score);
	}

	// Returns bigrams for a word
	public static HashSet<String> getBigrams(String string) {
		// Add splitting characters to string
		string = '^' + string + '$';

		// Create set of all bigrams in the string with splitting chars
		HashSet<String> bigrams = new HashSet<String>();
		for (int i = 0; i < string.length() - 1; i++) {
			bigrams.add("" + string.charAt(i) + string.charAt(i + 1));
		}

		// Return set of bigrams
		return bigrams;
	}

	// Returns the jaccard index of two sets of bigrams
	public static float jaccardIndex(HashSet<String> checkWordBigrams,
			HashSet<String> queryBigrams) {

		// Get the intersection of the two bigrams sets
		HashSet<String> intersectionSet = (HashSet<String>) checkWordBigrams.clone();
		intersectionSet.retainAll(queryBigrams);

		// Get the union of the two bigrams sets
		HashSet<String> unionSet = (HashSet<String>) checkWordBigrams.clone();
		unionSet.addAll(queryBigrams);

		// Calculate the jaccard index - proportion of matching bigrams to all bigrams
		float jaccardIndex = (float) intersectionSet.size() / (float) unionSet.size();

		return jaccardIndex;
	}
}
