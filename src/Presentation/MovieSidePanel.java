package Presentation;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;

import Business.Movie;

/**
 * 
 * Represents instruction list panel of instruction tracker that includes
 * both the search box/button and text field; AND the instruction list
 * @author IwanB
 *
 */
public class MovieSidePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2693528613703066603L;

	private MovieListPanel mListPanel;
	
	/**
	 * Represents left panel of instruction tracker that includes
	 * both the search box/button and text field; AND the instruction list
	 * 
	 * @param searchEventListener : used to retrieve user search query in search box
	 * @param listPanel : instruction list panel
	 */
	public MovieSidePanel(ISearchMovieListener searchEventListener, MovieListPanel listPanel)
	{
		mListPanel = listPanel;
		MovieSearchComponents searchComponents = new MovieSearchComponents(searchEventListener);
	
		add(searchComponents);
		add(listPanel);
		
		BorderLayout layout = new BorderLayout();
		layout.addLayoutComponent(searchComponents, BorderLayout.NORTH);
		layout.addLayoutComponent(listPanel, BorderLayout.CENTER);
		setLayout(layout);
	}
	
	public void refresh(Vector<Movie> instructions)
	{
		mListPanel.refresh(instructions);
	}
	
	public void registerEventSelectionNotifiableObject(IMovieSelectionNotifiable notifiable)
	{
		mListPanel.registerEventSelectionNotifiableObject(notifiable);
	}
}
