/**
 * <p>
 * This package takes input from raw CSV files about Movies. The people, awards and reviews related
 * to those movies.
 * </p>
 * 
 * <h2>Instructions to run:</h2>
 * 
 * <ol>
 * <li>export
 * CLASSPATH=$CLASSPATH:"./lib/ormlite-core-5.3.jar:./lib/ormlite-jdbc-5.3.jar:./lib/sqlite-jdbc-3.34.0.jar:./lib/javax.json-1.0.jar"</li>
 * <li>java {@link InitialiseDB}</li>
 * <li>java {@link PopulateDB}</li>
 * <li>java {@link QueryDB} [query-number] [query-options]</li>
 * </ol>
 *
 * <h2>Other Important Files</h2>
 *
 * <ul>
 * <li><a href="./Report.pdf">Report.pdf</a></li>
 * <li><a href="./compartive-essay.pdf">compartive-essay.pdf</a></li>
 * </ul>
 * 
 * 
 * @see QueryDB#printMovies query-1
 * @see QueryDB#printActors query-2
 * @see QueryDB#printDirectors query-3
 * @see QueryDB#printDirectors query-4
 * @see QueryDB#printMoviesWithAwardCount query-5
 * @see QueryDB#printPersonAwards query-6
 * 
 * @author Student ID: 200007413
 *
 *
 */
