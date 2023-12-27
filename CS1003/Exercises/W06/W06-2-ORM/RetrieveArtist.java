import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

public class RetrieveArtist {
	private static final int NUM_ARGUMENTS = 1;

	public static Artist retrieve(int id) throws IOException, SQLException {
		Database db = new Database("./data/W06Exercise02.db");
		Dao<Artist, Integer> dao = db.getArtistDao();

		Artist artist = dao.queryForId(id);

		db.close();
		return artist;
	}

	public static void main(String[] argv) throws IOException, SQLException {
		if (argv.length != NUM_ARGUMENTS) {
			System.out.printf("Error: Invalid Num Of Arguments: %s / %s\n", argv.length, NUM_ARGUMENTS);
			System.out.println("Usage: java RetrieveArtist <Artist ID>");
			return;
		}

		// --- Set Arguments ---

		int id;
		try {
			id = Integer.valueOf(argv[0]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid Integer ID: " + e.getMessage());
			return;
		}

		System.out.println(retrieve(id));

	}
}
