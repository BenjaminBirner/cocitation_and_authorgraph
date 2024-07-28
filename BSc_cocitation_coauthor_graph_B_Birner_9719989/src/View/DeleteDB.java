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
* This class models the Dialog-Frame regarding the interaction with the user for the possibility to delete databases.
* It manages two list to display the existing databases.
* 
* @author Benjamin Birner
*
*/
public class DeleteDB extends JDialog {
	
	//contain the names of the existing databases
	private JList<String> dbAuthList;
	private JList<String> dbCoCitList;
	JScrollPane authScroll = new JScrollPane();
	JScrollPane coCitScroll = new JScrollPane();
	
	private JButton exitB;
	private JButton deleteB;
	
	/**
	 * creates and adds all the required components
	 */
	public DeleteDB() {
		
		setModal(true);
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
	
		
		
		
		JPanel deleteAuthP = new JPanel();
		deleteAuthP.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		deleteAuthP.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JLabel deleteAuthL = new JLabel("Autoren-DB:");
		
		String[] nameAuthList = getAuthorDBs();
		dbAuthList = new JList<String>(nameAuthList);
		dbAuthList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dbAuthList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		authScroll.getViewport().add(dbAuthList);
		authScroll.setPreferredSize(new Dimension(160,80));
		
		
		
		JPanel deleteCoCitP = new JPanel();
		deleteCoCitP.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		deleteCoCitP.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JLabel deleteCoCitL = new JLabel("Co-Zitation-DB:");
		
		String[] nameCoCitList = getCoCitDBs();
		dbCoCitList = new JList<String>(nameCoCitList);
		dbCoCitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dbCoCitList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		coCitScroll.getViewport().add(dbCoCitList);
		coCitScroll.setPreferredSize(new Dimension(160,80));
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		int s = 85;
		if (sys.equals("linux/mac")) {s = 89;}
		
		deleteAuthP.add(deleteAuthL);
		deleteAuthP.add(Box.createRigidArea(new Dimension(s,20)));
		deleteAuthP.add(authScroll);
		
		deleteCoCitP.add(deleteCoCitL);
		deleteCoCitP.add(Box.createRigidArea(new Dimension(72,20)));
		deleteCoCitP.add(coCitScroll);
		
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		deleteB = new JButton("Löschen");
		exitB = new JButton("Abbruch");
		
		buttonP.add(deleteB);
		buttonP.add(Box.createRigidArea(new Dimension(135,0)));	
		buttonP.add(exitB);
		
		
		
		basic.add(deleteAuthP);
		basic.add(deleteCoCitP);
		basic.add(buttonP);
		add(basic);
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Lösche Datenbank");
		setSize(370,380);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
	}
	
	
	
	
	/**
	 * returns the selected value concerning the author-databases.
	 * 
	 * @return the selected value concerning the author-databases.
	 */
	public String getSelectedValueFromAuthDBList() {
		return dbAuthList.getSelectedValue();
	}
	
	
	
	
	/**
	 * returns the selected value concerning the co-citation-databases.
	 * 
	 * @return the selected value concerning the co-citation-databases.
	 */
	public String getSelectedValueFromCoCitDBList() {
		return dbCoCitList.getSelectedValue();
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
	 * refreshes the list after the deleting process
	 */
	public void refreshAuthList() {
		
		String[] list = getAuthorDBs();
		dbAuthList = new JList<String>(list);
		authScroll.getViewport().add(dbAuthList);
	}
	
	
	/**
	 * refreshes the list after the deleting process
	 */
	public void refreshCoCitList() {
		
		String[] list = getCoCitDBs();
		dbCoCitList = new JList<String>(list);
		coCitScroll.getViewport().add(dbCoCitList);
	}
	
	
	
	/**
	 * adds a listener to the delete-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setDeleteButtonListener(ActionListener listener) {  
		deleteB.addActionListener(listener);
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
	 * gets the selected index regarding the db-list 
	 * 
	 * @return the selected index.
	 */
	public int getSelectedDbIndex() {
		return dbAuthList.getSelectedIndex();
	}
	
	

}
