import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A soft-entity representing a Person working on a Movie. This means that multiple people can work
 * for a movie and indiviudal people to work on multiple movies.
 *
 */
@DatabaseTable(tableName = "MoviePerson")
public class MoviePerson {

	// --- Setup attributes for database columns ---
	@DatabaseField(generatedId = true)
	private int movieperson_id;
	@DatabaseField(canBeNull = false, foreign = true)
	private Movie movie;
	@DatabaseField(canBeNull = false, foreign = true)
	private Person person;
	@DatabaseField(canBeNull = false)
	private String role;
	@DatabaseField
	private String character_name;

	// Must have an empty constructor for ORMLite
	public MoviePerson() {
	}

	/**
	 * Allows a MoviePerson to be created with a character name associated if applicable. If not,
	 * use MoviePerson(movie, person, role)
	 * 
	 * @param movie          The Movie the Person is working on
	 * @param person         The Person working on the movie
	 * @param role           The role the Person has on this Movie
	 * @param character_name The name of the character if applicable
	 * @see #MoviePerson(Movie, Person, String)
	 */
	public MoviePerson(Movie movie, Person person, String role, String character_name) {
		this.movie = movie;
		this.person = person;
		this.role = role;
		this.character_name = character_name;
	}

	/**
	 * Allows a MoviePerson to be created without setting a character name
	 * 
	 * @param movie  The Movie the Person is working on
	 * @param person The Person working on the movie
	 * @param role   The role the Person has on this Movie
	 * @see #MoviePerson(Movie, Person, String, String)
	 */
	public MoviePerson(Movie movie, Person person, String role) {
		this(movie, person, role, null);
	}

	public int getID() {
		return movieperson_id;
	}

	public Movie getMovie() {
		return movie;
	}

	public Person getPerson() {
		return person;
	}

	public String getRole() {
		return role;
	}

	public String getCharacterName() {
		return character_name;
	}

	public void setCharacterName(String name) {
		this.character_name = name;
	}

	public void setRole(String role) {
		role = role.toLowerCase();
	}
}
