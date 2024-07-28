package Model;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;


/**
 * This class is the model for the <code>RelevanceJTable<code>.
 * 
 * @author Benjamin Birner
 *
 */
public class RelevanceModel extends DefaultTableModel {

	
	/**
	 * This method initializes the Table with the required columns and rows.
	 * 
	 * @param refList This list contains all <code>Publication<code> instances. The data in these instances are required to initialize the columns and rows.
	 */
	public RelevanceModel(LinkedList<Publication> refList) {
		
		//initializes the column names
		addColumn("Titel");
		addColumn("Autoren");
		addColumn("Anzahl Co-Zitationen gesamt");
		addColumn("Anzahl verschiedener Co-Zitationen");
		Object[] row = new Object[4];
		Iterator<Publication> it = refList.iterator();
		Publication ref;
		int i = 0;
		
		//initializes the rows
		while( it.hasNext() && i < 100){
			
			ref = it.next();

			row[0] = ref.getTitle();
			row[1] = ref.getAuthors();
			row[2] = Integer.parseInt(ref.getCocitations());
			row[3] = Integer.parseInt(ref.getDegree());
			addRow(row);
			i++;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
	      return false;
	   }
	
	@Override
	public Class<?> getColumnClass(int colIndex){
		if(colIndex == 0 || colIndex == 1) { return String.class;}
		else {return Integer.class;}                           
	}
	
	
}
