package Presentation;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import Business.Movie;

/**
 * 
 * @author IwanB
 *
 * AddMovieDialog - used to add a new movie
 * 
 */
public class AddMovieDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 173323780409671768L;
	
	/**
	 * detailPanel: reuse MovieDetailPanel to add events
	 */
	private MovieDetailPanel detailPanel = new MovieDetailPanel(false);

	public AddMovieDialog()
	{
		setTitle(StringResources.getAppTitle());
		detailPanel.initMovieDetails(getBlankInstruction());
		add(detailPanel);
		updateLayout();
		setSize(400, 400);
	}

	private void updateLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		layout.addLayoutComponent(detailPanel, BorderLayout.CENTER);
	}

	private Movie getBlankInstruction() {
		Movie movie = new Movie();
		return movie;
	}
}
