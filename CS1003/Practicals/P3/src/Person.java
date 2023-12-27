import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Real world entity for a person, stores their basic information such as name and date of birth.
 * 
 * See {@link MoviePerson} for linking Person to a Movie
 *
 */
@DatabaseTable(tableName = "Person")
public class Person {

	// --- Setup database column variables ---
	@DatabaseField(generatedId = true)
	private int person_id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField
	private Date dob;

	// Must have an empty constructor for ORMLite to work
	public Person() {
	}

	/**
	 * Allows a person to be created with all attribute values
	 * 
	 * @param name The person's real name
	 * @param dob  The person's date of birth
	 * @see #Person(String)
	 *
	 */
	public Person(String name, Date dob) {
		this.name = name;
		this.dob = dob;
	}

	/**
	 * Allows a person to be created with only the necessary attribute values
	 * 
	 * @param name The person's real name
	 * @see #Person(String, Date)
	 *
	 */
	public Person(String name) {
		this(name, null);
	}

	public int getID() {
		return person_id;
	}

	public String getName() {
		return name;
	}

	/**
	 * Get the person's date of birth
	 * 
	 * @return The Person's date of birth
	 *
	 */
	public Date getDob() {
		return dob;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the date of birth of the person
	 * 
	 * @param dob The {@link Date} object for the date of birth
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}


}
