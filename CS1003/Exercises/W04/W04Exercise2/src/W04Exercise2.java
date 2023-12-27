import java.io.*;
import java.util.*;

public class W04Exercise2 {
	public static void main(String[] argv) {
		// Make sure the number of arguments is correct
		if (argv.length != 2) {
			System.out.println("Usage: java W04Exercise2 <truth_csv> <results_csv>");
			return;
		}
		try {
			HashMap<String, Integer> true_values = getValues(argv[0]);

			BufferedReader reader = new BufferedReader(new FileReader(argv[1]));

			String[] headers = reader.readLine().split("\t");

			String row;
			while (!(row = reader.readLine().trim()).isEmpty()) {
				String[] columns = row.split("\t");
				String query = columns[0];
				HashMap<String, Integer> queryValues = new HashMap<String, Integer>();
				for (int c = 1; c < columns.length; c++) {
					if (columns[c].isEmpty())
						queryValues.put(headers[c], 0);
					else
						queryValues.put(headers[c], Integer.valueOf(columns[c]));
				}
				// Calculate Precision and Recall
				double p = precision(true_values, queryValues, query);
				double r = recall(true_values, queryValues, query);
				double f = f1(p, r);
				System.out.printf(query + ": %.2f\n", f);

			}

			reader.close();

		} catch (IOException e) {
			// IO Exception
			System.out.println("IOException: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Invalid Integer Value: " + e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			System.out.println("No Query / Value Pair Given: " + e.getMessage());
		}

	}

	private static double precision(HashMap<String, Integer> true_values, HashMap<String, Integer> query_values,
			String query) {
		int tp = Math.min(true_values.get(query), query_values.get(query));
		int fp = 0;

		for (String result : query_values.keySet()) {
			if (!result.equals(query)) fp += query_values.get(result);
		}

		// Calculate Precision for Query
		if (tp + fp == 0)
			return 0;
		else
			return (double) tp / (double) (tp + fp);
	}

	private static double recall(HashMap<String, Integer> true_values, HashMap<String, Integer> query_values,
			String query) {
		int tp = Math.min(true_values.get(query), query_values.get(query));
		int fn = Math.max(0, true_values.get(query) - query_values.get(query));
		if (tp + fn == 0)
			return 0;
		else
			return (double) tp / (double) (tp + fn);
	}

	private static double f1(double p, double r) {
		if (p + r == 0)
			return 0;
		else
			return (p * r) / (p + r);
	}

	private static HashMap<String, Integer> getValues(String filename)
			throws IOException, FileNotFoundException, IndexOutOfBoundsException, NumberFormatException {

		HashMap<String, Integer> values = new HashMap<String, Integer>();

		// Setup reader for file
		BufferedReader reader = new BufferedReader(new FileReader(filename));

		String row;
		while ((row = reader.readLine()) != null) {
			String[] columns = row.split("\t");
			if (columns[1].isEmpty())
				values.put(columns[0], 0);
			else
				values.put(columns[0], Integer.valueOf(columns[1]));
		}

		reader.close();

		return values;
	}

}
