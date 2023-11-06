package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Business.BusinessComponentFactory;
import Business.Movie;


/**
 * 
 * @author IwanB
 * Panel used for creating and updating movies
 */
public class MovieDetailPanel extends JPanel implements IMovieSelectionNotifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2031054367491790942L;

	private Movie currentMovie = null;
	private boolean isUpdatePanel = true;

	private JTextField movieIdField;
	private JTextField titleField;
	private JTextField releaseDateField;
	private JTextField avgRatingField;
	private JTextField genreField;
	private JTextField primaryGenreField;
	private JTextField secondaryGenreField;
	private JTextField staffField;
	private JTextArea descriptionArea;
	
	/**
	 * Panel used for creating and updating movies
	 * @param isUpdatePanel : describes whether panel will be used to either create or update movie
	 */
	public MovieDetailPanel(boolean isUpdatePanel)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.isUpdatePanel = isUpdatePanel;
	}

	/**
	 * Re-populates panel details with given movie object
	 * @param movie new movie object to populate panel details with
	 */
	public void initMovieDetails(Movie movie) {
		removeAll();	
		if(movie != null)
		{
			currentMovie = movie;
			addAll();
		}
	}

	private void addAll() {
		JPanel lTextFieldPanel = new JPanel();
		BoxLayout lTextFieldLayout = new BoxLayout(lTextFieldPanel, BoxLayout.Y_AXIS);
		lTextFieldPanel.setLayout(lTextFieldLayout);

		BorderLayout lPanelLayout = new BorderLayout();	
		lPanelLayout.addLayoutComponent(lTextFieldPanel, BorderLayout.NORTH);

		//create movie text fields
		//application convention is to map null to empty string (if db has null this will be shown as empty string)
		if(currentMovie.getMovieId() > 0) {
			movieIdField = createLabelTextFieldPair(StringResources.getMovieIdLabel(), ""+currentMovie.getMovieId(), lTextFieldPanel);
			movieIdField.setEditable(false);
		}

		titleField = createLabelTextFieldPair(StringResources.getTitleLabel(), currentMovie.getTitle() == null ? "" : ""+ currentMovie.getTitle(), lTextFieldPanel);
		releaseDateField = createLabelTextFieldPair(StringResources.getReleaseDateLabel(), currentMovie.getReleaseDate() == null ? "" : ""+ currentMovie.getReleaseDate(), lTextFieldPanel);
		if(isUpdatePanel) {
			avgRatingField = createLabelTextFieldPair(StringResources.getAvgRatingLabel(), ""+ currentMovie.getAvgRating(), lTextFieldPanel);
			genreField = createLabelTextFieldPair(StringResources.getGenreLabel(), currentMovie.getGenre() == null ? "" : ""+currentMovie.getGenre(), lTextFieldPanel);
		} else {
			primaryGenreField  = createLabelTextFieldPair(StringResources.getPrimaryGenreLabel(), "", lTextFieldPanel);
			secondaryGenreField  = createLabelTextFieldPair(StringResources.getSecondaryGenreLabel(), "", lTextFieldPanel);
		}
		staffField = createLabelTextFieldPair(StringResources.getStaffLabel(), currentMovie.getStaff() == null ? "" : ""+ currentMovie.getStaff(), lTextFieldPanel);
		//staffField = createLabelTextFieldPair(StringResources.getStaffLabel(), currentMovie.getStaff() == null ? MovieTrackerFrame.loggedInUsername : ""+currentMovie.getStaff(), lTextFieldPanel);
		add(lTextFieldPanel);

		//create description text area
		descriptionArea = new JTextArea(currentMovie.getDescription() == null ? "" : currentMovie.getDescription());
		descriptionArea.setAutoscrolls(true);
		descriptionArea.setLineWrap(true);
		lPanelLayout.addLayoutComponent(descriptionArea, BorderLayout.CENTER);
		add(descriptionArea);
		
		//create movie save (create or update button)
		JButton saveButton = createMovieSaveButton();
		lPanelLayout.addLayoutComponent(saveButton, BorderLayout.SOUTH);
		this.add(saveButton);

		setLayout(lPanelLayout);
		updateUI();
	}

	private JButton createMovieSaveButton() {
		JButton saveButton = new JButton(isUpdatePanel ? StringResources.getMovieUpdateButtonLabel() : 
			StringResources.getMovieAddButtonLabel());
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//application convention is to map empty string to null (i.e. if app has empty string - this will be null in db)
				currentMovie.setTitle(titleField.getText().equals("") ? null : titleField.getText());
				currentMovie.setReleaseDate(releaseDateField.getText().equals("") ? null : releaseDateField.getText());
				currentMovie.setStaff(staffField.getText().equals("") ? null : staffField.getText());
				if(isUpdatePanel) {
					currentMovie.setAvgRating(avgRatingField.getText().equals("") ? null : Float.parseFloat(avgRatingField.getText()));
					currentMovie.setGenre(genreField.getText().equals("") ? null : genreField.getText());
					//currentMovie.setStaff(staffField.getText().equals("") ? null : staffField.getText());
				} else {
					String genre1 = primaryGenreField.getText().equals("") ? "" : primaryGenreField.getText();
					String genre2 = secondaryGenreField.getText().equals("") ? "" : ","+secondaryGenreField.getText();
					currentMovie.setGenre(genre1 + genre2);
				}
				currentMovie.setDescription(descriptionArea.getText().equals("") ? null : descriptionArea.getText());

				if(isUpdatePanel) {
					try {
						BusinessComponentFactory.getInstance().getMovieProvider().updateMovie(currentMovie);
					} catch (SQLException ex) {
						throw new RuntimeException(ex);
					}
				}
				else {
					try {
						BusinessComponentFactory.getInstance().getMovieProvider().addMovie(currentMovie);
					} catch (SQLException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		});
		
		return saveButton;
	}

	private JTextField createLabelTextFieldPair(String label, String value, JPanel container) {
		
		JPanel pairPanel = new JPanel();
		JLabel jlabel = new JLabel(label);
		JTextField field = new JTextField(value);
		pairPanel.add(jlabel);
		pairPanel.add(field);

		container.add(pairPanel);

		BorderLayout lPairLayout = new BorderLayout();
		lPairLayout.addLayoutComponent(jlabel, BorderLayout.WEST);
		lPairLayout.addLayoutComponent(field, BorderLayout.CENTER);
		pairPanel.setLayout(lPairLayout);	
		
		return field;
	}

	/**
	 * Implementation of IMovieSelectionNotifiable::movieSelected used to switch movie
	 * displayed on MovieDisplayPanel
	 */
	@Override
	public void movieSelected(Movie movie) {
		initMovieDetails(movie);
	}
	
	/**
	 * Clear movie details panel
	 */
	public void refresh()
	{
		initMovieDetails(null);
	}
}
