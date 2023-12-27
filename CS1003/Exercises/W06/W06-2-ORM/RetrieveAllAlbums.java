import java.sql.SQLException;
import java.io.IOException;

import com.j256.ormlite.dao.Dao;

public class RetrieveAllAlbums {
	public static void main(String[] argv) throws SQLException, IOException {
		Database db = new Database("./data/W06Exercise02.db");
		Dao<Album, Integer> dao = db.getAlbumDao();

		System.out.println("Albums:");
		for (Album album : dao) {
			System.out.println(album);
		}
		db.close();
	}
}
