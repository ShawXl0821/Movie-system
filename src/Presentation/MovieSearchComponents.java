package Presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * Search text field and search text button
 * @author IwanB
 *
 */
public class MovieSearchComponents extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2482895318694258164L;
	
	private JButton searchButton = new JButton(StringResources.getFindButtonLabel());
	private JTextField searchField = new JTextField();
	
	/**
	 * 
	 * @param searchEventListener listener to invoke when search occurs
	 */
	public MovieSearchComponents(final ISearchMovieListener searchEventListener)
	{

		
		this.add(searchField);
		this.add(searchButton);
		
		BorderLayout verticalLayout = new BorderLayout();
		this.setLayout(verticalLayout);
		
		verticalLayout.addLayoutComponent(searchField, BorderLayout.NORTH);
		verticalLayout.addLayoutComponent(searchButton, BorderLayout.SOUTH);

		searchButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					searchEventListener.searchClicked(searchField.getText());
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	}
	
}
