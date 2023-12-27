import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Creates the database file, the tables in the database, and it's schema. If the database already
 * exists, it will delete it and re-initialise it.
 * 
 * @author George Pestell
 *
 */
public class InitialiseDB {
	/** The location to create the database */
	private final static String DB_FILENAME = "./data/P3Database.db";
	/** The locatin of the schema file to read from */
	private final static String SCHEMA_FILENAME = "./data/schema.ddl";

	public static void main(String[] args) {
		try {
			// Delete the database if it exists
			Files.deleteIfExists(Paths.get(DB_FILENAME));

			// Create the database file and connection to it
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILENAME);

			// Read the schema DDL file in it's entirety
			String ddl = Files.readAllLines(Paths.get(SCHEMA_FILENAME)).stream()
					.collect(Collectors.joining());

			// Execute the schema DDL on the database
			Statement stmnt = conn.createStatement();
			stmnt.executeUpdate(ddl);

			// Print "OK" if no errors occur
			System.out.println("OK");

		} catch (SQLException e) {
			System.out.println("Error: Failed to execute DDL update" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: Failed to open file" + e.getMessage());
		}
	}
}
