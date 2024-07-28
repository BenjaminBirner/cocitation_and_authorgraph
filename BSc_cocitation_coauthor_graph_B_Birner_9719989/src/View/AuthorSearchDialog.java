package View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.PropertiesCache;


/**
* This class models the Dialog-Frame regarding the interaction with the user for the author-search.
* 
* @author Benjamin Birner
*
*/
public class AuthorSearchDialog extends JDialog {

	
	private JTextField authorTf;
	private JButton searchB;
	private JButton exitB;
	
	
	/**
	 * creates and adds all the required components
	 */
	public AuthorSearchDialog() {
		setModal(true);
	
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
	
		JPanel authorP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		authorP.setBorder(BorderFactory.createEmptyBorder(30,25,10,20));
		
		
		JLabel authorL = new JLabel("Autor:");
		authorTf = new JTextField(11);
		authorTf.setEditable(true);
	
		authorP.add(authorL);
		authorP.add(Box.createRigidArea(new Dimension(120,0)));	
		authorP.add(authorTf);
		
		
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		searchB = new JButton("Suchen");
		exitB = new JButton("Abbrechen");
		
		buttonP.add(searchB);
		buttonP.add(Box.createRigidArea(new Dimension(85,0)));	
		buttonP.add(exitB);
		
		
		
		basic.add(authorP);
		basic.add(buttonP);
		add(basic);
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		int s = 320;
		if (sys.equals("linux/mac")) {s = 350;}
		
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Koautoren und Publikationen suchen");
		setSize(s,160);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
	}
	
	
	
	/**
	 * gets the text-field-entry concerning the author-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getAuthorTf() {
		return authorTf.getText();
	}
	
	
	
	/**
	 * adds a listener to the search-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setSearchButtonListener(ActionListener listener) {  
		searchB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the exit-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setExitButtonListener(ActionListener listener) {  
		exitB.addActionListener(listener);
	}
	
	
	
	
}
