import java.io.*;
import java.util.*;

public class IOReader {
	private ArrayList<String> text = new ArrayList<String>();

	public IOReader(String filename) throws EmptyFileException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			// Check if the file is empty and throw EmptyFileException
			String line = reader.readLine();
			if (line == null)
				throw new EmptyFileException(filename + " is an empty file");

			// Save lines to array
			do {
				text.add(line);
			} while ((line = reader.readLine()) != null);

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("File not found: " + e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("Couldn't close reader: " + e.getMessage());
				}
			}
		}
	}

	public void printWordCount() {
		int wordCount = 0;
		for (String line : text) {
			String[] words = line.trim().replaceAll("[^a-zA-Z ]", "").split(" ");
			for (String word : words) {
				if (!word.trim().equals(""))
					wordCount++;
			}
		}
		System.out.println("Total number of words in file: " + wordCount);
	}

	public void printWordCount(String searchTerm) {
		int wordCount = 0;

		for (String line : text) {

			String[] words = line.replaceAll("[^a-zA-Z ]", "").split(" ");
			for (String word : words) {
				if (word.equals(searchTerm))
					wordCount++;

			}
		}

		System.out.println(searchTerm + ": " + wordCount);
	}

}
