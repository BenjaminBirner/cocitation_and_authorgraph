package Model;

import java.util.Iterator;
import java.util.LinkedList;


import javax.swing.table.DefaultTableModel;


/**
 * This class is the model for the <code>SimilarityJTable<code>.
 * 
 * @author Benjamin Birner
 *
 */
public class SimilarityModel extends DefaultTableModel {
	
	
	/**
	 * This method initializes the Table with the required columns and rows.
	 * 
	 * @param refList This list contains all <code>Publication<code> instances. The data in these instances are required to initialize the columns and rows.
	 */
	public SimilarityModel(LinkedList<Publication> refList) {
		
		//initializes the column names
		addColumn("Titel");
		addColumn("Autoren");
		addColumn("Anzahl Co-Zitationen");
		String[] row = new String[3];
		Iterator<Publication> it = refList.iterator();
		Publication ref;
		
		//initializes the rows
		while( it.hasNext()){
			
			ref = it.next();
				
			row[0] = ref.getTitle();
			row[1] = ref.getAuthors();
			row[2] = ref.getCocitations();
			addRow(row);
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
	      return false;
	   }
	
	
}
