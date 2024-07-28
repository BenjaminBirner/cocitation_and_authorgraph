package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.data.Person;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;


/**
 * This class contains the method which extracts the header data from the publications and creates or extends a neo4j database.
 * 
 * @author Benjamin Birner
 *
 */
public class AuthorDBCreator {


	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	
    
	 /**
	  * This method is only used locally by the <code>createDB<code> method in this class. It extracts the header data from each
	  * publication/PDF file.
	  * 
	  * @param pathPDFs the path to the folder that includes the publications
	  * @return a list which contains all the extracted header data.
	  * @throws IOException
	  */
	 private LinkedList<Header> getAllHeader(String pathPDFs) throws IOException{
		 
		 //gets the PDF files
		 File f = new File(pathPDFs);
		 File[] farray = f.listFiles();
		 
		 //gets the information which system is used
		 PropertiesCache prop = PropertiesCache.getInstance();
		 String sys = prop.getProperty("system");
		 
		 
		 LinkedList<Header> hList = new LinkedList<Header>();
		 
		 //if windows is used only PDFBox extracts the data
		 if ( sys == null || sys.equals("windows")) {
			 
			 for (File element : farray) {
				 
				 Header h = this.getHeaderPDFBox(element);
				 hList.add(h); 
			 }
		 //if Linux or Mac is used - extracts all the header data  
		 }else {
			 
			//getting a GROBID Engine instance in order to extracts the Header data
		     String pGrobidHome = System.getProperty("user.dir") + File.separator + "grobid";             
		     GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
		     GrobidProperties.getInstance(grobidHomeFinder);
		     Engine e = GrobidFactory.getInstance().createEngine();

		     
		     //loads the consolidation state
			 String con = prop.getProperty("consolidation");
			 
			 //checks if consolidation is on
			 int c = 0;
			 if (con != null) {
				 if(con.equals("on")) {
					 c = 1;
				 }
			 }
		     
		     
		     //extracts the header of all pdfs
		     for (File element : farray) {
				 
		    	 //getting the header using PDFBox
				 Header h = this.getHeaderPDFBox(element);
				 int size = h.getAuthors().size();
				 int flag = 0;
				 Header h1 = null;
				 
				 //if PDFBox could not extract any authors or just one GROBID is used
				 if(size < 2 ) {
					 
					 String path = element.getAbsolutePath();
					 BiblioItem item = new BiblioItem();
					 e.processHeader(path, c, item);
					 List<Person> authList = item.getFullAuthors();
					 LinkedList<String> nameList = new LinkedList<String>();
					 
					 if( authList != null) {
						 
						 for ( Person p : authList) {
							 
							 if( p != null) {
								 
								 String auth = p.getFirstName() + " " + p.getLastName();
								 nameList.add(auth);
							 }
						 }
					 }
					 
					 h1 = new Header(item.getTitle(), nameList);
					 flag = 1;
				 }
				 if( flag == 0) {
					 
					 hList.add(h);
				 }else {
					 hList.add(h1);
				 }
		     }
		 }	 
		 return hList;
	}
	 
	 
	 
	 
	 
	 
	 /**
	  * This method is only used locally by the <code>getAllHeader<code> method in this class. It extracts the header data from the
	  * publication/PDF file.
	  * 
	  * @param f the pdf file
	  * @return a <code>Header<code> that contains the extracted data
	  * @throws IOException
	  */
	 private Header getHeaderPDFBox(File f) throws IOException {
		 
            
			//extracts the header data. Here the PDFBox library is used
		    PDDocument document = PDDocument.load(f);
		    PDDocumentInformation inf = document.getDocumentInformation();
		  
		    //gets the authors of the publication
		    String authors = inf.getAuthor();
		    LinkedList<String> list = new LinkedList<String>();
		    
		
		    if( authors != null) {
		    	
		    	//separates the different authors because they are extracted as one string
			    int index1 = 0; 
			    int index2 = authors.indexOf(";");                     
			    String separator = null;
			    if (index2 == -1) {
			    	
			    	index2 = authors.indexOf(",");
			    	separator = ",";
			    }else {
			    	separator = ";";
			    }
			  
			    while(index2 != -1) {
		
                   list.add(authors.substring(index1, index2 ));
               
                   if( authors.length() > index2 +1) {
                 	
             	      if( !String.valueOf(authors.charAt(index2 +1)).equals(" ")) {
                        
             		      index1 = index2 +1;
             	      }else {
             	  
             		      index1 = index2 +2;
             	      }
                      index2 = authors.indexOf(separator, index1);  
                   }else {
             	      index2 = -1;
                   }
			    }
			    list.add(authors.substring(index1, authors.length()));
			  
			    //creates for each publication a Header object
			    Header h = new Header(inf.getTitle(), list);
			    document.close();
			    
			    return h;
		    }
		    document.close();
		    //if no author could be extracted a empty Header-object is created
		    Header h = new Header("", list);
		    return h;
	 }
	 
	 
	 
	 
	 /**
	  * This method extracts the header data from each publication/PDF file and creates or extends an author database with this data.
	  * 
	  * @param pathPDFs the path to the folder that includes the publications/PDF files
	  * @param nameDB the name of the database which shall be created or extended
	  * @param type indicates if a new database shall be created or an existing one shall be extended 
	  * @throws IOException 
	  * @throws Exception
	  */
	public void createDB(String pathPDFs, String nameDB, String type) throws IOException{
		
		
			//makes the database available
	    	String db_path = System.getProperty("user.dir") + File.separator + "author_db" + File.separator + nameDB;
  		    Path paths =  Paths.get(db_path);
  		    managementService =  new DatabaseManagementServiceBuilder(paths).build();
	    	graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	    	registerShutdownHook( managementService );
	    	
		    
		    //extracts the header data
  		    LinkedList<Header> headerList;
		
  		    headerList = this.getAllHeader(pathPDFs);
  	
  		    try (Transaction tx = graphDb.beginTx()) {
  		    	
  		    	for(Header header : headerList) {
  		    		
  		    		//gets all authors from the header object
  		    		LinkedList<String> authList1 = header.getAuthors();
  		    		
  		    		LinkedList<String> authList2 = new LinkedList<String>();
  		    		LinkedList<Node> authNodes = new LinkedList<Node>();
  		    		Iterator<String> it = authList1.iterator();
  		    		
  		    		while(it.hasNext()) {
  		    			authList2.add(it.next());
  		    		}
  		    		
  		    	    //gets all nodes from the database
  					ResourceIterator<Node> allNodes = tx.findNodes(Labels.AUTHOR);
  					
  				    // checking if the node is already present and creates a new one if not
  					if(authList1.size() > 1) {
  						
  						while (allNodes.hasNext()) {
  	  						
  							//true means that all authors are found in the database
  							if(authList2.size() == 0) {
  								
  								break;
  							}
  								
  							Node node = allNodes.next();
  	  	  						
  	  	  					for(String author : authList1) {
  	  	  						
  	  	  						String nameDb = (String) node.getProperty("Name");
  	  	  						String firstDb = getFirstName(nameDb);
  	  	  					    String lastDb = getLastName(nameDb);
  	  	  					    String first = getFirstName(author);
  	  	  					    String last = getLastName(author);
  	  	  					    
  	  	  		
  	  	  						//true means that the author is already existing in the database
  	  	  						if ( (firstDb.equalsIgnoreCase(first) || firstDb.equalsIgnoreCase(last)) && 
  	  	  								(lastDb.equalsIgnoreCase(first) || lastDb.equalsIgnoreCase(last)) &&
  	  	  								 !firstDb.equalsIgnoreCase(lastDb) && !first.equalsIgnoreCase(last)) {                                                 
  	  	  					
  	  	  							//will be contain all nodes that are found 
  	  	  							authNodes.add(node);
  	  	  							//the found nodes/authors are removes from authList2
  	  	  							authList2.remove(author);
  	  	  							//adds the new title which belongs to this author
  	  	  							node.setProperty("Title", node.getProperty("Title") + header.getTitle() + " $ ");   
  	  	  	  					}else {
  	  	  	  						if( firstDb.equalsIgnoreCase(lastDb) && first.equalsIgnoreCase(last) && lastDb.equalsIgnoreCase(last)) {
  	  	  	  							authNodes.add(node);
  	  	  	  							authList2.remove(author);
  	  	  	  							node.setProperty("Title", node.getProperty("Title") + header.getTitle() + " $ ");
  	  	  	  						}
  	  	  	  					}
  	  	  					}
  	  	  				}
  						//now authList2 only contains the authors which were not found in the database
  						//So, it creates a new node for each of that authors
  						for(String auth : authList2) {
  						
  							Node newNode = tx.createNode(Labels.AUTHOR);
  							newNode.setProperty("Name", auth);
  							newNode.setProperty("Title", header.getTitle() + " $ ");
  							authNodes.add(newNode);
  						}
  					}
  					
  					//adds a new relationship or increases the weight of a already existing relationship
  					for (int i = 0; i < authNodes.size() - 1; i++) {
  						
  						boolean found = false;
  						int numb;
  						Node n1 = authNodes.get(i);
  						
  						for (int j = i + 1; j < authNodes.size(); j++) {
  							
  							found = false;
  							Iterator<Relationship> Relationships = n1.getRelationships().iterator();
  							Node n2 = authNodes.get(j);
  							
  							//checks if a relationship between n1 and n2 already exists
  							while( Relationships.hasNext()) {
  								
  								Relationship rel = Relationships.next();
  								
  								if(n2.equals(rel.getOtherNode(n1))) {
  									
  									numb = (int) rel.getProperty("number");
  									rel.setProperty("number", numb + 1);
  									found = true;
  									break;
  								}
  							}
  							
  							//creates a new relationship between n1 and n2 if there is no relationship yet
  							if( found == false) {
  								
  								Relationship newRel = n1.createRelationshipTo(n2, RelationshipTypes.PUBLISHED_TOGETHER);
  								newRel.setProperty("number", 1);
  							}
  						}
  					}
  		    	}
  		    	tx.commit();
  	        }
  		    managementService.shutdown();
  		    
  		    //stores the name in the properies if it does not exist yet
			if( type.equals("new")) {
			   storeDBName(nameDB);
			}
		}
	
	
	
	
	
	
	
	
	
