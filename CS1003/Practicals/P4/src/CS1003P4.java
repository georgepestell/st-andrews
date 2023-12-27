import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;


/**
 * Fuzzy searches for an input string in files from a given directory.
 */
public class CS1003P4 {
	private static double threshold;
	private static String[] query;
	private static HashSet<String> query_bigrams;

	/**
	 * Calculates Jaccard Index from two sets of bigrams
	 * 
	 * @param bigrams       Set of bigrams to check
	 * @param query_bigrams Set of bigrams from the search
	 * @return Jaccard Index of input bigrams compared to the query_bigrams
	 *
	 */

	@SuppressWarnings("unchecked")
	public static double jaccardIndex(HashSet<String> bigrams, HashSet<String> query_bigrams) {

		// --- Union and Intersection of the two sets of bigrams ---
		HashSet<String> intersection = (HashSet<String>) bigrams.clone();
		intersection.retainAll(query_bigrams);

		HashSet<String> union = (HashSet<String>) bigrams.clone();
		union.addAll(query_bigrams);

		// Calculate the jaccard index - proportion of matching bigrams to all bigrams
		double jaccardIndex = (double) intersection.size() / (double) union.size();

		return jaccardIndex;
	}

	/**
	 * Gets bigrams set with top and tail optimization
	 * 
	 * @param text Input text
	 * @return A Set containing unique bigrams
	 *
	 */
	public static HashSet<String> getBigrams(String text) {
		HashSet<String> bigrams = new HashSet<String>();

		// Add top and tail characters (Improves Accuracy)
		// text = '^' + text + '$';

		// Gets all character pairs
		for (int i = 0; i < text.length() - 1; i++) {
			bigrams.add("" + text.charAt(i) + text.charAt(i + 1));
		}

		return bigrams;
	}

	/**
	 *
	 * String cleanup and word splitter
	 * 
	 * @param text Input text
	 * @return An ordered list of words
	 * 
	 */
	public static String[] clean(String text) {
		// Cleanup and Split
		String[] words = text.toLowerCase().replaceAll("[^a-z0-9]", " ").replaceAll("[ ]+", " ")
				.split("[ \t\n\r]");

		// Split spaces, tabs, and new lines
		return words;

	}

	/**
	 * Read and parses a given file
	 * 
	 * @param file File object to read
	 *
	 */
	private static void searchFile(String[] contents) {

		// Loop over word size chunks
		for (int i = 0; i <= (contents.length - query.length); i++) {

			// Get chunk of words from contents
			String chunk = "";
			for (int k = 0; k < query.length; k++)
				chunk += contents[i + k] + " ";

			// Clean trailing whitespace
			chunk = chunk.trim();

			// Check similarity of chunk to query
			double jaccard = jaccardIndex(getBigrams(chunk), query_bigrams);
			if (jaccard >= threshold)
				System.out.println(chunk.trim());


		}

	}

	/**
	 * Java program to perfom a fuzzy string search across a number of files.
	 * 
	 * @param args (search-directory) (query) (similarity-threshold)
	 *
	 */
	public static void main(String[] args) {

		// Check the number of arguments
		if (args.length != 3) {
			System.out.printf("ERROR: %s / 3 parameters given\n"
					+ "Usage: java CS1003P4 search-directory \"query\" similarity-threshold\n",
					args.length);
			return;
		}


		// --- Parse Command-Line Arguments ---

		// Setup

		query = clean(args[1]);

		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("CS1003P4");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		jsc.setLogLevel("Off");

		JavaPairRDD<String, String> files;

		// Check query isn't empty
		if (query.length == 0) {
			System.out.println("Invalid Search Term: Must Not Be Blank");
			return;
		}


		// Parse the threshold value
		try {
			// Check a number is given
			threshold = Double.valueOf(args[2]);

			// Check value is between 0 and 1
			if (threshold > 1 || threshold < 0)
				throw new NumberFormatException();

		} catch (NumberFormatException e) {
			System.out.printf("Invalid Similarity Threshold: '%s'\nMust be a number from 0 to 1\n",
					args[2]);
			return;
		}

		// Check directory exists
		if (!Files.exists(Paths.get(args[0]))) {
			System.out.printf("Directory doesn't exist: \"%s\"\n", args[0]);
			return;
		}

		// Check directory contains files
		files = jsc.wholeTextFiles(args[0]);
		if (files.isEmpty()) {
			return;
		}

		// Get query bigrams
		query_bigrams = getBigrams(String.join(" ", query));

		// Search over each file
		files.mapValues(contents -> clean(contents)).foreach(f -> searchFile(f._2()));

	}

}

