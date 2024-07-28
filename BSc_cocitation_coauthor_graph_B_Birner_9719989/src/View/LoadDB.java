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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import Model.PropertiesCache;



/**
* This class models the Dialog-Frame regarding the interaction with the user for the possibility to load databases.
* It manages two list to display the existing databases.
* 
* @author Benjamin Birner
*
*/
public class LoadDB extends JDialog {
	
	private JList<String> authList;
	private JList<String> coCitList;
	private JButton exitB;
	private JButton loadB;
	
	
	/**
	 * creates and adds all the required components
	 */
	public LoadDB() {
		
		setModal(true);
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
	
		JPanel authP = new JPanel();
		authP.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		authP.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JLabel authL = new JLabel("Autoren-DB:");
		
		String[] authDBsList = getAuthorDBs();
		authList = new JList<String>(authDBsList);
		authList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		authList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane authScroll = new JScrollPane();
		authScroll.getViewport().add(authList);
		authScroll.setPreferredSize(new Dimension(160,80));
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		int s = 100;
		if (sys.equals("linux/mac")) {s = 104;}
		
		authP.add(authL);
		authP.add(Box.createRigidArea(new Dimension(s,20)));
		authP.add(authScroll);
		
		
		
		
		JPanel coCitP = new JPanel();
		coCitP.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		coCitP.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JLabel coCitL = new JLabel("Co-Zitations-DB:");
		
		String[] coCitDBsList = getCoCitDBs();
		coCitList = new JList<String>(coCitDBsList);                             
		coCitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		coCitList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane coCitScroll = new JScrollPane();
		coCitScroll.getViewport().add(coCitList);
		coCitScroll.setPreferredSize(new Dimension(160,80));
		
		coCitP.add(coCitL);
		coCitP.add(Box.createRigidArea(new Dimension(80,20)));
		coCitP.add(coCitScroll);
		
		
		
		
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		loadB = new JButton("Laden");
		exitB = new JButton("Abbruch");
		
		buttonP.add(loadB);
		buttonP.add(Box.createRigidArea(new Dimension(100,0)));	
		buttonP.add(exitB);
		
		
		
		basic.add(authP);
		basic.add(coCitP);
		basic.add(buttonP);
		add(basic);
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Lade Datenbank");
		setSize(387,320);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	/**
	 * This Method fetches the names of the existing Author-Databases from the Properties
	 * 
	 * @return String Array containing the names of the Author-Databases
	 */
	
	private String[] getAuthorDBs() {
		
		PropertiesCache prop = PropertiesCache.getInstance();
		String names = prop.getProperty("author_dbs");
		String[] list;
		
		//decodes the db names from the properties
		if( names == null) {
			
			list = new String[0];
		}else {
			
			int numb = names.length() - names.replace(String.valueOf("$"), "").length();
			list = new String[numb];
			int i = 0;
			int index1 = 0; 
			int index2 = names.indexOf("$");
			
			while( i < numb) {
				
				list[i] = names.substring(index1, index2 );
				
				index1 = index2 + 1;
				index2 = names.indexOf("$", index1);
				i++;
			}
		}
		return list;
	}
	
	
	
	
	
	/**
	 * This Method fetches the names of the existing Co-Citation-Databases from the Properties
	 * 
	 * @return String Array containing the names of the Co-Citation-Databases
	 */
	private String[] getCoCitDBs() {
		
		PropertiesCache prop = PropertiesCache.getInstance();
		String names = prop.getProperty("cocit_dbs");
		String[] list;
		
		//decodes the db names from the properties
		if( names == null) {
			
			list = new String[0];
		}else {
			
			int numb = names.length() - names.replace(String.valueOf("$"), "").length();
			list = new String[numb];
			int i = 0;
			int index1 = 0; 
			int index2 = names.indexOf("$");
			
			while( i < numb) {
				
				list[i] = names.substring(index1, index2 );
				
				index1 = index2 + 1;
				index2 = names.indexOf("$", index1);
				i++;
			}
		}
		return list;
	}
	
	
	
	
	
	
	/**
	 * returns the selected value concerning the list of the author databases.
	 * 
	 * @return the selected value concerning the list of the author databases.
	 */
	public String getSelectedValueFromAuthList() {
		return authList.getSelectedValue();
	}
	
	
	
	
	
	/**
	 * returns the selected value concerning the list of the co-citation databases.
	 * 
	 * @return the selected value concerning the list of the co-citation databases.
	 */
	public String getSelectedValueFromCoCitList() {
		return coCitList.getSelectedValue();
	}
	
	
	
	
	
	
	/**
	 * adds a listener to the load-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setLoadButtonListener(ActionListener listener) {  
		loadB.addActionListener(listener);
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
	 * gets the selected index regarding the author-db-list 
	 * 
	 * @return the selected index.
	 */
	public int getSelectedAuthIndex() {
		return authList.getSelectedIndex();
	}
	
	
	
	/**
	 * gets the selected index regarding the co-citation-db-list 
	 * 
	 * @return the selected index.
	 */
	public int getSelectedCoCitIndi() {
		return coCitList.getSelectedIndex();
	}
	

}
