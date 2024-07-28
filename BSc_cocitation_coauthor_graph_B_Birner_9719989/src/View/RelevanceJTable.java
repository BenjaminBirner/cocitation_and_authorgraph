package View;


import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Model.RelevanceModel;

/**
 * This class is the Table in the view to display the results of the relevance-search to the user.
 * 
 * @author Benjamin Birner
 *
 */
public class RelevanceJTable extends JInternalFrame {

	
	private JTable table;
	
	
	/**
	 * creates and adds the components to the <code>JInternalFrame<code>
	 * 
	 * @param model the model for the table
	 * @param frame the <code>MainFrame<code> object
	 * @param title the title for the <code>JInternalFrame<code>
	 */
	public RelevanceJTable(RelevanceModel model, MainFrame frame, String title) {
		
		super(title,true,true,true);
		
	
	    frame.addJInternalFrame(this);
		
	    TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model); 
	
	    rowSorter.setSortable(0, true);
	    rowSorter.setSortable(1, false);
	    rowSorter.setSortable(2, true);
	    rowSorter.setSortable(3, true);
	    
		table = new JTable(model);
		table.setRowSorter(rowSorter);
		table.getRowSorter().toggleSortOrder(2);
		table.getRowSorter().toggleSortOrder(2);
		
		JScrollPane sPane = new JScrollPane(table);
		add(sPane);
        pack();
        
        setSize(985, 540);

        setVisible(true);
	}
	
	
	
}
