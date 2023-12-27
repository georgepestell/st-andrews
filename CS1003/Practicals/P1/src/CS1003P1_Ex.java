import java.util.*;
import java.io.IOException;
import java.nio.file.*;

public class CS1003P1_Ex {
	// Allow the user to use different sized n-grams
	private static final int NGRAM=3;
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
		HashSet<String> queryBigrams = getNGrams(query);	
		
		// Get Bigrams for each word in the wordlist
		for (String checkWord : words) {
			HashSet<String> checkWordNGrams = getNGrams(checkWord);
			float jaccardIndex = jaccardIndex(checkWordNGrams, queryBigrams);

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

	// Returns ngrams for a word
	public static HashSet<String> getNGrams(String string) {
		// Add splitting characters to string
		string = '^'+string+'$';

		// Create set of all ngrams in the string with splitting chars 
		HashSet<String> ngrams = new HashSet<String>();
		for (int i=0; i<string.length()-NGRAM+1; i++) {

			// For the value of NGRAM, add the next characters to get strings of size NGRAM
			String ng = "";
			for (int n=0; n<NGRAM;n++) {
				ng += string.charAt(i+n);
			}
			// Add the ngram to the set
			ngrams.add(ng);
		}

		// Return set of ngrams
		return ngrams;
	}

	// Returns the jaccard index of two sets of ngrams
	public static float jaccardIndex(HashSet<String> checkWordNGrams, 
			HashSet<String> queryNGrams) {
			
		// Get the intersection of the two ngrams sets
		HashSet<String> intersectionSet = (HashSet<String>) checkWordNGrams.clone(); 
		intersectionSet.retainAll(queryNGrams);
		
		// Get the union of the two ngrams sets
		HashSet<String> unionSet = (HashSet<String>) checkWordNGrams.clone();
		unionSet.addAll(queryNGrams);
		
		// Calculate the jaccard index - proportion of matching ngrams to all ngrams
		float jaccardIndex = (float) intersectionSet.size() / (float) unionSet.size();

		return jaccardIndex;
	}
}
