import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class W06Exercise01 {
	public static void main(String[] argv) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./data/W06Exercise.db")) {
			// Create Artist Table
			Statement stmnt = conn.createStatement();
			stmnt.executeUpdate("DROP TABLE IF EXISTS Artists");
			stmnt.executeUpdate(
					"CREATE TABLE Artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist_name VARCHAR(50), label VARCHAR(50), chart_max INTEGER)");
			stmnt.close();

			// Create Album Table
			stmnt = conn.createStatement();
			stmnt.executeUpdate("DROP TABLE IF EXISTS Albums");
			stmnt.executeUpdate(
					"CREATE TABLE Albums (id INTEGER PRIMARY KEY AUTOINCREMENT, album_name VARCHAR(50), artist_id INTEGER, chart_max INTEGER, FOREIGN KEY (artist_id) REFERENCES Artists(id))");
			stmnt.close();

			// Populate the Artists
			insertArtist(conn, "Arcade Fire", "Firehouse Records", 7);
			insertArtist(conn, "Gus Dapperton", "Goofy Records", 56);

			// Print Artists with label
			printArtistsWithLabel(conn, "Goofy Records");
			printArtistsWithLabel(conn, "Firehouse Records");

			// Populate the Albums
			insertAlbum(conn, "The Suburbs", 1, 7);
			insertAlbum(conn, "Neon Bible", 1, 51);
			insertAlbum(conn, "Prune, You Talk Funny", 2, 56);
			insertAlbum(conn, "Where Polly People Go to Read", 2, 56);

			// Print Albums with Label
			printLabelAlbums(conn, "Firehouse Records");
			printLabelAlbums(conn, "Goofy Records");

			// // Delete unpopular Artists
			// deleteUnpopularArtists(conn, 10);
			// printArtistsWithLabel(conn, "Goofy Records");
			// printArtistsWithLabel(conn, "Firehouse Records");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void insertArtist(Connection conn, String name, String label, Integer chart_max) throws SQLException {
		Statement stmnt = conn.createStatement();
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO Artists (artist_name, label, chart_max) VALUES ('");
		query.append(name);
		query.append("', '");
		query.append(label);
		query.append("', ");
		query.append(chart_max);
		query.append(")");
		stmnt.executeUpdate(query.toString());
		stmnt.close();
	}

	public static void insertAlbum(Connection conn, String name, Integer artist_id, Integer chart_max)
			throws SQLException {
		Statement stmnt = conn.createStatement();
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO Albums (album_name, artist_id, chart_max) VALUES ('");
		query.append(name);
		query.append("', ");
		query.append(artist_id);
		query.append(", ");
		query.append(chart_max);
		query.append(")");
		stmnt.executeUpdate(query.toString());
		stmnt.close();
	}

	public static void printArtistsWithLabel(Connection conn, String label) throws SQLException {
		Statement stmnt = conn.createStatement();

		StringBuilder query = new StringBuilder();
		query.append("SELECT artist_name FROM Artists WHERE label='");
		query.append(label);
		query.append("'");

		ResultSet results = stmnt.executeQuery(query.toString());

		System.out.printf("Artists with %s:", label);
		while (results.next()) {
			System.out.println(" - " + results.getString("artist_name"));

		}
		stmnt.close();

	}

	public static void printLabelAlbums(Connection conn, String label) throws SQLException {
		Statement stmnt = conn.createStatement();

		StringBuilder query = new StringBuilder();
		query.append("SELECT Albums.album_name,Artists.artist_name FROM Albums,Artists WHERE Artists.label='");
		query.append(label);
		query.append("'");

		ResultSet results = stmnt.executeQuery(query.toString());
		System.out.printf("Albums from %s:", label);
		while (results.next()) {
			System.out.println(" - " + results.getString("album_name") + " by " + results.getString("artist_name"));

		}
		stmnt.close();

	}

	public static void deleteUnpopularArtists(Connection conn, Integer min_chart) throws SQLException {
		Statement stmnt = conn.createStatement();

		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM Artists WHERE chart_max>");
		query.append(min_chart);

		int numDeleted = stmnt.executeUpdate(query.toString());
		System.out.println("Deleted " + numDeleted + " artist/s");
		stmnt.close();

	}
}
