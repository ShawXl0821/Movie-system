package Presentation;

import java.sql.SQLException;
import java.util.Vector;

import Business.Movie;

/**
 * Encapsulates create/read/update/delete operations to database
 * @author IwanB
 *
 */
public interface IRepositoryProvider {
	/**
	 * Check login credentials
	 * @param userName: the userName of user credentials
	 * @param password: the password of user credentials
	 */
	public String checkStaffCredentials(String userName, String password) throws SQLException;
	
	/**
	 * Find associated movies given a userName
	 * @param user: the userName
	 * @return
	 */
	public Vector<Movie> findMoviesByStaff(String userName) throws SQLException;
	
	/**
	 * Find the associated movies based on the searchString provided as the parameter
	 * @param searchString: see assignment description search specification
	 * @return
	 */
	public Vector<Movie> findMoviesByCriteria(String searchString) throws SQLException;
	
	/**
	 * Add a new movie into the database
	 * @param movie: the new movie to add
	 */
	public void addMovie(Movie movie) throws SQLException;

	/**
	 * Update the details for a given movie
	 * @param movie: the movie for which to update details
	 */
	public void updateMovie(Movie movie) throws SQLException;
}
