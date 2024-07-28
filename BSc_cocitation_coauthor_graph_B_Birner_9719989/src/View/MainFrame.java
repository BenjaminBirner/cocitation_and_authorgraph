package View;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * This class models the main window for the application.
 * 
 * @author Benjamin Birner
 *
 */
public class MainFrame extends JFrame{
	
	
	//the different menu-points
	private JMenuItem newAnal;
	private JMenuItem load;
	private JMenuItem delete;
	private JMenuItem add;
	private JMenuItem close;
	private JMenuItem similarity;
	private JMenuItem relevance;
	private JMenuItem coauthors;
	private JMenuItem open;

	private final JDesktopPane desktopPane = new JDesktopPane();
	
	
	/**
	 * creates and adds all the required components
	 */
	public MainFrame() {
		setSize(1000, 600);
		setLocationRelativeTo(null);
		addMenuBar();
		initalizeFrame();

	}
	
	
	
	    /**
	     * creates and adds the menus
	     */
		private void addMenuBar () {
			
			JMenuBar menubar = new JMenuBar();
			
			//creates the menu "Datei"
			JMenu file = new JMenu("Datei");
			
			newAnal = new JMenuItem("Neu");
			newAnal.setToolTipText("neue Analyse starten");
			file.add(newAnal);
			
			load = new JMenuItem("Laden");
			load.setToolTipText("bestehende Analyse laden");
			file.add(load);
			
			delete = new JMenuItem("Löschen");
			delete.setToolTipText("bestehende Analyse löschen");
			file.add(delete);
			
			add = new JMenuItem("Hinzufügen");
			add.setToolTipText("bestehende Analyse erweitern");
			file.add(add);
			
			close = new JMenuItem("Beenden");
			close.setToolTipText("Anwendung beenden");
			file.add(close);
			
		
			menubar.add(file);
			
			
			//creates the menu "Suchen"
			JMenu search = new JMenu("Suche");
			
			similarity = new JMenuItem("Ähnlichkeit");
			similarity.setToolTipText("thematisch ähnliche Publikationen suchen");
			search.add(similarity);
			
			relevance = new JMenuItem("Relevanz");
			relevance.setToolTipText("Publikationen mit hoher Relevanz suchen");
			search.add(relevance);
			
			coauthors = new JMenuItem("Koautoren");
			coauthors.setToolTipText("Autor suchen");
			search.add(coauthors);
			
			
			menubar.add(search);
			
			
			
			//creates the menu "Einstellungen"
			JMenu setting = new JMenu("Einstellungen");
			
			open = new JMenuItem("öffnen");
			open.setToolTipText("Einstellungen ändern");
			setting.add(open);
			
			
			menubar.add(setting);
			
			
			
			setJMenuBar(menubar);
			setVisible(true);  
			
		}
	
	
		//initializes the frame with some general settings
		private void initalizeFrame() {
			setTitle("Autoren- und Co-Zitatiosgraphen");
			add(desktopPane); 
			setVisible(true);
		}
		
		
		
		
		/**
		 * adds a JInternalFrame to the desktopPane.
		 * 
		 * @param internalFrame the JInternalFrame that should be added.
		 */
		public void addJInternalFrame(JInternalFrame internalFrame) {
			desktopPane.add(internalFrame);                       
		}      
		
		
		
		
		/**
		 * adds a listener to the <code>close</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setCloseMItemListener(ActionListener listener) {
			close.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>newAnal</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setNewAnalMItemListener(ActionListener listener) {
			newAnal.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>load</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setLoadMItemListener(ActionListener listener) {
			load.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>delete</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setDeleteMItemListener(ActionListener listener) {
			delete.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>add</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setAddMItemListener(ActionListener listener) {
			add.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>similarity</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setSimilarityMItemListener(ActionListener listener) {
			similarity.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>relevance</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setRelevanceMItemListener(ActionListener listener) {
			relevance.addActionListener(listener);
		}
		
		
		
		/**
		 * adds a listener to the <code>coauthors</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setCoauthorsMItemListener(ActionListener listener) {
			coauthors.addActionListener(listener);
		}
		
		
		/**
		 * adds a listener to the <code>setting</code>-JMenuItem.
		 * 
		 * @param listener the listener that should be added.
		 */
		public void setSettingMItemListener(ActionListener listener) {
			open.addActionListener(listener);
		}
		
		
		
		
		
		
		/**
		 * adds a <code>WindowListener</code> to this MainFrame.
		 * 
		 * @param listener the WindowListener that should be added.
		 */
		public void addMainFrameClosingListener(WindowListener listener) {
			addWindowListener(listener);                       
		}      
	

}
