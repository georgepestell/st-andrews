import java.io.IOException;
import java.sql.SQLException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.Logger;

/**
 * Database class provides a direct connection to the database file.
 *
 */
public class Database implements AutoCloseable {
	/** The JDBC driver connection to the database */
	private final JdbcConnectionSource source;

	/** Object representation of the Person table */
	private Dao<Person, Integer> person_dao;
	/** Object representation of the Movie table */
	private Dao<Movie, Integer> movie_dao;
	/** Object representation of the Award table */
	private Dao<Award, Integer> award_dao;
	/** Object representation of the Review table */
	private Dao<Review, Integer> review_dao;
	/** Object representation of the MoviePerson table */
	private Dao<MoviePerson, Integer> movieperson_dao;
	/** Object representation of the MovieAward table */
	private Dao<MovieAward, Integer> movieaward_dao;
	/** Object representation of the MoviePersonAward table */
	private Dao<MoviePersonAward, Integer> moviepersonaward_dao;

	/**
	 * Attempts to connect to the database file and creates object representations for each table
	 * 
	 * @param dbfile The database filepath to connect to
	 * @throws SQLException if the database does not already exist
	 */
	public Database(String dbfile) throws SQLException {
		// Suppress any unintended output
		Logger.setGlobalLogLevel(Log.Level.OFF);

		// Setup DAOs for each table
		source = new JdbcConnectionSource("jdbc:sqlite:" + dbfile);
		person_dao = DaoManager.createDao(source, Person.class);
		movie_dao = DaoManager.createDao(source, Movie.class);
		review_dao = DaoManager.createDao(source, Review.class);
		award_dao = DaoManager.createDao(source, Award.class);
		movieperson_dao = DaoManager.createDao(source, MoviePerson.class);
		movieaward_dao = DaoManager.createDao(source, MovieAward.class);
		moviepersonaward_dao = DaoManager.createDao(source, MoviePersonAward.class);

	}

	/** Closes the connection to the database */
	public void close() throws IOException {
		source.close();
	}

	/**
	 * Get the {@link Person} DAO allowing queries to be made on the {@link Person} table
	 * 
	 * @return the Person {@link Dao}
	 *
	 */
	public Dao<Person, Integer> getPersonDao() {
		return person_dao;
	}

	/**
	 * Get the {@link Movie} DAO allowing queries to be made on the {@link Movie} table
	 * 
	 * @return the Movie {@link Dao}
	 *
	 */
	public Dao<Movie, Integer> getMovieDao() {
		return movie_dao;
	}


	/**
	 * Get the {@link Award} allowing queries to be made on the {@link Award}
	 * 
	 * @return The Award {@link Dao}
	 *
	 */
	public Dao<Award, Integer> getAwardDao() {
		return award_dao;
	}

	/**
	 * Get the MoviePerson DAO allowing queries to be made on the {@link MoviePerson}
	 * 
	 * @return the MoviePerson {@link Dao}
	 *
	 */
	public Dao<MoviePerson, Integer> getMoviePersonDao() {
		return movieperson_dao;
	}

	/**
	 * Get the MoviePersonAward DAO allowing queries to be made on the {@link MoviePersonAward}
	 * 
	 * @return the MoviePersonAward {@link Dao}
	 *
	 */
	public Dao<MoviePersonAward, Integer> getMoviePersonAwardDao() {
		return moviepersonaward_dao;
	}

	/**
	 * Get the MovieAward DAO allowing queries to be made on the {@link MovieAward}
	 * 
	 * @return the MovieAward {@link Dao}
	 *
	 */
	public Dao<MovieAward, Integer> getMovieAwardDao() {
		return movieaward_dao;
	}

	/**
	 * Allows a Person to be added to the database
	 * 
	 * @param person The {@link Person} object to be added
	 * @throws SQLException if the person is invalid or database inaccessible
	 * 
	 */
	public void addPerson(Person person) throws SQLException {
		this.person_dao.create(person);
	}

	/**
	 * Allows a Movie to be added to the database
	 * 
	 * @param movie The {@link Movie} object to be added
	 * @throws SQLException if the movie is invalid or database inaccessible
	 *
	 */
	public void addMovie(Movie movie) throws SQLException {
		this.movie_dao.create(movie);
	}

	/**
	 * Allows a Review to be added to the database
	 * 
	 * @param review The {@link Review} object to be added
	 * @throws SQLException if the review is invalid or database inaccessible
	 *
	 */
	public void addReview(Review review) throws SQLException {
		this.review_dao.create(review);
	}

	/**
	 * Allows an Award to be added to the database
	 * 
	 * @param award The {@link Award} object to be added
	 * @throws SQLException if the award is invalid or database inaccessible
	 *
	 */
	public void addAward(Award award) throws SQLException {
		this.award_dao.create(award);
	}

	/**
	 * Allows a MoviePerson to be added to the database
	 * 
	 * @param movieperson The {@link MoviePerson} object to be added
	 * @throws SQLException if the movieperson is invalid or database inacessible
	 *
	 */
	public void addMoviePerson(MoviePerson movieperson) throws SQLException {
		this.movieperson_dao.create(movieperson);
	}

	/**
	 * Allows a MovieAward to be added to the database
	 * 
	 * @param movieaward The {@link MovieAward} object to be added
	 * @throws SQLException if the movieaward is invalid or database inacessible
	 *
	 */
	public void addMovieAward(MovieAward movieaward) throws SQLException {
		this.movieaward_dao.create(movieaward);
	}

	/**
	 * Allows a MoviePersonAward to be added to the database
	 * 
	 * @param moviepersonaward The {@link MoviePersonAward} object to be added
	 * @throws SQLException if the moviepersonaward is invalid or database inacessible
	 * 
	 */
	public void addMoviePersonAward(MoviePersonAward moviepersonaward) throws SQLException {
		this.moviepersonaward_dao.create(moviepersonaward);
	}

}
