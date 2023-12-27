import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

public class StoreAlbum {
	private static final int NUM_ARGUMENTS = 3;

	public static void main(String[] argv) throws SQLException, IOException {
		if (argv.length != NUM_ARGUMENTS) {
			System.out.printf("Error: Invalid Num Of Arguments: %s / %s\n", argv.length, NUM_ARGUMENTS);
			System.out.println("Usage: java StoreAlbum <name> <Chart Max> <Artist ID>");
			return;
		}

		// --- Set Arguments ---

		String name = argv[0];

		int chart_max;
		int artistID;
		try {
			chart_max = Integer.valueOf(argv[1]);
			artistID = Integer.valueOf(argv[2]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid Integer: " + e.getMessage());
			return;
		}

		Database db = new Database("./data/W06Exercise02.db");
		Dao<Album, Integer> dao = db.getAlbumDao();
		Dao<Artist, Integer> artist_dao = db.getArtistDao();

		Artist artist = artist_dao.queryForId(artistID);
		System.out.println(artist.getName());

		if (artist == null) {
			System.out.println("Invalid Artist ID: " + artistID);
			return;
		}

		JdbcConnectionSource source = db.getSource();

		TransactionManager.callInTransaction(source, new Callable<Void>() {
			public Void call() throws Exception {
				// Create Object from arguments
				Album album = new Album(name, chart_max, artist);
				dao.create(album);
				return null;

			}
		});

		db.close();
	}
}
