package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Model.PropertiesCache;



/**
* This class models the Dialog-Frame regarding the possibility to carry out a new analysis and create a new database.
* 
* @author Benjamin Birner
*
*/
public class NewDB extends JDialog {
	
	private JTextField nameDBTf;
	private JRadioButton authorR;
    private JRadioButton citationR;
    private JButton folderB;
    private JButton exitB;
    private JButton startB;
    private String path = "-1";
	
	
    /**
	 * initializes all components and adds the components to the <code>JDialog<code>.
	 */
	public NewDB() {
		
		setModal(true);
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		
		
		
		JPanel nameDBP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		nameDBP.setBorder(BorderFactory.createEmptyBorder(42,60,5,60));
		
		
		JLabel nameDBL = new JLabel("Name der Datenbank:");
		nameDBTf = new JTextField(11);
		nameDBTf.setEditable(true);
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		int s = 80;
		if (sys.equals("linux/mac")) {s = 70;}
		
		nameDBP.add(nameDBL);
		nameDBP.add(Box.createRigidArea(new Dimension(s,0)));	
		nameDBP.add(nameDBTf);
		
		
		
		
		
		
		
		
		
		JPanel typP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		typP.setBorder(BorderFactory.createEmptyBorder(12,60,0,60));
		
		JPanel typRP = new JPanel();
		typRP.setLayout(new BoxLayout(typRP, BoxLayout.Y_AXIS));
		
		JLabel typ = new JLabel("Analyseart:");
		authorR = new JRadioButton("Autorenanalyse");
		citationR = new JRadioButton("Co-Zitationsanalyse");
		ButtonGroup group = new ButtonGroup();
	    authorR.setSelected(true);
		group.add(authorR);
		group.add(citationR);
		
		s = 125;
		if (sys.equals("linux/mac")) {s = 129;}
		
		typP.add(typ, BorderLayout.WEST);
		typRP.add(authorR);
		typRP.add(citationR);
		typP.add(Box.createRigidArea(new Dimension(s,0)));	
		typP.add(typRP, BorderLayout.EAST);
		
		
		
		JPanel folderP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		folderP.setBorder(BorderFactory.createEmptyBorder(42,60,5,60));
		
		JLabel folderL = new JLabel("Dokumente:");
		folderB = new JButton("Ordner wählen");
		
		folderP.add(folderL);
		folderP.add(Box.createRigidArea(new Dimension(127,0)));	
		folderP.add(folderB);
		
		
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		startB = new JButton("Start");
		exitB = new JButton("Abbruch");
		
		buttonP.add(startB);
		buttonP.add(Box.createRigidArea(new Dimension(100,0)));	
		buttonP.add(exitB);
		
		basic.add(typP);
		basic.add(folderP);
		basic.add(nameDBP);
		basic.add(buttonP);
		
		//adds the basic-Panel to the JDialog
		add(basic);
		
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Neue Datenbank erstellen");
		setSize(475,300);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	
	/**
	 * gets the text-field-entry concerning the nameDB-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getnameDBTf() {
		return nameDBTf.getText();
	}
	
	
	
	/**
	 * gets the selected analysis-type
	 * 
	 * @return "a" if author-analysis is selected and "c" if co-citation-analysis is selected. If nothing is selected then it returns null.
	 */
	public String getChartTyp() {
		if(authorR.isSelected()) {
			return "a";
		}
		if(citationR.isSelected()) {
			return "c";
		}
		return null;
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
	 * adds a listener to the start-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setStartButtonListener(ActionListener listener) {  
		startB.addActionListener(listener);
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
		folderB.addActionListener(listener);
	}
	
	
	

}
