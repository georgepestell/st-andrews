import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;

public class UpdateAlbum {
	private static final int NUM_ARGUMENTS = 3;

	public static void main(String[] argv) throws SQLException, IOException {
		if (argv.length != NUM_ARGUMENTS) {
			System.out.printf("Error: Invalid Num Of Arguments: %s / %s\n", argv.length,
					NUM_ARGUMENTS);
			System.out.println("Usage: java UpdateAlbum <Album ID> <Attribute> <New Value>");
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

		String attr = argv[1];
		String val = argv[2];

		Database db = new Database("./data/W06Exercise02.db");
		Dao<Album, Integer> dao = db.getAlbumDao();
		JdbcConnectionSource source = db.getSource();

		try {
			TransactionManager.callInTransaction(source, new Callable<Void>() {
				public Void call() throws Exception {
					Album album = dao.queryForId(id);
					if (album == null) {
						System.out.println("Error: Can't Find Employee: " + id);

					} else {
						switch (attr) {
							case "id":
								album.setId(Integer.valueOf(val));
								break;
							case "name":
								album.setName(val);
								break;
							case "artist":
								Dao<Artist, Integer> artist_dao = db.getArtistDao();
								Artist artist = artist_dao.queryForId(Integer.valueOf(val));

								if (artist == null) {
									System.out.println("Error: Artist Not Found With ID");
								} else {
									album.setArtist(artist);
								}

								break;
							case "chartMax":
								album.setChartMax(Integer.valueOf(val));
								break;
							default:
								System.out.println("Error: Invalid Attribute: " + attr);
								System.out.println(
										"Avaliable Attributes: id, name, chartMax, artist");
								break;
						}

					}
					return null;
				}
			});
		} catch (Exception e) {
			System.out.println("Exception Occurred: " + e.getMessage());
		}

	}

}
