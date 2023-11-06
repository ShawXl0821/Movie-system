package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Business.Movie;

/**
 * Panel encapsulating movie list
 * @author IwanB
 *
 */
public class MovieListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1013855025757989473L;
	
	private List<IMovieSelectionNotifiable> notifiables = new ArrayList<IMovieSelectionNotifiable>();
	private Vector<Movie> movies;
	
	/**
	 * 
	 * @param events vector of events to display in the event list panel
	 */
	public MovieListPanel(Vector<Movie> movies)
	{
		this.movies = movies;
		this.setBorder(BorderFactory.createLineBorder(Color.black));	
		initList(this.movies);
	}

	private void initList(Vector<Movie> movies) {
		
		final JList<Movie> list = new JList<Movie>(movies);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);
		this.add(listScroller);
		
		BorderLayout listLayout = new BorderLayout();
		listLayout.addLayoutComponent(listScroller, BorderLayout.CENTER);
		this.setLayout(listLayout);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				for(IMovieSelectionNotifiable notifiable : notifiables)
				{
					Movie selectedMovie = list.getSelectedValue();
					if(selectedMovie != null)
					{
						notifiable.movieSelected(selectedMovie);
					}
				}
			}		
		});
	}
	
	/**
	 * Refresh movie list to display vector of event objects
	 * @param movies - vector of movie objects to display
	 */
	public void refresh(Vector<Movie> movies)
	{
		this.removeAll();
		this.initList(movies);
		this.updateUI();
		this.notifiables.clear();
	}
	
	/**
	 * Register an object to be notified of a event selection change
	 * @param notifiable object to invoke when a new event is selected
	 */
	public void registerEventSelectionNotifiableObject(IMovieSelectionNotifiable notifiable)
	{
		notifiables.add(notifiable);
	}

}
