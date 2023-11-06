package Business;

import java.sql.SQLException;
import java.util.Vector;

/**
 * Encapsulates any business logic to be executed on the app server; 
 * and uses the data layer for data queries/creates/updates/deletes
 * @author IwanB
 *
 */
public interface IMovieProvider {

	/**
	 * Check login credentials
	 * @param userName : the userName of user credentials
	 * @param password : the password of user credentials
	 */
	public String checkStaffCredentials(String userName, String password) throws SQLException;
	
	/**
	 * Find movies associated in some way with a userName
	 * Movies which have the parameter below should be included in the result
	 * @param id
	 * @return
	 */
	public Vector<Movie> findMoviesByStaff(String userName) throws SQLException;
	
	/**
	 * Given an expression searchString like 'word' or 'this phrase', this method should return 
	 * any movies that contains this phrase.
	 * @param searchString : the searchString to use for finding movies in the database
	 * @return
	 */
	public Vector<Movie> findMoviesByCriteria(String searchString) throws SQLException;
	
	/**
	 * Add the details for a new movie to the database
	 * @param movie : the new movie to add
	 */
	public void addMovie(Movie movie) throws SQLException;

	/**
	 * Update the details for a given movie
	 * @param movie : the movie for which to update details
	 */
	public void updateMovie(Movie movie) throws SQLException;
}
