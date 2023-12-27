import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Artists")
public class Artist extends BaseDaoEnabled<Artist, Integer> {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String label;
	@DatabaseField
	private int chartMax;

	// Necessary No Arg Constructor
	public Artist() {
	}

	public Artist(String name, String label, int chartMax) {
		this.name = name;
		this.label = label;
		this.chartMax = chartMax;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) throws SQLException {
		this.label = label;
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
		return "Artist{" + "id=" + id + ", name='" + name + '\'' + ", label='" + label + '\'' + ", chartMax=" + chartMax
				+ '}';
	}

}
