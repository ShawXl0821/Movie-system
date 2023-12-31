package Presentation;


import java.sql.SQLException;

/**
 * 
 * @author IwanB
 *
 * Used to notify any interested object that implements this interface
 * and is used to construct PatientSearchComponents of a search that was issued
 * 
 */
public interface ISearchMovieListener {
	public void searchClicked(String searchString) throws SQLException;
}
