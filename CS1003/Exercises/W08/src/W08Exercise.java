import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

// To compile
// javac -cp commons-csv-1.8.jar: W08Exercise.java

// To run
// time java -cp commons-csv-1.8.jar: W08Exercise data/pp-complete.csv

public class W08Exercise {
	private static String filename;

	public static class AreaAvg {
		public String name;
		public double avgPrice;

		public AreaAvg(String name, List<Area> prices) {
			this.name = name;
			OptionalDouble avg = prices.stream().mapToInt(x -> x.price).average();

			if (avg.isPresent())
				this.avgPrice = avg.getAsDouble();
			else
				this.avgPrice = 0.0;

		}
	}
	private static class Area {
		public String name;
		public int price;

		public Area(String name, int price) {
			this.name = name;
			this.price = price;
		}
	}

	public static Stream<CSVRecord> setup() throws IOException {
		File inFile = new File(filename);

		// Create parser from CSV file
		CSVParser parser = CSVParser.parse(inFile, Charset.forName("UTF-8"), CSVFormat.RFC4180);

		// Stream CSV data
		Stream<CSVRecord> stream = StreamSupport.stream(parser.spliterator(), false);
		return stream;
	}

	public static void task1() throws Exception {
		System.out.println("--- TASK 1 - Cheapest Property in First 1000 Entries ---");
		Stream<CSVRecord> stream = setup();

		System.out.println("Cheapest Property within the first 1000 entries:");
		stream.limit(1000).sorted((r1, r2) -> r2.get(1).compareTo(r1.get(1))).limit(1)
				.forEach(result -> System.out.println(result));

		System.out.println();
	}

	public static void task2() throws Exception {
		System.out.println("--- TASK 2 - Avg Price in a Given Town ---");
		Stream<CSVRecord> stream = setup();

		String town = "Liverpool";

		OptionalDouble avgPrice = stream.filter(result -> {
			return result.get(11).equals(town.toUpperCase());
		}).mapToInt(result -> Integer.valueOf(result.get(1))).average();

		if (avgPrice.isEmpty()) {
			System.out.println("Town/City doesn't exist in data");
		} else {
			System.out.printf("Average property price in %s:\n£%.2f\n", town,
					avgPrice.getAsDouble());
		}
		System.out.println();
	}

	public static void task3() throws Exception {
		System.out.println("--- TASK 3 - TOP 10 Most Expensive Areas (Avg) ---");
		Stream<CSVRecord> stream = setup();

		Map<String, List<Area>> areas =
				stream.map(r -> new Area(r.get(13), Integer.parseInt(r.get(1))))
						.collect(Collectors.groupingBy(area -> area.name));

		areas.entrySet().stream().map(x -> new AreaAvg(x.getKey(), x.getValue()))
				.sorted((a, b) -> Double.compare(b.avgPrice, a.avgPrice)).limit(10)
				.forEach(x -> System.out.printf("%s: £%.2f\n", x.name, x.avgPrice));

		System.out.println();
	}

	public static void task4() throws Exception {
		System.out.println("--- TASK 4 - Filtering by Year of Transaction ---");
		Stream<CSVRecord> stream = setup();

		SimpleDateFormat dformat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		SimpleDateFormat yearformat = new SimpleDateFormat("YYYY");

		String year = "2020";


		Map<String, List<Area>> areas = stream.filter(r -> {
			try {
				Date date = dformat.parse(r.get(2));

				return year.compareTo(yearformat.format(date)) == 0;
			} catch (ParseException e) {
				return false;
			}

		}).map(r -> new Area(r.get(13), Integer.parseInt(r.get(1))))
				.collect(Collectors.groupingBy(area -> area.name));

		areas.entrySet().stream().map(x -> new AreaAvg(x.getKey(), x.getValue()))
				.sorted((a, b) -> Double.compare(b.avgPrice, a.avgPrice)).limit(10)
				.forEach(x -> System.out.printf("%s: £%.2f\n", x.name, x.avgPrice));

		System.out.println();
	}

	public static void main(String args[]) throws Exception {
		filename = args[0];
		task1();
		task2();
		task3();
		task4();
	}
}
