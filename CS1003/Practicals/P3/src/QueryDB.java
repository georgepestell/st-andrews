import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

/**
 * <p>
 * Allows the user to query the database from a set list of query commands.
 * </p>
 * <p>
 * Usage: java QueryDB [query-number] [query-options]
 * </p>
 * 
 * @see QueryDB#printMovies query-1
 * @see QueryDB#printActors query-2
 * @see QueryDB#printDirectors query-3
 * @see QueryDB#printDirectors query-4
 * @see QueryDB#printMoviesWithAwardCount query-5
 * @see QueryDB#printPersonAwards query-6
 * 
 * 
 */
public class QueryDB {
	/** Json Factory used in collating the information from the database for printing */
	private static JsonBuilderFactory factory = Json.createBuilderFactory(null);
	/** Database object to query. Set in the main method */
	private static Database db;

	/**
	 * Prints all of the movie titles in the database Runs when query 1 is selected in the main
	 * function
	 */
	private static void printMovies() throws SQLException {
		// Setup JSON Writer
		JsonArrayBuilder builder = factory.createArrayBuilder();

		db.getMovieDao().forEach((movie) -> {
			builder.add(movie.getTitle());
		});

		System.out.println(builder.build());
	}

	private static void printActors(String movieTitle) throws SQLException {
		// Setup JSON Writer
		JsonArrayBuilder builder = factory.createArrayBuilder();

		// Loop through movies with given title
		// This is because multiple movies can have the same title
		List<Movie> movies = db.getMovieDao().queryForEq("title", movieTitle);
		for (Movie m : movies) {
			// Setup actor query values
			HashMap<String, Object> actorFields = new HashMap<String, Object>();
			actorFields.put("role", "actor");
			actorFields.put("movie_id", m.getID());

			// Loop through Actors in specified movie and adds the names to the JSON array
			db.getMoviePersonDao().queryForFieldValues(actorFields).forEach((actor) -> {
				try {
					// Gets the person's name from the database
					Person person = db.getPersonDao().queryForId(actor.getPerson().getID());
					// Adds the person's name to the output array
					builder.add(person.getName());
				} catch (SQLException e) {
					System.out.println("Error: Database Mismatch, Person Doesn't Exist");
				}
			});

			// Print the list of actors
			System.out.println(builder.build().toString());

		}
	}

	private static void printPlot(String actor_name, String director_name) throws SQLException {
		JsonArrayBuilder builder = factory.createArrayBuilder();

		// Get A List of Matching Actors and Directors ID's
		List<Person> personActors = db.getPersonDao().queryForEq("name", actor_name);
		List<Person> personDirectors = db.getPersonDao().queryForEq("name", director_name);

		// Check if actor and director names exist in the database
		if (personActors.size() == 0 || personDirectors.size() == 0) {
			System.out.println("Error: Actor and/or Director doesn't exist in table 'People'");
			return;
		}

		// As People can have the same name,
		// you must loop over a list of people matching the names given
		for (Person personActor : personActors) {
			for (Person personDirector : personDirectors) {
				// Setup name and role fields to match MoviePerson query
				HashMap<String, Object> actorFields = new HashMap<String, Object>();
				HashMap<String, Object> directorFields = new HashMap<String, Object>();

				actorFields.put("role", "actor");
				directorFields.put("role", "director");
				actorFields.put("person_id", personActor);
				directorFields.put("person_id", personDirector);

				// Get all matching actors and dirctors
				List<MoviePerson> actors = db.getMoviePersonDao().queryForFieldValues(actorFields);
				List<MoviePerson> directors =
						db.getMoviePersonDao().queryForFieldValues(directorFields);

				// Check if movies match for any actors and directors
				for (MoviePerson actor : actors) {
					for (MoviePerson director : directors) {
						if (actor.getMovie().getID() == director.getMovie().getID()) {
							JsonObjectBuilder movieBuilder = factory.createObjectBuilder();
							Movie movie = db.getMovieDao().queryForId(director.getMovie().getID());
							movieBuilder.add("title", movie.getTitle());
							movieBuilder.add("plot", movie.getPlot());
							builder.add(movieBuilder);
						}
					}
				}
			}
		}

		System.out.println(builder.build().toString());
	}

