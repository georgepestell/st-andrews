import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Initialise {
	public static void main(String[] argv) throws SQLException, IOException {
		// Initialize the database
		Database db = new Database("./data/W06Exercise02.db");
		JdbcConnectionSource source = db.getSource();

		// Create Artist & Album tables
		TableUtils.createTableIfNotExists(source, Artist.class);
		TableUtils.createTableIfNotExists(source, Album.class);

		db.close();
	}
}
