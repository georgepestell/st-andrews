import java.sql.SQLException;
import java.io.IOException;

import com.j256.ormlite.dao.Dao;

public class RetrieveAllArtists {
	public static void main(String[] argv) throws SQLException, IOException {
		Database db = new Database("./data/W06Exercise02.db");
		Dao<Artist, Integer> dao = db.getArtistDao();

		System.out.println("Artists:");
		for (Artist artist : dao) {
			System.out.println(artist);
		}
		db.close();
	}
}
