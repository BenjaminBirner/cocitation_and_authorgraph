package Model;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * This class is responsible for the similarity search. It searches regarding a specified publication in a specified co-citation database
 * for similar publications.
 * 
 * @author Benjamin Birner
 *
 */
public class SimilaritySearch {
	
	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	

	/**
	 * This method searches regarding the publication with the titlt <code>title<code> in the co-citation database <code>nameDB<code> 
	 * for similar publications.
	 * 
	 * @param nameDB specifies the database in which the search shall be carried out.
	 * @param title the title regarding the publication of which similar publications shall be found.
	 * @return list that includes similar publications
	 */
	public LinkedList<Publication> similaritySearch(String nameDB, String title)	{
	
		//makes the database available
		 String db_path = System.getProperty("user.dir") + File.separator + "co_citation_db" + File.separator + nameDB;
		 Path paths =  Paths.get(db_path);
		 managementService =  new DatabaseManagementServiceBuilder(paths).build();
	     graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	     registerShutdownHook( managementService );
		
	     LinkedList<Publication> refList = new LinkedList<Publication>();
	     
	     try (Transaction tx = graphDb.beginTx()) {
	    	 
	    	 //gets all nodes from the database
	    	 ResourceIterator<Node> allNodes = tx.findNodes(Labels.PUBLICATION);
	    	 Node node = null;
	    	 String titleDB = null;
	    	 boolean flag = false;
	    	 
	    	 //searching in the database for the node that is labeled with the title of which we looking for similar publications  
	    	 while (allNodes.hasNext()) {
	    		 
	    		 node = allNodes.next();
	    		 titleDB = (String) node.getProperty("title");
	    		 
	    		 if( titleDB.equalsIgnoreCase(title)) {
	    			 
	    			 flag = true;
	    			 break;
	    		 } 
	    	 }
	    	 
	    	 //true means that the node with the title <title> was found
	    	 if( flag == true) {
	    		 
	    		 //gets all relationships for this node
	    		 Iterator<Relationship> relationships = node.getRelationships().iterator();
	    		 
	    		 Node otherNode;
	    		 String otherTitle;
	    		 String otherAuth;
	    		 int number;
	    		 
	    		 //creates a new <Publication> object and adds it to the <reList>
	    		 while( relationships.hasNext()) {   
	    			 
	    			 Relationship rel = relationships.next();
	    			 otherNode = rel.getOtherNode(node);
	    			 otherTitle = (String) otherNode.getProperty("title");
	    			 otherAuth = (String) otherNode.getProperty("authors");
	    			 number = (int) rel.getProperty("cocitations");
	    			 refList.add(new Publication(otherTitle, otherAuth, Integer.toString(number)));
	    		 }
	    	 }
	    	 tx.commit();
	     }
	     managementService.shutdown();
	     
	     return refList;
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
