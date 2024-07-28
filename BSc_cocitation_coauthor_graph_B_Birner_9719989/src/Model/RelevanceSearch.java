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
 * This class is responsible for the relevance search. It generates the most relevant publications regarding
 * a specified co-citations database.
 * 
 * @author Benjamin Birner
 *
 */
public class RelevanceSearch {
	
	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	
	
	/**
	 * This method creates for each node of the specified database a corresponding <code>Publication<code> object.
	 * Then, it sorts these <code>Publication<code> objects according to there relevance.
	 * 
	 * @param nameDB the name of the database in which the relevance search shall be carried out
	 * @return the sorted list which includes for each node in the database the corresponding <code>Publication<code> object
	 */
	public LinkedList<Publication> relevanceSearch(String nameDB)	{
		
		 //makes the database with the name <name DB> available
		 String db_path = System.getProperty("user.dir") + File.separator + "co_citation_db" + File.separator + nameDB;
		 Path paths =  Paths.get(db_path);
		 managementService =  new DatabaseManagementServiceBuilder(paths).build();
	     graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	     registerShutdownHook( managementService );
		
	     LinkedList<Publication> refList = new LinkedList<Publication>();
	     
	     try (Transaction tx = graphDb.beginTx()) {
	    	 
	    	 //gets all nods from the database
	    	 ResourceIterator<Node> allNodes = tx.findNodes(Labels.PUBLICATION);
	    	 Node node;
	    
	    	//creating for each node a <Publication>-Object with the corresponding properties
	    	 while (allNodes.hasNext()) {
	    	 
	    		 node = allNodes.next();
	    		 String title = (String) node.getProperty("title");
	    		 String auth = (String) node.getProperty("authors");
		    	 
		    	 int degree = node.getDegree();
	    		 
	    		 Iterator<Relationship> rel = node.getRelationships().iterator();
	    		 int cocit = 0;
	    		 
	    		 while( rel.hasNext()) {
	    			 
	    			 Relationship relship = rel.next();
	    			 cocit = cocit + (int) relship.getProperty("cocitations");
	    		 }
	    		 
	    		 refList.add(new Publication(title, auth, Integer.toString(cocit), Integer.toString(degree)));
	    	 }
	    	 tx.commit();	 
	     }
	     managementService.shutdown();
	     Collections.sort(refList, new SortRelevance());
	     
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