	/**
	 * This method stores the name of the database in the properties file
	 * 
	 * @param name name of the database
	 */
	private void storeDBName(String name) {
		 
		 PropertiesCache prop = PropertiesCache.getInstance();
		 String names = prop.getProperty("author_dbs");
		 
		 if( names == null) {
			 
			 names = name + "$";
		 }else {
			 
			 names = names + name + "$";
		 }
		  prop.setProperty("author_dbs", names);
	 }
	
	
	
	
	 private static void registerShutdownHook( final DatabaseManagementService managementService ){
		    
	       
	        Runtime.getRuntime().addShutdownHook( new Thread(){
	        
	            @Override
	            public void run(){
	            
	                managementService.shutdown();
	            }
	        } );
	    }


	 private String getFirstName(String auth) {
		 
		 String first = auth;
		 if( first != null) {
			 int i = auth.indexOf(" ");
			 if(i != -1) {
				 first = auth.substring(0, i); 
			 }
		 }
		 return first;
	 }
	 
	 
	 private String getLastName(String auth) {
		 
		 String last = auth;
		 if( last != null) {
			 int i = auth.lastIndexOf(" ");
			 if(i != -1) {
				 last = auth.substring(i+1);
			 }
		 }
		 return last;
		 
	 }
	                                                                                                                                                                                                          
		
}


