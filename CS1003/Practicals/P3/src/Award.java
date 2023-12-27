import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Object representation of an award in itself.
 * 
 * @see MovieAward For winners of movie awards
 * @see MoviePersonAward For winners of individual awards
 *
 */
@DatabaseTable(tableName = "Award")
public class Award {
	// Setup database column variables
	@DatabaseField(generatedId = true)
	private int award_id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField
	private String event;

	public Award() {
	}

	public Award(String name, String event) {
		this.name = name;
		this.event = event;
	}

	public int getID() {
		return award_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}


}
