import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

public class OMDbAPI {
	// OMDb API Key
	private final String API_KEY = "6fa3f1e6";

	// Class to store the movie's info
	public class Movie {
		public String movieID;
		public String title;
		public String plot;

		public Movie(String movieID) {
			this.movieID = movieID;
		}
	}

	// Get JSON result from the ONLINE OMDbAPI
	private JsonParser callAPI(String request) throws IOException {
		request = request + "&apiKey=" + this.API_KEY;
		URL url = new URL(request);
		URLConnection connection = url.openConnection();

		JsonParserFactory factory = Json.createParserFactory(null);
		JsonParser parser = factory.createParser(connection.getInputStream());
		return parser;

	}

	// Get JSON result from the CACHE OMDbAPI
	private JsonParser callCache(String request) throws IOException {
		String filepath = "/cs/studres/CS1003/Practicals/P2/Tests/cachedir/" + URLEncoder.encode(request, "UTF-8")
				+ ".json";
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		JsonParserFactory factory = Json.createParserFactory(null);
		JsonParser parser = factory.createParser(reader);
		return parser;

	}

	// getMovies(source, search_title);
	// Title search the online or cache API to get a list of movie ID's
	public ArrayList<String> getMovies(String source, String search_title)
			throws IllegalArgumentException, UnsupportedEncodingException, IOException {

		// --- Setup ---
		// List of movie ID's which will be filled from the API
		ArrayList<String> movies = new ArrayList<String>();

		// Create the base API search request
		String request = "https://www.omdbapi.com/?r=json&s=" + search_title;

		// --- Call API ---
		// Check source and call online or cache API.
		JsonParser parser;
		switch (source) {
			case "cache": {
				parser = callCache(request);
				break;
			}
			case "online": {
				parser = callAPI(request);
				break;
			}
			default:
				throw new IllegalArgumentException(source);
		}

		// --- Parse JSON Response ---
		// Loops through each line, looking for the imdbID KEY_NAME
		// and its' associated VALUE_STRING
		String keyName = null;
		while (parser.hasNext()) {
			JsonParser.Event e = parser.next();
			switch (e) {

				case KEY_NAME:
					// Store most recent key.
					// The latest key will determine what the VALUE_STRING means
					keyName = parser.getString();
					break;

				case VALUE_STRING: {
					// Add ID to movies list if latest key was imdbID
					if (keyName.contains("imdbID")) {
						movies.add(parser.getString());
					}

					// Reset keyName as keys can only have one Value
					keyName = null;
					break;

				}
				default:
					break;

			}
		}

		// --- Return Movie IDs List ---
		return movies;

	}

	// ID search the online or cache API to get a specific movie's plot and title
	public Movie getInfo(String source, String id) throws IOException {

		// --- Setup ---
		// Create the movie class which gets filled by API response
		Movie movie = new Movie(id);

		// Create the base API search request
		String request = "https://www.omdbapi.com/?r=json&i=" + id;

		// --- Call API ---
		// Check source and call online or cache API.
		JsonParser parser;
		switch (source) {
			case "cache": {
				parser = callCache(request);
				break;
			}
			case "online": {
				parser = callAPI(request);
				break;
			}
			default:
				throw new IllegalArgumentException(source);
		}

		// --- Parse JSON Response ---
		// Loops through each line, looking for the "Plot" and "Title" keys
		// and their associated VALUE_STRING
		String keyName = null;
		while (parser.hasNext()) {
			JsonParser.Event e = parser.next();
			switch (e) {
				case KEY_NAME:

					keyName = parser.getString();
					break;
				case VALUE_STRING: {
					// Store the Plot and Title if they exist
					if (keyName.contains("Plot")) {
						String plot = parser.getString();
						if (!plot.trim().equals("N/A")) movie.plot = plot;
					} else if (keyName.contains("Title")) {
						String title = parser.getString();
						if (!title.trim().equals("N/A")) movie.title = title;
					}

					// Reset keyName as keys can only have one Value
					keyName = null;
					break;
				}
				default:
					break;

			}
		}

		// --- Return Movie Info ---
		return movie;
	}

	// getStopwords(filepath);
	// Read a stopwords file to get a hash set of stopwords
	// Each line is interpreted as a word
	public HashSet<String> getStopwords(String filepath) throws IOException {
		// --- Setup ---
		// HashSets mean if a repeated stopword is added, it only appears once
		// This is because it's pointless to check for the same word in text twice
		HashSet<String> stopwords = new HashSet<String>();

		// --- Read File ---
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {

			// Loops through the file and read each line as a word
			String word;
			while ((word = reader.readLine()) != null) {

				// Normalizes the stopword - trims whitespace
				// and makes it lowercase
				word = word.toLowerCase().trim();
				stopwords.add(word);

			}

			// --- Return Stopwords HashSet ---
			return stopwords;
		}
	}

	// getWords(plot, stopwords)
	// Process the plot text of a movie: normalize and remove stopwords
	// Returns a list of each word remaining
	public ArrayList<String> getWords(String plot, HashSet<String> stopwords) {

		// --- Setup / Normalization---
		ArrayList<String> filtered_words = new ArrayList<String>();

		// Replaces all the non-alphanumeric text with spaces
		// and makes it lowercase
		plot = plot.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();

		// Creates an array with each word from the plot
		String[] plot_words = plot.split("[ \t\n\r]");

		// --- Stopword Removal ---
		for (String word : plot_words) {
			// Removes empty words and stopwords
			if (!stopwords.contains(word) && !word.trim().isEmpty()) {
				filtered_words.add(word);
			}
		}

		// --- Return Filtered Plot Words List ---
		return filtered_words;
	}
}
