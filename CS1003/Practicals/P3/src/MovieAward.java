import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A soft-entity representing the awards given to movies in a specific year. The {@link #award} and
 * {@link #year} attributes make up the primary-key pairs so only one movie can win a speciic award
 * in a specific year.
 * 
 * @see MoviePersonAward
 * @see Award
 */
@DatabaseTable(tableName = "MovieAward")
public class MovieAward {
	// Setup database column variables
	@DatabaseField(canBeNull = false, foreign = true)
	private Award award;
	@DatabaseField(canBeNull = false, foreign = true)
	private Movie movie;
	@DatabaseField(canBeNull = false)
	private int year;

	// Empty constructor needed for ORMLite
	public MovieAward() {
	}

	/**
	 * Sets up the award won for the specific year and the movie who won it
	 * 
	 * @param award The award won
	 * @param movie The movie who won the award
	 * @param year  The year the movie won the award
	 *
	 */
	public MovieAward(Award award, Movie movie, int year) {
		this.award = award;
		this.movie = movie;
		this.year = year;
	}

	public Movie getMovie() {
		return movie;
	}

	public Award getAward() {
		return award;
	}

	public int getYear() {
		return year;
	}

}
