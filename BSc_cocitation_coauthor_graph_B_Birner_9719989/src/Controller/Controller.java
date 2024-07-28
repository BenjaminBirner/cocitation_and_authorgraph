package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import Model.CoAuthor;
import Model.CurrentLoad;
import Model.AuthorDBCreator;
import Model.PropertiesCache;
import Model.Publication;
import Model.CoCitationDBCreator;
import Model.RelevanceSearch;
import Model.RelevanceModel;
import View.AddDocuments;
import View.AuthorJTable;
import View.AuthorSearchDialog;
import View.DeleteDB;
import View.LoadDB;
import View.MainFrame;
import View.NewDB;
import View.RelevanceJTable;
import View.SettingDialog;
import View.SimilarityJTable;
import Model.SimilaritySearch;
import Model.SimilarityModel;
import View.SimilaritySearchDialog;
import Model.AuthorSearch;
import Model.AuthorModel;






/**
 * this class organizes the event-handling concerning all <code>JMenuItem</code>s that the <code>JMenuBar</code>
 * in the class {@link #MainFrame} contains as well as the Frames and Dialogs
 * that can be opened with these JMenuItems.
 * So, it manages the view, analyzes the user input
 * and induces the suitable actions concerning the model.
 * This class contains several inner classes that implement the <code>ActionListener</code> interface and
 * handle the occurred events.
 * 
 * @author Benjamin Birner
 *
 */
public class Controller {
	
	private MainFrame frame;
	private CoCitationDBCreator refEx;
	private AuthorDBCreator headEx;
	private CurrentLoad load;
	private SimilaritySearch simiSearch;
	private RelevanceSearch relSearch;
	private AuthorSearch authSearch;
	
	public Controller(MainFrame frame, CoCitationDBCreator refEx, AuthorDBCreator headEx, CurrentLoad load, 
			SimilaritySearch simiSearch, RelevanceSearch relSearch, AuthorSearch authSearch) {
		
		this.frame = frame;
		this.refEx = refEx;
		this.headEx = headEx;
		this.load = load;
		this.simiSearch = simiSearch;
		this.relSearch = relSearch;
		this.authSearch = authSearch;
		
		//adds all listeners to the MainFrame
		addMainFrameListener();
	}
	
	
	//adds all listeners to the MainFrame
	private void addMainFrameListener() {
		frame.addMainFrameClosingListener(new MainFrameClosingListener());
		frame.setCloseMItemListener(new CloseFrameListener());
		frame.setNewAnalMItemListener(new NewAnalysisListener());
		frame.setLoadMItemListener(new LoadAnalysisListener());
		frame.setDeleteMItemListener(new DeleteAnalysisListener());
		frame.setAddMItemListener(new AddDocumentsListener());
		frame.setSimilarityMItemListener(new SimilaritySearchDialogListener());
		frame.setRelevanceMItemListener(new RelevanceSearchListener());
		frame.setCoauthorsMItemListener(new AuthorSearchDialogListener());
		frame.setSettingMItemListener(new SettingDialogListener());
		
	}
	
	
	/*
	 * The method in this class is called up when the MainFrame will be closed respectively when the application will be closed via
	 * the frame icon.
	 * It stores the manipulated information in the property file.
	 */
	private class MainFrameClosingListener extends WindowAdapter{
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			PropertiesCache prop = PropertiesCache.getInstance();
			try {
				prop.store();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.exit(0);;
		}
	}
	
	
	
