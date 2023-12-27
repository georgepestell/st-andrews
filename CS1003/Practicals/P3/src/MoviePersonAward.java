import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A soft-entity representing awards for a MoviePerson, instead of a Movie. Multiple people can win
 * the same award in the same year for group awards as there are no primary keys, however, the same
 * MoviePerson cannot win the same award in the same year more than once.
 * 
 * @see MovieAward
 * @see Award
 *
 */
@DatabaseTable(tableName = "MoviePersonAward")
public class MoviePersonAward {
	// Setup database column variables
	@DatabaseField(canBeNull = false, foreign = true)
	private Award award;
	@DatabaseField(canBeNull = false, foreign = true)
	private MoviePerson movieperson;
	@DatabaseField(canBeNull = false)
	private int year;

	// Empty constructor Needed for ORMLite
	public MoviePersonAward() {
	}

	/**
	 * All attributes are needed to be added to the database
	 * 
	 * @param award       The award won
	 * @param movieperson The {@link MoviePerson} who won the award
	 * @param year        The year the MoviePerson won the award
	 */
	public MoviePersonAward(Award award, MoviePerson movieperson, int year) {
		this.award = award;
		this.movieperson = movieperson;
		this.year = year;
	}

	public MoviePerson getMoviePerson() {
		return movieperson;
	}

	public Award getAward() {
		return award;
	}

	public int getYear() {
		return year;
	}

}
