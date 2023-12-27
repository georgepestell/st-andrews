import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Albums")
public class Album extends BaseDaoEnabled<Album, Integer> {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@DatabaseField
	private int chartMax;

	@DatabaseField(foreign = true)
	private Artist artist;

	// Necessary No Arg Constructor
	public Album() {
	}

	public Album(String name, int chartMax, Artist artist) {
		this.name = name;
		this.chartMax = chartMax;
		this.artist = artist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws SQLException {
		this.name = name;
		this.update();
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) throws SQLException {
		this.artist = artist;
		this.update();
	}

	public int getChartMax() {
		return chartMax;
	}

	public void setChartMax(int chartMax) throws SQLException {
		this.chartMax = chartMax;
		this.update();
	}

	@Override
	public String toString() {
		Artist artist = null;
		try {
			artist = RetrieveArtist.retrieve(this.artist.getId());
		} catch (Exception e) {
		}
		return "Album{" + "id=" + id + ", name='" + name + '\'' + ", artist="
				+ ((artist.equals(null)) ? "null" : artist) + ", chartMax='" + chartMax + '\''
				+ '}';
	}
}
