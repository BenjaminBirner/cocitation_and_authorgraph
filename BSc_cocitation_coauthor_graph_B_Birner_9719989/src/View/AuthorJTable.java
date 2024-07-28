package View;


import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Model.AuthorModel;

/**
 * This class is the Table in the view to display the results of the author-search to the user.
 * 
 * @author Benjamin Birner
 *
 */
public class AuthorJTable extends JInternalFrame {
	
	
private JTable table;
	
	/**
	 * creates and adds the components to the <code>JInternalFrame<code>
	 * 
	 * @param model the model for the table
	 * @param frame the <code>MainFrame<code> object
	 * @param title the title for the <code>JInternalFrame<code>
	 */
	public AuthorJTable(AuthorModel model, MainFrame frame, String title) {
		
		super(title,true,true,true);
		
		//adds this object to the MainFrame
	    frame.addJInternalFrame(this);
		table = new JTable(model);
		JScrollPane sPane = new JScrollPane(table);
		add(sPane);
        pack();
        setSize(985, 540);

        setVisible(true);
	}

}
