package Presentation;

import Business.Movie;

/**
 * 
 * @author IwanB
 * 
 * Used to notify any interested object that implements this interface
 * and registers with InstructionListPanel of an InstructionSelection
 *
 */
public interface IMovieSelectionNotifiable {
	public void movieSelected(Movie movie);
}
