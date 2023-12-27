import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Reads the raw CSV data files and populates the database with information
 *
 */
public class PopulateDB {
	// All CSV files must have dates formatted in this way
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("YYYY-MM-DD");
	// This sets "|" as the file delimiter as commas are common in some values
	private static final String DELIMETER = "\\|";

	/**
	 * Parses a given CSV file, and attemps to add rows to the table given
	 * 
	 * @param db       The datatabase file
	 * @param table    The table to populate the values to
	 * @param filename The CSV filename containing data
	 *
	 */
	private static void parseCSV(Database db, String table, String filename) {
		// Try to open the CSV file and add objects accordingly
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {

			// --- Loop over each row and add data to database table associated ---
			String row;
			while ((row = reader.readLine()) != null) {
				// --- Splits the rows up into their columns ---
				String[] item = row.split(DELIMETER);
				try {
					switch (table) {
						case "Person": {
							Person person = new Person(item[0], DATEFORMAT.parse(item[1]));
							db.addPerson(person);
							break;
						}
						case "Movie": {
							Movie movie = new Movie(item[0], Integer.parseInt(item[1]),
									Integer.parseInt(item[2]), item[3], item[4]);
							db.addMovie(movie);
							break;
						}
						case "Review": {
							// Checks the Movie foreign key value exists in the Movie table
							Movie movie = db.getMovieDao().queryForId(Integer.parseInt(item[0]));
							Review review = new Review(movie, Integer.parseInt(item[1]), item[2]);
							db.addReview(review);
							break;
						}
						case "Award": {
							Award award = new Award(item[0], item[1]);
							db.addAward(award);
							break;
						}
						case "MoviePerson": {
							// Checks the Movie and Person values exist before adding the
							// relationships to the database
							Movie movie = db.getMovieDao().queryForId(Integer.valueOf(item[0]));
							Person person = db.getPersonDao().queryForId(Integer.valueOf(item[1]));

							MoviePerson mp = new MoviePerson(movie, person, item[2], item[3]);
							db.addMoviePerson(mp);
							break;
						}
						case "MovieAward": {
							// Checks the Award and Movie exist in the database
							Award award = db.getAwardDao().queryForId(Integer.valueOf(item[0]));
							Movie movie = db.getMovieDao().queryForId(Integer.valueOf(item[1]));

							MovieAward ma = new MovieAward(award, movie, Integer.valueOf(item[2]));
							db.addMovieAward(ma);
							break;
						}
						case "MoviePersonAward": {
							// Checks the Award and MoviePerson exist before adding values to the
							// database
							Award award = db.getAwardDao().queryForId(Integer.valueOf(item[0]));
							MoviePerson mp =
									db.getMoviePersonDao().queryForId(Integer.valueOf(item[1]));
							MoviePersonAward mpa =
									new MoviePersonAward(award, mp, Integer.valueOf(item[2]));
							db.addMoviePersonAward(mpa);
							break;
						}
						default: {
							System.out.println("Invalid Table Selection");
							return;
						}
					}
					// SQL Exceptions occurr when the values added are null when they shouldn't be
					// or the values already exist
				} catch (SQLException e) {
					System.out.println("Item values invalid / Already exist: " + e.getMessage());
					// ParseExceptions and NumberFormatExceptions occur when the values supplied
					// from the CSV files are the wrong type
				} catch (ParseException e) {
					System.out.println("Invalid value type: " + e.getMessage());
				} catch (NumberFormatException e) {
					System.out.println("Invalid value type: " + e.getMessage());
					// IndexOutOfBoundsExceptions occur when there are not enough columns supplied
					// in the CSV file
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Not enough columns for item: " + e.getMessage());
				}
			}

			// Throws IOException when the CSV file cannot be read
		} catch (IOException e) {
			System.out.println("Failed to Read File: " + e.getMessage());
			return;
		}
	}

	public static void main(String[] args) {
		// --- Attempts to open an existing database and populates it's tables with values from CSV
		// files
		try (Database db = new Database("./data/P3Database.db")) {
			// --- Loop through the raw data files and add items to the database
			parseCSV(db, "Person", "./data/raw/persons.csv");
			parseCSV(db, "Movie", "./data/raw/movies.csv");
			parseCSV(db, "Award", "./data/raw/awards.csv");
			parseCSV(db, "Review", "./data/raw/reviews.csv");
			parseCSV(db, "MoviePerson", "./data/raw/moviepersons.csv");
			parseCSV(db, "MovieAward", "./data/raw/movieawards.csv");
			parseCSV(db, "MoviePersonAward", "./data/raw/moviepersonawards.csv");
			System.out.println("OK");
		} catch (IOException e) {
			System.out.println("Failed to open database file: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Failed to connect to database: " + e.getMessage());
		}

	}
}
