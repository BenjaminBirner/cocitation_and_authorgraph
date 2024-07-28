package Main;



import Controller.Controller;
import Model.CurrentLoad;
import Model.AuthorDBCreator;
import Model.CoCitationDBCreator;
import Model.RelevanceSearch;
import Model.AuthorSearch;
import View.MainFrame;
import Model.SimilaritySearch;



/**
 * This class is the starting-class with the main()-method.
 * The main()-method is the first method that is called up when the program starts.
 * So it is responsible for creating, initializing and connecting all required instances so that the program runs correctly.
 * 
 * @author Benjamin Birner
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		
		
		javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

		SimilaritySearch simiSearch = new SimilaritySearch();
		MainFrame frame = new MainFrame();
		CurrentLoad load = new CurrentLoad();
		CoCitationDBCreator ref = new CoCitationDBCreator();
		AuthorDBCreator head = new AuthorDBCreator();
		RelevanceSearch relSearch = new RelevanceSearch();
		AuthorSearch authSearch = new AuthorSearch();
		
		Controller cont = new Controller(frame, ref, head, load, simiSearch, relSearch, authSearch);

	}

}
