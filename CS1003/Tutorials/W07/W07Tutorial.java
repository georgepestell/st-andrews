import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class W07Tutorial {
	public static void main(String[] argv) throws SQLException, IOException, ParseException {
		if (argv.length != 1) {
			System.out.println("Must give table name");
			return;
		}
		Connection connection = DriverManager.getConnection("jdbc:sqlite:./data/W07Tutorial.db");
		Statement stmnt = connection.createStatement();

		ResultSet res = stmnt.executeQuery("SELECT * FROM " + argv[0]);

		while (res.next()) {
			ResultSetMetaData meta = res.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				int type = meta.getColumnType(i);
				String name = meta.getColumnName(i);

				// switch (type) {
				// case 4:
				// System.out.println(name + ": " + res.getInt(i));
				// break;
				// case 12:
				// System.out.println(name + ": " + res.getString(i));
				// break;
				// case 91:
				// System.out.println(name + ": " + res.getString(i));
				// break;
				// default:
				// System.out.println(name + ": Unhandled Type");
				// }
				System.out.println(name + ": " + res.getObject(i).toString());
			}
			System.out.println();
		}

	}
}
