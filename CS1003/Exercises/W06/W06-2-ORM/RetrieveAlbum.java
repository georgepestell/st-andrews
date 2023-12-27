import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

public class RetrieveAlbum {
	public static Album retrieve(int id) throws IOException, SQLException {
		Database db = new Database("./data/W06Exercise02.db");
		Dao<Album, Integer> dao = db.getAlbumDao();

		Album album = dao.queryForId(id);

		db.close();

		return album;

	}

	private static final int NUM_ARGUMENTS = 1;

	public static void main(String[] argv) throws IOException, SQLException {
		if (argv.length != NUM_ARGUMENTS) {
			System.out.printf("Error: Invalid Num Of Arguments: %s / %s\n", argv.length, NUM_ARGUMENTS);
			System.out.println("Usage: java RetrieveAlbum <Album ID>");
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
