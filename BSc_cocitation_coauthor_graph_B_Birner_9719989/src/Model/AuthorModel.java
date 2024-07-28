package Model;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;



/**
 * This class is the model for the <code>AuthorJTable<code>.
 * 
 * @author Benjamin Birner
 *
 */
public class AuthorModel extends DefaultTableModel {
	

	/**
	 * This method initializes the Table with the required columns and rows.
	 * 
	 * @param coList This list contains all <code>CoAuthor<code> instances. The data in these instances are required to initialize the columns and rows.
	 */
	public AuthorModel(LinkedList<CoAuthor> coList) {
		
		//initializes the columns
		addColumn("Koautoren");
		addColumn("Anzahl gemeinsamer Publikationen");
		addColumn("Titel der gemeinsamen Publikationen");
		
		Object[] row = new Object[3];
		Iterator<CoAuthor> it = coList.iterator();
		
		CoAuthor co;
		
		//initializes the rows
		while( it.hasNext()){
			
			co = it.next();
			LinkedList<String> titList = co.getTitleList();
			
			row[0] = co.getName();
			row[1] = co.getNumberOfPublications();
			row[2] = titList.get(0);
		
			addRow(row);
			titList.remove(0);
			
			for(String title : titList) {
				
				row[0] = "";
				row[1] = "";
				row[2] = title;
			
				addRow(row);
			}
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
	      return false;
	   }

}
