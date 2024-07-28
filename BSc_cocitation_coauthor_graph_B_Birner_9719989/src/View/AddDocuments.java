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


/**
* This class models the Dialog-Frame regarding the possibility to add further publications to an existing database.
* 
* @author Benjamin Birner
*
*/
public class AddDocuments extends JDialog {

	//specified the path to the folder that contains the PDF documents
	private String path = "-1";
	private JButton chooseB;
	private JButton addB;
	private JButton exitB;
	
	/**
	 * initializes all components and adds the components to the <code>JDialog<code>.
	 */
	public AddDocuments() {
		
		setModal(true);
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
	
		//creates and adds the choose-button-panel
		JPanel chooseP = new JPanel();
		chooseP.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		chooseP.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
	    
		//creates the labels
		JLabel chooseL = new JLabel("Ordner:");
		chooseB = new JButton("wählen");
		
		chooseP.add(chooseL);
		chooseP.add(Box.createRigidArea(new Dimension(144,0)));
		chooseP.add(chooseB);
		
		

		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		
		//creates buttons
		addB = new JButton("Hinzufügen");
		exitB = new JButton("Abbruch");
		
		
		buttonP.add(addB);
		buttonP.add(Box.createRigidArea(new Dimension(87,0)));	
		buttonP.add(exitB);
		
		
		
		basic.add(chooseP);
		basic.add(buttonP);
		add(basic);
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Dokumente Hinzufügen");
		setSize(320,160);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	
	
	
	/**
	 * sets the path to the folder that contains the pdfs
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		
		this.path = path;
	}
	
	
	/**
	 * returns the path to the folder that contains the pdfs.
	 * @return
	 */
	public String getPath() {
		
		return path;
	}
	
	
	/**
	 * adds a listener to the add-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setAddButtonListener(ActionListener listener) {  
		addB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the exit-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setExitButtonListener(ActionListener listener) {  
		exitB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the choose-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setChooseButtonListener(ActionListener listener) {  
		chooseB.addActionListener(listener);
	}
	
	
	
	
}
