import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class CS1003P2 {
	final static public int REQUIRED_ARGUMENTS = 3;

	public static void main(String[] argv) {

		// --- Setup ---
		// Check the user has given the correct number of arguments
		if (argv.length != REQUIRED_ARGUMENTS) {
			System.out.println("Expected " + REQUIRED_ARGUMENTS + " arguments, but got: " + argv.length);
			System.out.println("Usage: java CS1003P2 <source> <stopwords_file> <search term>");
			return;
		}

		// Create an OMDbAPI object which contains API functions
		OMDbAPI api = new OMDbAPI();

		// Source must either be "online" or "cache" - checked later
		String source = argv[0].toLowerCase();
		// Must be a valid file - checked later
		String stopwords_filepath = argv[1];
		// Replace the spaces with + for API calls
		String search_title = argv[2].replaceAll(" ", "+");

		// Read the stopword file to get Set of stopwords
		HashSet<String> stopwords;
		try {
			stopwords = api.getStopwords(stopwords_filepath);
		} catch (IOException e) {
			// Catch IOExcepiton when the stopword file doesn't exist
			System.out.println("File does not exist: " + stopwords_filepath);
			return;
		}

		// --- Try Calling the APIs ---
		try {

			// Calls the API to get list of movie ID's
			ArrayList<String> movies = api.getMovies(source, search_title);

			// If the API request returned 0 movies, tell the user and exit
			if (movies.size() == 0) {
				System.out.println("Error: search returned 0 results");
				return;
			}

			// totalFreq contains the non-stopword total word-frequency
			// from the movie plot strings
			HashMap<String, Integer> totalFreq = new HashMap<String, Integer>();

			// Loop through each movie ID and gets their Plot and Titles
			for (int i = 0; i < movies.size(); i++) {

				// Call the API for the movie info
				OMDbAPI.Movie movie = api.getInfo(source, movies.get(i));
				// --- Print Out Movie Information ---
				// Print the list number
				System.out.println("Movie " + (i + 1));

				// Prints out the title and plot - prints "N/A" if their null
				System.out.println("Title: " + ((movie.title == null) ? "N/A" : movie.title));
				System.out.println("Plot: " + ((movie.plot == null) ? "N/A" : movie.plot));

				// Separate with a newline
				System.out.println();

				// -- Add Plot Word Frequencies to Total ---
				if (movie.plot != null) {

					// Get the plot's filtered word list
					ArrayList<String> filtered_words = api.getWords(movie.plot, stopwords);

					// Add the plot's filtered words to the totalFreq
					for (String word : filtered_words) {
						if (totalFreq.containsKey(word)) {
							// Add 1 to the total count
							totalFreq.replace(word, totalFreq.get(word) + 1);
						} else {
							// Add the word to the list
							totalFreq.put(word, 1);
						}
					}
				}

			}

			// --- Print out The Most Frequent Words From the Plots ---
			System.out.println("Most frequent words in the plot fields:");

			// Prints the most frequent word in the totalFreq map
			// Ties are broken by sorting the words alphabetically
			// Top word removed from list after printing. Repeats 10 times
			for (int i = 0; i < 10; i++) {
				// Loop through the totalFreq list and check if the word has a
				// higher frequency than the current highest
				Map.Entry<String, Integer> topW = null;
				for (Map.Entry<String, Integer> word : totalFreq.entrySet()) {
					// If the current top word's value is less than this word,
					// set the top word to this word
					if (topW == null || word.getValue() > topW.getValue()) {
						topW = word;
					} else if (word.getValue() == topW.getValue()) {
						// If the current top word and this word's frequencies
						// are equal, sort alphabetically
						if (word.getKey().compareTo(topW.getKey()) <= 0) {
							topW = word;
						}
					}
				}

				// Make sure topW is set
				if (topW != null) {
					// Print the top word and it's frequency
					System.out.println("\t" + topW.getValue() + "\t" + topW.getKey());

					// Remove the top word from the list
					totalFreq.remove(topW.getKey());
				} else {
					return;
				}
			}
		} catch (IllegalArgumentException e) {
			// Catch IllegalArgumentException when the source isn't "online" or "cache"
			System.out.println("Invalid source: " + e.getMessage() + " - Must be either 'cache' or 'online'");
		} catch (UnsupportedEncodingException e) {
			// Catch UnsupportedEncodingExcepition when the title entered contains invalid characters
			System.out.println("Invalid URL String: " + e.getMessage());
		} catch (IOException e) {
			// Catch IOExcepiton when the API file doesn't exist
			System.out.println("Couldn't Access API" + e.getMessage());
			return;
		}

	}
}
