package View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;

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
* This class models the Dialog-Frame regarding the interaction with the user for the similarity-search.
* 
* 
* @author Benjamin Birner
*
*/
public class SimilaritySearchDialog extends JDialog{
	
	private JTextField titleTf;
    private JButton fileB;
    private JButton exitB;
    private JButton searchB;
    private File file = null;
    
    /**
	 * creates and adds all the required components
	 */
    public SimilaritySearchDialog() {
		
		setModal(true);
		
		
		
		
		JPanel textP1 = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		textP1.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
		JPanel textP2 = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		textP2.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
		JPanel textP3 = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		textP3.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
		
		JLabel textL1 = new JLabel("Geben Sie hier entweder den Titel der Publikation");
		JLabel textL2 = new JLabel("ODER");
		JLabel textL3 = new JLabel("die Publikation selbst an: ");
		textP1.add(textL1);
		textP2.add(textL2);
		textP3.add(textL3);
		
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		
		
		
		JPanel titleP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		titleP.setBorder(BorderFactory.createEmptyBorder(40,20,5,20));
		
		
		JLabel titleL = new JLabel("Titel der Publikation:");
		titleTf = new JTextField(9);
		titleTf.setEditable(true);
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		int s = 117;
		if (sys.equals("linux/mac")) {s = 103;}
		
		titleP.add(titleL);
		titleP.add(Box.createRigidArea(new Dimension(s,0)));	
		titleP.add(titleTf);
		
		
	
		
		
		JPanel fileP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		fileP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		JLabel fileL = new JLabel("Publikation:");
		fileB = new JButton("PDF wählen");
		
		fileP.add(fileL);
		fileP.add(Box.createRigidArea(new Dimension(158,0)));	
		fileP.add(fileB);
		
		

		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		searchB = new JButton("Suchen");
		exitB = new JButton("Abbrechen");
		
		buttonP.add(searchB);
		buttonP.add(Box.createRigidArea(new Dimension(130,0)));	
		buttonP.add(exitB);
		
		basic.add(textP1);
		basic.add(textP2);
		basic.add(textP3);
		basic.add(titleP);
		basic.add(fileP);
		basic.add(buttonP);
		
		//adds the basic-Panel to the JDialog
		add(basic);
		
		s = 370;
		if (sys.equals("linux/mac")) {s = 400;}
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Ähnliche Publikationen Suchen");
		setSize(s,300);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}
    
    
    /**
	 * gets the text-field-entry concerning the nameDB-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getTitleTf() {
		return titleTf.getText();
	}
	
	
	
	
	/**
	 * sets the file 
	 * 
	 * @param path
	 */
	public void setFile(File file) {
		
		this.file = file;
	}
	
	
	/**
	 * returns the file
	 * @return
	 */
	public File getFile() {
		
		return file;
	}
	
	
	
	
	
	/**
	 * adds a listener to the start-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setStartButtonListener(ActionListener listener) {  
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
	
	
	
	/**
	 * adds a listener to the folder-select-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setFolderSelectButtonListener(ActionListener listener) {  
		fileB.addActionListener(listener);
	}
	
	
	

}