	public static void printDirectors(String actor_name) throws SQLException {
		JsonObjectBuilder builder = factory.createObjectBuilder();

		// Check Actor Exists
		List<Person> personActors = db.getPersonDao().queryForEq("name", actor_name);
		for (Person personActor : personActors) {
			HashMap<String, Object> movieActorFields = new HashMap<String, Object>();
			movieActorFields.put("person_id", personActor);
			movieActorFields.put("role", "actor");
			List<MoviePerson> moviePersonActors =
					db.getMoviePersonDao().queryForFieldValues(movieActorFields);
			for (MoviePerson moviePersonActor : moviePersonActors) {
				HashMap<String, Object> movieDirectorFields = new HashMap<String, Object>();
				movieDirectorFields.put("movie_id", moviePersonActor.getMovie());
				movieDirectorFields.put("role", "director");
				db.getMoviePersonDao().queryForFieldValues(movieDirectorFields)
						.forEach((director) -> {
							// Get the Movie Title and Director Name and add to json
							try {
								builder.add(db.getMovieDao()
										.queryForId(moviePersonActor.getMovie().getID()).getTitle(),
										db.getPersonDao().queryForId(director.getPerson().getID())
												.getName());
							} catch (SQLException e) {
								System.out.println("Error: Database References Invalid");
							}
						});

			}

		}
		System.out.println(builder.build().toString());

	}

	private static void printMoviesWithAwardCount(int awardCount) {
		JsonArrayBuilder builder = factory.createArrayBuilder();

		db.getMovieDao().forEach((movie) -> {
			try {
				if (db.getMovieAwardDao().queryForEq("movie_id", movie).size() == awardCount) {
					builder.add(movie.getTitle());
				}
			} catch (SQLException e) {
				System.out.println("Database Error: " + e.getMessage());
			}
		});

		// Print out the list of movies
		System.out.println(builder.build().toString());
	}

	private static void printPersonAwards(String name) {
		JsonArrayBuilder builder = factory.createArrayBuilder();

		try {
			// Search for the Person by name
			db.getPersonDao().queryForEq("name", name).forEach((person) -> {
				JsonArrayBuilder personJson = factory.createArrayBuilder();

				List<MoviePerson> roles;
				try {
					// Get all roles for that person
					roles = db.getMoviePersonDao().queryForEq("person_id", person);
				} catch (SQLException e) {
					System.out
							.println("Database error while getting MoviePerson: " + e.getMessage());
					return;
				}

				// Loop over the roles and get all awards for that role
				for (MoviePerson role : roles) {
					try {
						db.getMoviePersonAwardDao().queryForEq("movieperson_id", role)
								.forEach((mpa) -> {
									// Add the award to the list of awards for this Person
									try {
										Award award =
												db.getAwardDao().queryForId(mpa.getAward().getID());
										personJson.add(mpa.getYear() + " " + award.getName());
									} catch (SQLException e) {
										System.out.println("Database error while getting Award:"
												+ e.getMessage());
									}
								});
					} catch (SQLException e) {
						System.out.println("Database error while getting MoviePersonAwards: "
								+ e.getMessage());
					}
				}
				builder.add(personJson);


			});
		} catch (SQLException e) {
			System.out.println("Database error whilst getting Person: " + e.getMessage());
		}

		System.out.println(builder.build().toString());

	}

	/**
	 * Checks user input for a query number and then runs the relevant query function with relevant
	 * options from args
	 *
	 * @param args [0] is the query number, [1+] are the query options
	 *
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Error: Must give at least 1 argument");
			System.out.println("Usage: java queryDB <query-number> [query-options]");
			return;
		}

		try {
			db = new Database("./data/P3Database.db");
			switch (Integer.valueOf(args[0])) {
				case 1:
					// Print out movies
					printMovies();
					break;
				case 2:
					// Make sure a movie name is supplied
					if (args.length != 2) {
						System.out.printf("Error: %s / 2 arguments given\n", args.length);
						System.out.println("Usage: java queryDB 2 <movie-name>");
						return;
					}
					printActors(args[1]);
					break;
				case 3:
					if (args.length != 3) {
						System.out.printf("Error: %s / 3 arguments given\n", args.length);
						System.out.println("Usage: java queryDB 3 <actor-name> <director_name>");
						return;
					}
					printPlot(args[1], args[2]);
					break;
				case 4:
					// Make sure an actors name is supplied
					if (args.length != 2) {
						System.out.printf("Error: %s / 2 arguments given\n", args.length);
						System.out.println("Usage: java queryDB 4 <actor-name>");
						return;
					}
					printDirectors(args[1]);
					break;
				case 5:
					// Make sure an actors names are supplied
					if (args.length != 2) {
						System.out.printf("Error: %s / 2 arguments given\n", args.length);
						System.out.println("Usage: java queryDB 5 <award-count>");
						return;
					}
					printMoviesWithAwardCount(Integer.valueOf(args[1]));
					break;
				case 6:
					// Make sure
					if (args.length != 2) {
						System.out.printf("Error: %s / 2 arguments given\n", args.length);
						System.out.println("Usage: java queryDB 6 <person-name>");
						return;
					}
					printPersonAwards(args[1]);
					break;
				default:
					System.out.println("Error: Input must be in range 1-6");
					return;

			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid Integer Input: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			try {
				db.close();
			} catch (IOException e) {
				System.out.println("IOException: Failed to Close Database");
			}
		}
	}
}
