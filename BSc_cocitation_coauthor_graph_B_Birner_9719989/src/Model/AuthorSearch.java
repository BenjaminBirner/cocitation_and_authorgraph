package Model;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;



/**
 * This class carries through the author search. The responsible method searches in a author database for a specified author and gets 
 * all his co-authors as well as the corresponding titles and the number of publications.
 * 
 * @author Benjamin Birner
 *
 */
public class AuthorSearch {

	
	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	
	
	/**
	 * This Method carries through the author search. It searches in a author database for a specified author and gets 
     * all his co-authors as well as the corresponding titles and the number of publications.
	 * 
	 * @param author the author that shell be searched in the database
	 * @param nameDB name of the database in which the search shall be carried through
	 * @return list that contains all <code>CoAuthor</code> instances which contain the required data. 
	 */
	public LinkedList<CoAuthor> authorSearch(String author, String nameDB)	{
		
		 //loads the database
		 String db_path = System.getProperty("user.dir") + File.separator + "author_db" + File.separator + nameDB;
		 Path paths =  Paths.get(db_path);
		 managementService =  new DatabaseManagementServiceBuilder(paths).build();
	     graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	     registerShutdownHook( managementService );
		
	     LinkedList<CoAuthor> authList = new LinkedList<CoAuthor>();
	     
	     try (Transaction tx = graphDb.beginTx()) {
	    	 
	    	 //gets all nodes from the database
	    	 ResourceIterator<Node> allNodes = tx.findNodes(Labels.AUTHOR);
	    	 Node node;
	    
	    	 //searches the <code>author</code> in the database. It checks for each node if the property <code>Title</code> is equal.
	    	 while (allNodes.hasNext()) {
	    		 
	    		 node = allNodes.next();
	    		 String auth = (String) node.getProperty("Name");
	    		 
	    		 //if the author/node is found... then
	    		 if( auth.equalsIgnoreCase(author)) {
	    			 
	    			 //gets all relationships regarding the node with the property Name = <code>author</code>
	    			 Iterator<Relationship> relships = node.getRelationships().iterator();
	    			 
	    			 //gets each co-author and the number of publications they have together
	    			 while(relships.hasNext()) {
	    				 
	    				 Relationship rel = relships.next();
	    				 Node otherNode = rel.getOtherNode(node);
	    				 String titlesA = (String) node.getProperty("Title");
	    				 String titlesC = (String) otherNode.getProperty("Title");
	    				 LinkedList<String> titListA = getAllTitlesAsList(titlesA);
	    				 LinkedList<String> titListC = getAllTitlesAsList(titlesC);
	    				 LinkedList<String> eqList = new LinkedList<String>();
	    				 
	    				 for(String s1 : titListA) {
	    					 for(String s2 : titListC) {
	    						
	    						 if( s1.equalsIgnoreCase(s2)) {
	    							 
	    							 eqList.add(s2);
	    						 } 
	    					 }
	    				 }
	    				 //creates the corresponding <code>CoAuthor</code> instance
	    				 int numb = (int) rel.getProperty("number");       
	    				 String coAuth = (String) otherNode.getProperty("Name");
	    				 authList.add(new CoAuthor(coAuth, eqList, numb )); 
	    			 }
	    		 } 
	    	 }
	    	 tx.commit();
	     }
	     
	     managementService.shutdown();
	     
	     Collections.sort(authList, new SortCoAuthor());
	     
	     return authList;
	}
	
	
	/**
	 * This method separates the different titles which are saved in a node because there are several titles
	 * saved as one string.
	 * 
	 * @param titles the string that includes all titles
	 * @return the separated titles in a list
	 */
	private LinkedList<String> getAllTitlesAsList(String titles){
		
		 LinkedList<String> titleList = new LinkedList<String>();
		
		 int index1 = 0; 
		 int index2 = titles.indexOf("$",1);
			
		 while(index2 != -1) {
				
			String s = titles.substring(index1, index2 );
				
			titleList.add(s);
				
			if( titles.length() > index2 + 2) {
					
				index1 = index2 + 2;
				index2 = titles.indexOf("$", index1);
					
			}else {
	       	    index2 = -1;
			}    
	   	 }
		 
		 return titleList;
	}
	
	
	
	
	
	
	
	private static void registerShutdownHook( final DatabaseManagementService managementService ){
	    
	       
        Runtime.getRuntime().addShutdownHook( new Thread(){
        
            @Override
            public void run(){
            
                managementService.shutdown();
            }
        } );
    }
	
	
	
}
