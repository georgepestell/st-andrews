import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * An object representation of a single Movie
 */
@DatabaseTable(tableName = "Movie")
public class Movie {
	// Setup database column variables
	@DatabaseField(generatedId = true)
	private int movie_id;
	@DatabaseField(canBeNull = false)
	private String title;
	@DatabaseField
	private int releaseDate;
	@DatabaseField
	private int runtime;
	@DatabaseField
	private String genre;
	@DatabaseField
	private String plot;

	// Empty constructor needed for ORMLite
	public Movie() {
	}

	/**
	 * The full constructor for a movie with all attriubutes
	 * 
	 * @param title       The movie title
	 * @param releaseDate The movie's year of release
	 * @param runtime     The runtime (in minutes) of the movie
	 * @param genre       The genre of the movie
	 * @param plot        A short plot synopsis of the movie
	 * @see #Movie(String)
	 *
	 */
	public Movie(String title, int releaseDate, int runtime, String genre, String plot) {
		this.title = title;
		this.releaseDate = releaseDate;
		this.runtime = runtime;
		this.genre = genre;
		this.plot = plot;
	}

	/**
	 * The minimal constructor for a movie to add a movie with just it's title
	 * 
	 * @param title The movie title
	 * @see #Movie(String, int, int, String, String)
	 *
	 */
	public Movie(String title) {
		this.title = title;
	}

	public int getID() {
		return movie_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setReleaseDate(int releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

}
