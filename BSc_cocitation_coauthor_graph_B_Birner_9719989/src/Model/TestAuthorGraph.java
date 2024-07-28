package Model;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;



/*
 * This class is only used for testing purpose
 */
public class TestAuthorGraph {
	
	
	
	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	
    
	
	
	
	
	 
	 /*
	  * This method extracts the authors from the manually generated Documents.
	  */
	 private LinkedList<Header> TestGetAllHeader() throws IOException{
		 
		 //getting the PDF files which contain the authors
		 File f = new File(System.getProperty("user.dir")+ File.separator + "pdf_collection_test" + File.separator);
		 File[] farray = f.listFiles();
		 
		 LinkedList<Header> hList = new LinkedList<Header>();
		 
		 for(int i = 0; i < farray.length; i++) {
			 
			 PDDocument doc = PDDocument.load(farray[i]);
			 PDFTextStripper textStripper = new PDFTextStripper();
			 String s = textStripper.getText(doc);
			 doc.close();
			 
			 LinkedList<String> authList = new LinkedList<String>();
				
				
			 int index1 = 1; 
			 int index2 = s.indexOf("#", 2);
				
			 while(index2 != -1) {
					
				 authList.add(s.substring(index1, index2 ));
					
				 if( s.length() > index2 +1) {
						
				 index1 = index2 +1;
				 index2 = s.indexOf("#", index1);
						
				 }else {
		        	 index2 = -1;
				}    	
			 }
			 
			 Header h = new Header("xxx", authList);
			 hList.add(h);
		 }
		 
		 return hList;
	}
	 
	 
	 
	 
	 
	 
	 
	 private static void registerShutdownHook( final DatabaseManagementService managementService ){
		    
	       
	        Runtime.getRuntime().addShutdownHook( new Thread(){
	        
	            @Override
	            public void run(){
	            
	                managementService.shutdown();
	            }
	        } );
	    }
	 
	 
	 
	 
	 
	//The same method as in the <AuthorDBCreator> class. The aim here is to check if this method works properly
	public void testCreateDB() throws Exception {

		    System.out.println("strats creating a DB"); 
		
	    	String db_path = System.getProperty("user.dir") + File.separator + "Test_author_database";
  		    Path paths =  Paths.get(db_path);
  		    managementService =  new DatabaseManagementServiceBuilder(paths).build();
	    	graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	    	registerShutdownHook( managementService );

  		    
  		    LinkedList<Header> headerList = this.TestGetAllHeader();                   //the only exchange here!!!

  		    try (Transaction tx = graphDb.beginTx()) {
  		    	
  		    	for(Header header : headerList) {
  		    		
  		    		
  		    		LinkedList<String> authList1 = header.getAuthors();
  		    		LinkedList<String> authList2 = new LinkedList<String>();
  		    		LinkedList<Node> authNodes = new LinkedList<Node>();
  		    		
  		    		for(String str : authList1) {
  						
  						System.out.println(str);
  					}
  					
  					
  		    		
  		    		
  		    		Iterator<String> it = authList1.iterator();
  		    		while(it.hasNext()) {
  		    			authList2.add(it.next());
  		    		}
  		    		
  		    	    // checking if the node is already present and creates a new one if not
  					ResourceIterator<Node> allNodes = tx.findNodes(Labels.AUTHOR);

  					if(authList1.size() > 1) {
  						
  						while (allNodes.hasNext()) {
  	  						
  							if(authList2.size() == 0) {
  								
  								break;
  							}
  								
  							Node node = allNodes.next();
  	  	  						
  	  	  					for(String author : authList1) {
  	  	  							
  	  	  						if (node.getProperty("Name").equals(author)) {
  	  	  							
  	  	  							authNodes.add(node);
  	  	  							authList2.remove(author);
  	  	  							node.setProperty("Title", node.getProperty("Title") + " ### " + header.getTitle());
  	  	  	  					}			
  	  	  					}
  	  	  				}
  						
  						for(String auth : authList2) {
  						
  							Node newNode = tx.createNode(Labels.AUTHOR);
  							newNode.setProperty("Name", auth);
  							newNode.setProperty("Title", header.getTitle());
  							authNodes.add(newNode);
  						}
  					}
  					System.out.println(authNodes.size());
  					System.out.println("");
  					
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
  		
  			//} catch (Exception ex) {
  	        	
  	        	//System.out.println("Exception occurred: " + ex.toString());
  	        }
  		    
  		    System.out.println("finished creating the DB"); 
  		    managementService.shutdown();
		}
	
	

}
