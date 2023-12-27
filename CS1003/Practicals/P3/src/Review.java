import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Object representation for a review with thre rating and optional review notes
 *
 */
@DatabaseTable(tableName = "Review")
public class Review {
	// Setup database column variables
	@DatabaseField(generatedId = true)
	private int review_id;
	@DatabaseField(canBeNull = false, foreign = true)
	private Movie movie;
	@DatabaseField
	private int rating;
	@DatabaseField
	private String notes;

	// Empty constructor needed for ORMLite
	public Review() {
	}

	/**
	 * A review constructor to set all information
	 * 
	 * @param movie  The movie the review is about
	 * @param rating The rating /10 for the movie
	 * @param notes  The review notes
	 * @see #Review(Movie, int)
	 *
	 */
	public Review(Movie movie, int rating, String notes) {

		this.notes = notes;
		this.movie = movie;
		this.setRating(rating);
	}

	/**
	 * A review constructor with just movie and rating score, and sets the notes to null
	 * 
	 * @param movie  The movie the review is about
	 * @param rating The rating /10 for the movie
	 * @see #Review(Movie, int, String)
	 *
	 */

	public Review(Movie movie, int rating) {
		this(movie, rating, null);
	}

	public int getID() {
		return review_id;
	}

	public Movie getMovie() {
		return movie;
	}

	public String getEvent() {
		return notes;
	}

	/**
	 * Allows the rating to be set, but limits the integer values to between 0 and 10
	 * 
	 * @param rating The rating /10 for the movie
	 *
	 */
	public void setRating(int rating) {
		// Limits the rating to between 0 and 10
		if (rating > 10)
			this.rating = 10;
		else if (rating < 0)
			this.rating = 0;
		else
			this.rating = rating;

	}

	public void setNotes(String notes) {
		this.notes = notes;
	}


}
