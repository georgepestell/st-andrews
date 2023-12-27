import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.Logger;

public class Database {
	private final JdbcConnectionSource source;
	private final Dao<Artist, Integer> artist_dao;
	private final Dao<Album, Integer> album_dao;

	public Database(String filename) throws SQLException {
		source = new JdbcConnectionSource("jdbc:sqlite:" + filename);
		artist_dao = DaoManager.createDao(source, Artist.class);
		album_dao = DaoManager.createDao(source, Album.class);

		Logger.setGlobalLogLevel(Log.Level.OFF);

	}

	public void close() throws IOException {
		source.close();
	}

	public Dao<Artist, Integer> getArtistDao() {
		return artist_dao;
	}

	public Dao<Album, Integer> getAlbumDao() {
		return album_dao;
	}

	public JdbcConnectionSource getSource() {
		return source;
	}

}
