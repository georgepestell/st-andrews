import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import au.com.bytecode.opencsv.CSVParser;
import scala.Tuple2;


public class W09Exercise {
	private static CSVParser csvParser = new CSVParser(',', '"');
	private static SparkConf conf = new SparkConf().setAppName("W09Exercise").setMaster("local[*]");
	private static JavaSparkContext sc = new JavaSparkContext(conf);
	private static String filename;

	public static class AreaAvg {
		public String name;
		public double avgPrice;

		public AreaAvg(String name, Iterable<Area> prices) {
			this.name = name;

			LongAccumulator accum = sc.sc().longAccumulator();
			LongAccumulator count = sc.sc().longAccumulator();
			prices.forEach(x -> {
				count.add(1);
				accum.add(x.price);
			});

			if (!count.isZero())
				this.avgPrice = accum.value() / count.value();
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

	public static JavaRDD<String[]> setup() throws IOException {
		return sc.textFile("./data/pp-2020.csv").map((line) -> csvParser.parseLine(line));
	}

	public static void task1() throws Exception {
		System.out.println("--- TASK 1 - Cheapest Property in First 1000 Entries ---");
		Stream<String[]> stream = setup().take(1000).stream();

		System.out.println("Cheapest Property within the first 1000 entries:");
		stream.sorted((r1, r2) -> r2[1].compareTo(r1[1])).limit(1)
				.forEach(result -> System.out.println(Arrays.asList(result)));

		System.out.println();
	}

	public static void task2() throws Exception {
		System.out.println("--- TASK 2 - Avg Price in a Given Town ---");
		JavaRDD<String[]> stream = setup();

		String town = "Liverpool";

		double avgPrice = stream.filter(result -> {
			return result[11].equals(town.toUpperCase());
		}).mapToDouble(result -> Double.valueOf(result[1])).mean();

		if (avgPrice > 0)
			System.out.printf("Average property price in %s:\n£%.2f\n", town, avgPrice);
		else
			System.out.printf("Town: \"%s\" is not in the database\n", town);
		System.out.println();
	}

	public static void task3() throws Exception {
		System.out.println("--- TASK 3 - TOP 10 Most Expensive Areas (Avg) ---");
		JavaRDD<String[]> stream = setup();

		JavaPairRDD<String, Iterable<String[]>> areas = stream.groupBy(x -> x[11]);

		areas.map(x -> {
			LongAccumulator tot = sc.sc().longAccumulator();
			LongAccumulator count = sc.sc().longAccumulator();

			x._2.forEach(item -> {
				tot.add(Integer.parseInt(item[1]));
				count.add(1);
			});

			Tuple2<String, Double> out;
			if (count.isZero())

				out = new Tuple2<String, Double>(x._1, 0.0);
			else
				out = new Tuple2<String, Double>(x._1, (double) (tot.value() / count.value()));

			return out;

		}).sortBy(x -> x._2, false, 10).take(10).stream()
				.forEach(x -> System.out.printf("%s: £%.0f\n", x._1, x._2));

		System.out.println();
	}

	public static void task4() throws Exception {
		System.out.println("--- TASK 4 - Filtering by Year of Transaction ---");
		JavaRDD<String[]> stream = setup();

		SimpleDateFormat dformat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		SimpleDateFormat yearformat = new SimpleDateFormat("YYYY");

		String year = "2020";

		stream.filter(r -> {
			try {
				Date date = dformat.parse(r[2]);

				return year.compareTo(yearformat.format(date)) == 0;
			} catch (ParseException e) {
				return false;
			}

		}).groupBy(x -> x[11]).map(x -> {
			LongAccumulator tot = sc.sc().longAccumulator();
			LongAccumulator count = sc.sc().longAccumulator();

			x._2.forEach(item -> {
				tot.add(Integer.parseInt(item[1]));
				count.add(1);
			});

			Tuple2<String, Double> out;
			if (count.isZero())

				out = new Tuple2<String, Double>(x._1, 0.0);
			else
				out = new Tuple2<String, Double>(x._1, (double) (tot.value() / count.value()));

			return out;

		}).sortBy(x -> x._2, false, 10).take(10).stream()
				.forEach(x -> System.out.printf("%s: £%.0f\n", x._1, x._2));

		System.out.println();
	}

	public static void main(String args[]) throws Exception {
		sc.setLogLevel("OFF");
		// filename = args[0];
		task1();
		task2();
		task3();
		task4();
	}
}
