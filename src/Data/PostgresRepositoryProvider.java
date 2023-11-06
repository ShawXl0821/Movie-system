package Data;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;

import Business.Movie;
import Presentation.IRepositoryProvider;


/**
 * Encapsulates create/read/update/delete operations to PostgreSQL database
 * @author IwanB
 */
public class PostgresRepositoryProvider implements IRepositoryProvider {
	//DB connection parameters - ENTER YOUR LOGIN AND PASSWORD HERE
	private final String userid = "y23s1c9120_xliu7691";
	private final String passwd = "Zhwlx0530";
	private final String myHost = "soit-db-pro-2.ucc.usyd.edu.au";

	//date format as required
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private Connection openConnection() throws SQLException {
		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerName(myHost);
		source.setDatabaseName(userid);
		source.setUser(userid);
		source.setPassword(passwd);
		Connection conn = source.getConnection();

		return conn;
	}

	/**
	 * Validate login request
	 *
	 * @param userName: the user's userName trying to login
	 * @param password: the user's password used to login
	 * @return
	 */
	@Override
	public String checkStaffCredentials(String userName, String password) throws SQLException {
		Connection conn = openConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT Login, Password FROM Staff WHERE Login = LOWER(?) and Password = ?"
			);
			stmt.setString(1, userName);
			stmt.setString(2, password);
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				// The credentials are valid
				rset.close();
				return userName;
			} else {
				// The credentials are invalid
				rset.close();
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			conn.close();
		}
	}

	/**
	 * Find all movies associated with a user
	 *
	 * @param userName: the user's userName
	 * @return
	 */
	@Override
	public Vector<Movie> findMoviesByStaff(String userName) throws SQLException {
		Vector<Movie> movies = new Vector<>();
		Connection conn = openConnection();
		try{
			CallableStatement stmt = conn.prepareCall("{call find_movies_by_staff(LOWER(?))}");
			stmt.setString(1, userName);
			ResultSet rset = stmt.executeQuery();
			//loop through the result
			while (rset.next()) {
				Movie movie = new Movie();
				movie.setMovieId(rset.getInt("id"));
				movie.setTitle(rset.getString("title"));

				//format the date to 'dd-mm-yyyy' as requirement
				String releaseDateStr = dateFormat.format(rset.getDate("ReleaseDate"));
				movie.setReleaseDate(releaseDateStr);

				String genre1=rset.getString("PrimaryGenre");
				String genre2=rset.getString("SecondaryGenre");
				//show genre as required
				if(genre2==null||genre2.equals("")){
					movie.setGenre(genre1);
				}else{
					movie.setGenre(genre1+","+genre2);
				}
				movie.setAvgRating(rset.getFloat("avgRating"));
				movie.setStaff(rset.getString("staffName"));
				movie.setDescription(rset.getString("description"));
				movies.add(movie);
			}
			stmt.close();
			rset.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			conn.close();
		}
		return movies;
	}

	/**
	 * Find a list of movies based on the searchString provided as parameter
	 *
	 * @param searchString: see assignment description for search specification
	 * @return
	 */
	@Override
	public Vector<Movie> findMoviesByCriteria(String searchString) throws SQLException {
		Vector<Movie> movies = new Vector<>();
		Connection conn = openConnection();
		try {
			CallableStatement stmt = conn.prepareCall("{call find_movies_by_criteria(?)}");
			stmt.setString(1, searchString);
			ResultSet rset = stmt.executeQuery();
			// loop through the result
			while (rset.next()) {
				Movie movie = new Movie();
				movie.setMovieId(rset.getInt("Id"));
				movie.setTitle(rset.getString("Title"));

				//format the date to 'dd-mm-yyyy' as requirement
				String releaseDateStr = dateFormat.format(rset.getDate("ReleaseDate"));
				movie.setReleaseDate(releaseDateStr);

				String genre1=rset.getString("PrimaryGenre");
				String genre2=rset.getString("SecondaryGenre");
				//show genre as required
				if(genre2==null||genre2.equals("")){
					movie.setGenre(genre1);
				}else{
					movie.setGenre(genre1+","+genre2);
				}

				movie.setAvgRating(rset.getFloat("AvgRating"));
				movie.setStaff(rset.getString("ManagedBy"));
				movie.setDescription(rset.getString("Description"));
				movies.add(movie);
			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			conn.close();
		}

		return movies;
	}
	/**
	 * Add a new movie into the Database
	 *
	 * @param movie: the new movie to add
	 */
	@Override
	public void addMovie(Movie movie) throws SQLException {
		Connection conn = openConnection();
		PreparedStatement ps = null;
		try {
			String query = "INSERT INTO Movie (Title, ReleaseDate, PrimaryGenre, SecondaryGenre, ManagedBy, Description) " +
					"VALUES (?, to_date(?, 'dd-MM-yyyy'), (SELECT GenreID FROM Genre WHERE GenreName=?), (SELECT GenreID FROM Genre WHERE GenreName=?), ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, movie.getTitle());

			// Check that the release date is in the correct format
			dateFormat.setLenient(false);
			Date releaseDate = dateFormat.parse(movie.getReleaseDate());
			if (!dateFormat.format(releaseDate).equals(movie.getReleaseDate())) {
				throw new IllegalArgumentException("Invalid release date format");
			}

			ps.setString(2, movie.getReleaseDate());
			ps.setString(3, movie.getPrimaryGenre());
			ps.setString(4, movie.getSecondaryGenre());
			ps.setString(5, movie.getStaff());
			ps.setString(6, movie.getDescription());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Update the details of a given movie
	 *
	 * @param movie: the movie for which to update details
	 */
	@Override
	public void updateMovie(Movie movie) throws SQLException {
		Connection conn = openConnection();
		PreparedStatement ps = null;
		try {
			String query =
					"UPDATE Movie " +
							"SET Title = ?, ReleaseDate = to_date(?, 'dd-mm-yyyy'), PrimaryGenre = (SELECT GenreID FROM Genre WHERE GenreName=?), SecondaryGenre = (SELECT GenreID FROM Genre WHERE GenreName=?), " +
							"AvgRating = ?, ManagedBy = ?, Description = ? " +
							"WHERE Id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, movie.getTitle());

			// Check that the release date is in the correct format
			dateFormat.setLenient(false);
			Date releaseDate = dateFormat.parse(movie.getReleaseDate());
			if (!dateFormat.format(releaseDate).equals(movie.getReleaseDate())) {
				throw new IllegalArgumentException("Invalid release date format");
			}

			ps.setString(2, movie.getReleaseDate());


			// update genre
			String[] genres = movie.getPrimaryGenre().split(",\\s*");
			ps.setString(3, genres[0]);
			if (genres.length > 1) {
				ps.setString(4, genres[1]);
			} else {
				ps.setNull(4, Types.VARCHAR);
			}

			ps.setFloat(5,movie.getAvgRating());
			ps.setString(6, movie.getStaff());
			ps.setString(7, movie.getDescription());
			ps.setInt(8, movie.getMovieId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

}
