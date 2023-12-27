import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;

public class StoreArtist {
	private static final int NUM_ARGUMENTS = 3;

	public static void main(String[] argv) throws SQLException, IOException {
		if (argv.length != NUM_ARGUMENTS) {
			System.out.printf("Error: Invalid Num Of Arguments: %s / %s\n", argv.length,
					NUM_ARGUMENTS);
			System.out.println("Usage: java StoreArtist <Name> <label> <Chart Max>");
			return;
		}

		// --- Set Arguments ---

		String name = argv[0];
		String label = argv[1];

		int chart_max;
		try {
			chart_max = Integer.valueOf(argv[2]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid Integer: " + e.getMessage());
			return;
		}

		Database db = new Database("./data/W06Exercise02.db");
		Dao<Artist, Integer> dao = db.getArtistDao();
		JdbcConnectionSource source = db.getSource();

		TransactionManager.callInTransaction(source, new Callable<Void>() {
			public Void call() throws Exception {
				// Create Object from arguments
				Artist artist = new Artist(name, label, chart_max);
				dao.create(artist);
				return null;

			}
		});

		db.close();
	}
}
