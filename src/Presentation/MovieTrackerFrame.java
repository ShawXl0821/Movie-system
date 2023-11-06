package Presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Business.BusinessComponentFactory;
import Business.Movie;

public class MovieTrackerFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5532618722097754725L;
	
	private AddEntitiesPanel addEntitiesPanel = null;
	private MovieDetailPanel detailPanel = null;
	private MovieSidePanel sidePanel = null;


	static String loggedInUsername = null;

	/**
	 * Main movie tracker frame
	 * Logs on the user
	 * Initialize side panel + add entities panel + containing event list + detail panel
	 */
	public MovieTrackerFrame() throws SQLException {
		setTitle(StringResources.getAppTitle());
	    setLocationRelativeTo(null);
	    
	    logOnUser();
	    initialise();
	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);  
	}
	
	/**
	 *  !!! 
	 *  Only used to simulate logon
	 *  This should really be implemented using proper salted hashing
	 *	and compare hash to that in DB
	 *  really should display an error message for bad login as well
	 *	!!!
	 */
	private void logOnUser() throws SQLException {
		boolean OK = false;
		while (!OK) {		
				String userName = (String)JOptionPane.showInputDialog(
									this,
									null,
									StringResources.getEnterUserNameString(),
									JOptionPane.QUESTION_MESSAGE);
				
				JPasswordField jpf = new JPasswordField();
				int okCancel = JOptionPane.showConfirmDialog(
									null,
									jpf,
									StringResources.getEnterPasswordString(),
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
				
				String password = null;
				if (okCancel == JOptionPane.OK_OPTION) {
					password = new String(jpf.getPassword());
				}

				if (userName == null || password == null)
					System.exit(0);
				else
					if (!userName.isEmpty() && !password.isEmpty()) {
						loggedInUsername = checkAdmCredentials(userName, password);
						if (loggedInUsername != null) {
							OK = true;
						}
						// show an error message
						else{
							JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
		}
	}

	private void initialise() throws SQLException {
		addEntitiesPanel = new AddEntitiesPanel();	
	    detailPanel = new MovieDetailPanel(true);	    
	    sidePanel = getSidePanel(new MovieListPanel(getAllMovies()));
	    
	    BorderLayout borderLayout = new BorderLayout();
	    borderLayout.addLayoutComponent(addEntitiesPanel, BorderLayout.NORTH);
	    borderLayout.addLayoutComponent(sidePanel, BorderLayout.WEST);
	    borderLayout.addLayoutComponent(detailPanel, BorderLayout.CENTER);
	    
	    JButton refreshButton = new JButton(StringResources.getRefreshButtonLabel());
	    final MovieTrackerFrame frame = this;
	    refreshButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					frame.refresh(frame.getAllMovies());
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	    
	    borderLayout.addLayoutComponent(refreshButton, BorderLayout.SOUTH);
	    
	    this.setLayout(borderLayout);
	    this.add(addEntitiesPanel);
	    this.add(refreshButton);
	    this.add(sidePanel);
	    this.add(detailPanel);
	    this.setSize(600, 300);
	}
	
	private MovieSidePanel getSidePanel(MovieListPanel listPanel)
	{
		final MovieTrackerFrame frame = this;
		listPanel.registerEventSelectionNotifiableObject(detailPanel);
		return new MovieSidePanel(new ISearchMovieListener() {
			@Override
			public void searchClicked(String searchString) throws SQLException {
				frame.refresh(frame.findMoviesByTitle(searchString));
			}
		},listPanel);
	}
	
	private String checkAdmCredentials(String userName, String password) throws SQLException {
		return BusinessComponentFactory.getInstance().getMovieProvider().checkStaffCredentials(userName, password);
	}
	
	private Vector<Movie> getAllMovies() throws SQLException {
		return BusinessComponentFactory.getInstance().getMovieProvider().findMoviesByStaff(loggedInUsername);
	}
	
	private Vector<Movie> findMoviesByTitle(String pSearchString) throws SQLException {
		pSearchString = pSearchString.trim();
		if (!pSearchString.equals(""))
			return BusinessComponentFactory.getInstance().getMovieProvider().findMoviesByCriteria(pSearchString);
		else
			return BusinessComponentFactory.getInstance().getMovieProvider().findMoviesByStaff(loggedInUsername);
	}
	
	private  void refresh(Vector<Movie> movies)
	{
		if(sidePanel != null && detailPanel!= null)
		{
			sidePanel.refresh(movies);
			detailPanel.refresh();
			sidePanel.registerEventSelectionNotifiableObject(detailPanel);
		}
	}
	
	
	public static void main(String[] args)
	{
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
				MovieTrackerFrame ex = null;
				try {
					ex = new MovieTrackerFrame();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				ex.setVisible(true);
            }
        });		
	}
}