	/*
	 * The method in this class is called up when the JMenuItem "Beenden" regarding the MainFrame is pushed.
	 * It stores the manipulated information in the property file.
	 */
	private class CloseFrameListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			PropertiesCache prop = PropertiesCache.getInstance();
			try {
				prop.store();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.exit(0);;
		}
	}
	
	
	
	
	
	/*
	 * The method in this class is called up when in a dialog the "Abbrechen"-Button is pushed.
	 * It closes the dialog.
	 */
	private class ExitButtonListener implements ActionListener{
		
		private JDialog dia;
		
		private ExitButtonListener(JDialog dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			dia.dispose();
		}
	}
	
	
	
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Neu" is pushed.
	 * It creates a NewAnalysis-Object and adds all listeners
	 */
	private class NewAnalysisListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			NewDB analDia = new NewDB();
			analDia.setStartButtonListener(new StartCreatingDBListener(analDia));
			analDia.setExitButtonListener(new ExitButtonListener(analDia));
			analDia.setFolderSelectButtonListener(new FolderSelectionButtonListener(analDia));
			analDia.setVisible(true);
		}
	}
	
	
	
	
	
	
	/*
	 * The method in this class is called up when the Button to select a folder is pushed.
	 * It opens the dialog to select the folder which contains the pdfs.
	 * 
	 */
	private class FolderSelectionButtonListener implements ActionListener{
		
		private NewDB dia;
		
		private FolderSelectionButtonListener(NewDB dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			JFileChooser chooser = new JFileChooser();
			
			FileFilter filter = new FileNameExtensionFilter("pdf","pdf");
			chooser.setFileFilter(filter);
			
			int value = chooser.showOpenDialog(null);
			
			if(value == JFileChooser.APPROVE_OPTION) {
				
				dia.setPath(chooser.getCurrentDirectory().getAbsolutePath());
			}
		
		}
	}
	
	
	
	
	
	
	
	
	/*
	 * The method in this class is called up if the Button to start a new analysis is pushed.
	 * It carries out the the corresponding analysis according to the users input.
	 */
	private class StartCreatingDBListener implements ActionListener{
		
		private NewDB dia;
		
		private StartCreatingDBListener(NewDB dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			//getting the users input
			String type = dia.getChartTyp();
			String name = dia.getnameDBTf();
			String path = dia.getPath();
			
			try {
				//checks if all the entries are done
				if( name.equals("") || name == null || path.equals("-1")) {
				
					throw new IllegalArgumentException("Unvollständige Eingabe!");
				}
				
				JOptionPane.showMessageDialog(null, "Die Erzeugung der Datenbank kann einige Zeit in Anspruch nehmen."
						+ "Sobald die Datenbank erfolgreich angelegt wurde, erfolgt eine Meldung.");
				
				dia.dispose();
				//initiating the analysis
				if( type.equals("c")) {
					
					refEx.createDB(path, name, "new");
					load.setType(1);
				}else {
					
					headEx.createDB(path, name, "new");
					load.setType(0);
				}
				
				load.setDB(name);
				JOptionPane.showMessageDialog(null, "Die Datenbank wurde erfolgerich angelegt.");
				
				
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte vervollständigen Sie die Eingabe!", "Unvollständige Eingabe!",JOptionPane.ERROR_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null,  "Beim Erzeugen der Datenbank ist ein Fehler aufgetreten","Fehlermeldung",JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Laden" is pushed.
	 * It creates a LoadDB-Object and adds all listeners.
	 */
	private class LoadAnalysisListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			LoadDB loadDia = new LoadDB();
			loadDia.setLoadButtonListener(new LoadButtonListener(loadDia));
			loadDia.setExitButtonListener(new ExitButtonListener(loadDia));
			loadDia.setVisible(true);
		}
	}
	
	
	
	
	/*
	 * The method in this class is called up if in a dialog the "Load"-Button is pushed.
	 * It loads the selected database
	 */
	private class LoadButtonListener implements ActionListener{
		
		private LoadDB dia;
		
		private LoadButtonListener(LoadDB dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			
			//getting the user´s input
			String authDB = dia.getSelectedValueFromAuthList();
			String coCitDB = dia.getSelectedValueFromCoCitList();
			
			try {
				//checks if the entries are correct
				if( authDB != null && coCitDB != null) {
					
					throw new IllegalArgumentException("Mehr als eine Datenbank wurde selektiert!");
				}else {
					
					//setting the new information
					if(authDB != null) {
						
						load.setDB(authDB);
						load.setType(0);
						
					}else {
						
						load.setDB(coCitDB);
						load.setType(1);
					}
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte nur eine Datenbank selektieten!", "Mehr als eine Datenbank wurde selektiert!",JOptionPane.ERROR_MESSAGE);
			}
			dia.dispose();
		}
	}
	
	
	
	
	
	
	/*
	 * The method in this class is called up when the JMenuItem "Löschen" is pushed.
	 * It creates a DeleteDB-Object and adds all listeners.
	 */
	private class DeleteAnalysisListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			DeleteDB deleteDia = new DeleteDB();
			deleteDia.setDeleteButtonListener(new DeleteDBButtonListener(deleteDia));
			deleteDia.setExitButtonListener(new ExitButtonListener(deleteDia));
			deleteDia.setVisible(true);
		}
	}
	
	
	
	
	
	
	/*
	 * The method in this class is called up when the Button to delete a database is pushed.
	 * It deletes the corresponding database.
	 */
	private class DeleteDBButtonListener implements ActionListener{
		
		private DeleteDB dia;
		
		private DeleteDBButtonListener(DeleteDB dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			int n = JOptionPane.showConfirmDialog(null,"Möchten Sie die Datenbank wirklich unwiederruflich löschen?", "Löschung bestätigen", JOptionPane.YES_NO_OPTION);
			
			if( n == 0) {
				
				//getting the user's input
				String auth = dia.getSelectedValueFromAuthDBList();
				String cocit = dia.getSelectedValueFromCoCitDBList();
			
				try {
				    
					//checks if the user's input is correct
					if( auth != null  && cocit != null) {
					
						throw new IllegalArgumentException("Mehr als eine Datenbank wurde selektiert!");
					}else {
					
						if( auth != null) {
						
							//deleting the database if it is a author database
							deleteDB("author_db", auth);
							deleteProperty("author_dbs", auth);
							dia.refreshAuthList();
							
							//checking if this database is loaded
							if( load.getDB().equals(auth)) {
								load.setDB("-1");
								load.setType(-1);
							}
						}else {
						
							//deleting the database if it is a co-citation database
							deleteDB("co_citation_db", cocit);
							deleteProperty("cocit_dbs", cocit);
							dia.refreshCoCitList();
							
							//checking if this database is loaded
							if( load.getDB().equals(cocit)) {
								load.setDB("-1");
								load.setType(-1);
							}
						}
						
					}
				}catch(IllegalArgumentException ae) {
					JOptionPane.showMessageDialog(null,  "Bitte nur eine Datenbank selektieren!", "Mehr als Datenbank wurde selektiert!",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	
	
	/*
	 * deletes the database name from the property
	 */
	private void deleteProperty(String key, String nameDB) {
		
		PropertiesCache prop = PropertiesCache.getInstance();
		String names = prop.getProperty(key);
		
		names = names.replace(nameDB + "$", "");
		prop.setProperty(key, names);
		
	}
	
	
	
	
	/*
	 * deletes the database 
	 */
	private void deleteDB(String db, String name) {
		
		String db_path = System.getProperty("user.dir") + File.separator + db + File.separator + name;
		Path paths =  Paths.get(db_path);
		
		try (Stream<Path> walk = Files.walk(paths)) {
		    walk.sorted(Comparator.reverseOrder()).forEach(path -> {
		        try {
		            Files.delete(path);
		        } catch (IOException ex) {
		        	JOptionPane.showMessageDialog(null,  "Eine IOException ist aufgetreten","Fehlermeldung",JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		        }
		    });
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,  "Eine IOException ist aufgetreten","Fehlermeldung",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	/*
	 * The method in this class is called up when the JMenuItem "Hinzufügen" is pushed.
	 * It creates a AddDocuments-Object and adds all listeners.
	 */
	private class AddDocumentsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			AddDocuments addDia = new AddDocuments();
			addDia.setAddButtonListener(new AddDocButtonListener(addDia));
			addDia.setChooseButtonListener(new ChooseFolderButtonListener(addDia));
			addDia.setExitButtonListener(new ExitButtonListener(addDia));
			addDia.setVisible(true);
		}
	}
	
	
	
	
	
	/*
	 * The method in this class is called up if the "Hinzufügen" Button is pushed.
	 * It fetches the PDF files and adds it to the selected database.
	 */
	private class AddDocButtonListener implements ActionListener{
		
		private AddDocuments dia;
		
		private AddDocButtonListener(AddDocuments dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			//getting the data regarding the database which is loaded
			String db = load.getDB();
			int type = load.getType();
			
			try {
				//checking if a database is loaded
				if( db.equals("-1") || type == -1) {
				
					dia.dispose();
					throw new IllegalArgumentException("Keine Datenbank geladen!");
				}else {
					
			
					String path = dia.getPath();
					
					try {
						
						//checking if a folder that contains the PDFs is selected
						if( path.equals("-1")) {
							
							throw new IllegalArgumentException("Keine Ordner ausgewählt!");
						}else {
							
							JOptionPane.showMessageDialog(null, "Die Erweiterung der Datenbank kann einige Zeit in Anspruch nehmen. "
									+ "Sobald die Datenbank erfolgreich erweitert wurde, erfolgt eine Meldung.");
							
							dia.dispose();
							
							if( type == 0) {
								
								//adding the extracted information to the author database
								headEx.createDB(path, db, "extend");
							}
							
							if ( type == 1) {
								
								//adding the extracted information to the co-citation database
								refEx.createDB(path, db, "extend");
							}
							
							JOptionPane.showMessageDialog(null, "Die Datenbank wurde erfolgerich angelegt.");
						}
					
					}catch(IllegalArgumentException ae) {
						JOptionPane.showMessageDialog(null,  "Bitte wählen Sie den Ordner, welcher die PDF-Dateien enthält!", "Keine Ordner geladen!",JOptionPane.ERROR_MESSAGE);
						ae.printStackTrace();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,  "Beim Erweitern der Datenbank ist ein Fehler aufgetreten","Fehlermeldung",JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte laden Sie die Datenbank, welche erweitert werden soll!", "Keine Datenbank geladen!",JOptionPane.ERROR_MESSAGE);
				ae.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	/*
	 * The method in this class is called up if the Button to select a folder is pushed.
	 * It opens the dialog to select the folder which contains the pdfs.
	 * 
	 */
	private class ChooseFolderButtonListener implements ActionListener{
		
		private AddDocuments dia;
		
		private ChooseFolderButtonListener(AddDocuments dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			JFileChooser chooser = new JFileChooser();
			
			FileFilter filter = new FileNameExtensionFilter("pdf","pdf");
			chooser.setFileFilter(filter);
			
			int value = chooser.showOpenDialog(null);
			
			if(value == JFileChooser.APPROVE_OPTION) {
				
				dia.setPath(chooser.getCurrentDirectory().getAbsolutePath());
			}
		
		}
	}
	
	
	

	
	
	/*
	 * The method in this class is called up if the JMenuItem "Relevanz" is pushed.
	 * It selects the most relevant publications form the co-citation database and
	 * displays it to the user.
	 * 
	 */
	private class RelevanceSearchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			//getting the information of the loaded database
			String nameDB = load.getDB();
			int type = load.getType();
			
			try {
				
				//checking if a database is loaded
				if( nameDB.equals("-1") || type == -1) {
					
					throw new IllegalArgumentException("Keine Datenbank geladen!");
				}else {
					try {
					
						//checking if a author database is loaded
						if( type == 0) {
						
							throw new IllegalArgumentException("Autoren-Datenbank ist geladen!");
						}else {
						
							//It selects the most relevant publications form the co-citation database and displays it to the user.
							RelevanceModel model = new RelevanceModel(relSearch.relevanceSearch(nameDB));
							RelevanceJTable table = new RelevanceJTable(model, frame, "Relevante Publikationen");
							
						}
						
					}catch(IllegalArgumentException ae) {
						JOptionPane.showMessageDialog(null,  "Es ist eine Autoren-Datenbank geladen. Eine Relevanz-Suche kann nur in einer Co-Zitations-Datenbank durchgeführt werden.", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
					}
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte laden Sie eine Co-Zitationsdatenbank.", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Setting" is pushed.
	 * It creates aSettingDialog-Object and adds all listeners.
	 */
	private class SettingDialogListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			SettingDialog sDia = new SettingDialog();
			sDia.setExitButtonListener(new ExitButtonListener(sDia));
			sDia.setOkButtonListener(new SettingOkButtonListener(sDia));
			sDia.setVisible(true);
		}
	}
	
	
	
	/*
	 * The method in this class is called up if in the Setting-Dialog the "Ok"-Button is pushed.
	 * It gets the selected settings and saves them in the properties file.
	 * 
	 */
	private class SettingOkButtonListener implements ActionListener{
		
		private SettingDialog dia;
		
		private SettingOkButtonListener(SettingDialog dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
	
			String sys = dia.getSystem();
			String con = dia.getConsolidation();
			String split = dia.getSplit();
			String multi = dia.getMulti();
			
			PropertiesCache prop = PropertiesCache.getInstance();
			
			if(sys != null) {
				
				if( sys.equals("w")) {
					
					prop.setProperty("system", "windows");
				}else {
					
					prop.setProperty("system", "linux/mac");
				}
			}
			
			
			if(con != null) {
				
				if( con.equals("j")) {
					
					prop.setProperty("consolidation", "on");
				}else {
					
					prop.setProperty("consolidation", "off");
				}
			}
			
			
			if(split != null) {
				
				if( split.equals("j")) {
					
					prop.setProperty("split", "on");
				}else {
					
					prop.setProperty("split", "off");
				}
			}
			
			
			if(multi != null) {
				
				if( multi.equals("j")) {
					
					prop.setProperty("multi", "on");
				}else {
					
					prop.setProperty("multi", "off");
				}
			}
			
			dia.dispose();
			
		}
	}
	
	
	
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Autor" is pushed.
	 * It creates a AuthorSearchDialog-Object and adds all listeners.
	 */
	private class AuthorSearchDialogListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			AuthorSearchDialog authDia = new AuthorSearchDialog();
			authDia.setExitButtonListener(new ExitButtonListener(authDia));
			authDia.setSearchButtonListener(new AuthorSearchListener(authDia));
			authDia.setVisible(true);
		}
	}
	
	
	/*
	 * The method in this class is called up if in the Author-Dialog the "Suchen"-Button is pushed.
	 * It gets all co-authors to a specified author as well as the corresponding titles and number of co-authored publications.
	 * Then, it displays the information to the user.
	 * 
	 */
	private class AuthorSearchListener implements ActionListener{
		
		private AuthorSearchDialog dia;
		
		private AuthorSearchListener(AuthorSearchDialog dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
	
			//getting the needed information from the dialog and the CurrentLoad object
			String auth = dia.getAuthorTf();
			String nameDB = load.getDB();
			int type = load.getType();
			
			try {
				
				//checking if a database is loaded
				if( nameDB.equals("-1") || type == -1) {
					
					dia.dispose();
					throw new IllegalArgumentException("Keine Datenbank geladen!");
				}else {
					
					try {
						
						//checking if a co-citation database is loaded
						if( type == 1) {
							
							dia.dispose();
							throw new IllegalArgumentException("Autoren Datenbank geladen!");
						}else {
							
							try {
								
								//checking if the user has the entry done
								if( auth.isEmpty()) {
									
									throw new IllegalArgumentException("Eingabefeld leer!");
								}else {
									
									//getting the co-authors as well as the further information
									LinkedList<CoAuthor> list = authSearch.authorSearch(auth, nameDB);
					   		    	
						   		    if( list.size() < 1) {
						   		    		
						   		    	JOptionPane.showMessageDialog(null,  "Der Autor konnte in der Datenbank nicht gefunden werden.", "Meldung", JOptionPane.INFORMATION_MESSAGE);
						   		    }else {
						   		    	dia.dispose();
						   		    	
						   		    	//displays the information to the user
						   		    	AuthorModel model = new AuthorModel(list);
						   		    	AuthorJTable table = new AuthorJTable(model, frame, auth+":");
						   		    }
								}
								
							}catch(IllegalArgumentException ae) {
								 JOptionPane.showMessageDialog(null,  "Eingabefeld ist leer!", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
							}	
						}
						
					}catch(IllegalArgumentException ae) {
						 JOptionPane.showMessageDialog(null,  "Es ist eine Co-Zitations-Datenbank geladen! Eine Autorensuche ist nur in einer Autorendatenbank möglich.", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
					}	
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte laden Sie die Datenbank, in der Sie nach einem Autor suchen möchten.", "Keine Datenbank geladen!",JOptionPane.ERROR_MESSAGE);	
			}	
		}
	}
	
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Ähnlichkeit" is pushed.
	 * It creates a SimilaritySearchDialod-Object and adds all listeners
	 */
	private class SimilaritySearchDialogListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			SimilaritySearchDialog dia = new SimilaritySearchDialog();
			dia.setStartButtonListener(new SimilaritySearchListener(dia));
			dia.setExitButtonListener(new ExitButtonListener(dia));
			dia.setFolderSelectButtonListener(new ChooseFileButtonListener(dia));
			dia.setVisible(true);
		}
	}
	
	
	
	
	
	/*
	 * The method in this class is called up when in the SimilaritySearch-Dialog the "Suchen"-Button is pushed.
	 * It searches in the selected co-citation database for similar publications to a specified publication.
	 * 
	 */
	private class SimilaritySearchListener implements ActionListener{
		
		private SimilaritySearchDialog dia;
		
		private SimilaritySearchListener(SimilaritySearchDialog dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			
			//gets all the needed data
			String title = dia.getTitleTf();
			File file = dia.getFile();
			String nameDB = load.getDB();
			int type = load.getType();
			
			try {
				//checks if a database is loaded
				if( nameDB.equals("-1") || type == -1) {
					
					dia.setFile(null);
					dia.dispose();
					throw new IllegalArgumentException("Keine Datenbank geladen!");
				}else {
					
					try {
						
						//checks if a author database is loaded
						if( type == 0) {
							
							dia.setFile(null);
							dia.dispose();
							throw new IllegalArgumentException("Autoren Datenbank geladen!");
						}else {
							
							try {
								
								//checks if the entries are correct. Only once is needed. A title or a file.
								if( !title.isEmpty() && file != null) {
								
									dia.setFile(null);
									throw new IllegalArgumentException("Titel und Datei angegeben!");
								}else {
									
									//checks if a file is specified
									if (file != null) {
										
										//extracts the title from the publication
										PDDocument document = PDDocument.load(file);
							   		    PDDocumentInformation inf = document.getDocumentInformation();
							   		  
							   		    title = inf.getTitle();
							   		    dia.setFile(null);
									}    
							   		try {
							   		    if( title == null) {
								   		    	
								   		    throw new IllegalArgumentException("Titel konnte nicht extrahiert werden!");
								   		}else {
								   		    	
								   			//gets all publications which are similar to the specified one
								   		    LinkedList<Publication> list = simiSearch.similaritySearch(nameDB, title);
								   		    	
								   		    if( list.size() < 1) {
								   		    		
								   		    	JOptionPane.showMessageDialog(null,  "Die Publikation konnte in der Datenbank nicht gefunden werden.", "Meldung", JOptionPane.INFORMATION_MESSAGE);
								   		    }else {
								   		    	
								   		    	//displays the results to the user
								   		    	SimilarityModel model = new SimilarityModel(list);
								   		    	SimilarityJTable table = new SimilarityJTable(model, frame, "Ähnliche Publikationen");
								   		    }
								   		    	
								   		}  	
							   		}catch(IllegalArgumentException ae) {
										JOptionPane.showMessageDialog(null,  "Der Titel konnte nicht extrahiert werden. Bitte geben Sie den Titel in das Feld Titel ein.", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
									}
								}
							}catch(IllegalArgumentException ae) {
								JOptionPane.showMessageDialog(null,  "Bitte geben sie entweder einen Titel ODER eine Publikation an!", "Fehlerhafte Eingaben!",JOptionPane.ERROR_MESSAGE);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null,  "Beim einlesen der Datei ist ein Fehler aufgetreten!", "Fehlermaldung!",JOptionPane.ERROR_MESSAGE);
								e1.printStackTrace();
							}
						}
					}catch(IllegalArgumentException ae) {
					 JOptionPane.showMessageDialog(null,  "Es ist eine Autoren-Datenbank geladen! Eine Ähnlichkeitssuche ist nur in einer Co-Zitationsdatenbank möglich..", "Fehlermeldung",JOptionPane.ERROR_MESSAGE);
					}
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte laden Sie die Datenbank, welche erweitert werden soll!", "Keine Datenbank geladen!",JOptionPane.ERROR_MESSAGE);	
			}	
		}
	}
	
	
	
	
	
	
	
	/*
	 * The method in this class is called up if the Button to select a folder is pushed.
	 * It opens the dialog to select the folder which contains the pdfs.
	 * 
	 */
	private class ChooseFileButtonListener implements ActionListener{
		
		private SimilaritySearchDialog dia;
		
		private ChooseFileButtonListener(SimilaritySearchDialog dia) {
			
			this.dia = dia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			JFileChooser chooser = new JFileChooser();
			
			FileFilter filter = new FileNameExtensionFilter("pdf","pdf");
			chooser.setFileFilter(filter);
			
			int value = chooser.showOpenDialog(null);
			
			if(value == JFileChooser.APPROVE_OPTION) {
				
				File file = chooser.getSelectedFile();
				dia.setFile(file);
			}
		
		}
	}

}
