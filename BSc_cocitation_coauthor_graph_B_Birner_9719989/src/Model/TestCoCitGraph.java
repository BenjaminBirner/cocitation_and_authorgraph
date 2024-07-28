package Model;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

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
public class TestCoCitGraph {
	
	
	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;
	
	
	
	
	
	
	
  
	
	
	/*
	  * This method extracts the references from the manually generated Documents.
	  */
	private References[] TestReferenceParser() throws Exception {
		
		 //getting the PDF files which contain the references
        File f1 = new File(System.getProperty("user.dir")+ File.separator + "pdf_collection_test" + File.separator);
	    File[] farray = f1.listFiles();
	    
	    References[] refArray = new References[farray.length];  //check if this method is also possible in the main class
	    
	    for(int i = 0; i < farray.length; i++) {
	    	
	    	//gets the text from the PDF as string
	    	PDDocument pdfDocument = PDDocument.load(farray[i]);
			PDFTextStripper textStripper = new PDFTextStripper();
			String s = textStripper.getText(pdfDocument);
			pdfDocument.close();
			
			
			References refObject = new References();
			//LinkedList<String> refList = new LinkedList<String>();
			
			int index1 = s.indexOf("$") + 1; 
			int index2 = s.indexOf("$", index1 + 1);
			
			while(index2 != -1) {
				
				
				String s2 = s.substring(index1, index2 );
				
				refObject.addReference(s2 , "xxx");
				
				if( s.length() > index2 +1) {
					
					index1 = index2 +1;
					index2 = s.indexOf("$", index1);
					
				}else {
	        	    index2 = -1;
				}    
			}
			
			refArray[i] = refObject;
				
		}
	         //normally we can delete this block
	    	for(int j = 0; j < refArray.length; j++) {
			
	    		Publication[] ref = refArray[j].getAllReferencesAsArray();
	    		
	    		String[] s = new String[ref.length];
	    		
	    		for(int k = 0; k < ref.length; k++) {
				
	    			s[k] = ref[k].getTitle();                                 
	    		}
	    		
	    		Arrays.sort(s);
	    		for(String st : s) {
	    			System.out.println(st);
	    		}
	    		
	    		System.out.println("");
	    		System.out.println("");
	    		
	    		
	    	}
	    	

	    return refArray;
	}
	
	
	
	
	
	
	//The same method as in the <CoCitationDBCreator> class. The aim here is to check if this method works properly.
	public void TestCreateDB() throws Exception { 
		 
		 
	
	
		//creates an new Database or loads an already existing one
	    String db_path = System.getProperty("user.dir") + File.separator + "test_citation_database";
		Path paths =  Paths.get(db_path);
		managementService =  new DatabaseManagementServiceBuilder(paths).build();
	    graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	    registerShutdownHook( managementService );
		 
	    //contains all the extracted title and authors of each PDF in a structured form  
		References[] refObjs = this.TestReferenceParser();                                      //the only exchanged method!!!
		
		//is required to mark the co-cited documents which are already considered
	    boolean[][] bool = new boolean[refObjs.length][]; 
	    
	    //initializing the boolean array
	    for(int m = 0; m < refObjs.length; m++) {
	    	
	    	bool[m] = new boolean[refObjs[m].size()];
	    }
	   
	    //The following loops are used to compare all titles to see if there are any cocitations
	    for( int i = 0; i < refObjs.length - 1; i++) {
	    
	    	Publication[] allRefs = refObjs[i].getAllReferencesAsArray();
	    	for( int j = 0; j < allRefs.length - 1; j++) {
	    		
	    		String titleM1 = allRefs[j].getManipulatedTitle();
	    		for( int k = j + 1; k < allRefs.length; k++) {
	    			
	    			String titleM2 = allRefs[k].getManipulatedTitle();
	    			
	    			//checking if the combination of title1 and title2 were already considered in previous iterations
	    			if(bool[i][j] == false || bool[i][k] == false) {	
	    				
	    				//checks if the combination of title1 and titel2 are present in other publications. If so means that they are co-cited.
	    				for(int l = i + 1; l < refObjs.length; l++) {
	    				
	    					Publication[] allRefs2 = refObjs[l].getAllReferencesAsArray();
	    					Boolean flag1 = false;
	    					Boolean flag2 = false;
	    					int inx1 = 0;
	    					int inx2 = 0;
	    					for(int n = 0; n < allRefs2.length; n++) {
	    					
	    						if( allRefs2[n].getManipulatedTitle().equalsIgnoreCase(titleM1)){
	    						
	    							flag1 = true;
	    							inx1 = n;
	    						}
	    					
	    						if( allRefs2[n].getManipulatedTitle().equalsIgnoreCase(titleM2)){
	    						
	    							flag2 = true;
	    							inx2 = n;
	    						}
	    					}
	    					//true means that title1 and title2 occur together in this publication. So, they are co-cited.
	    					if(flag1 == true && flag2 == true) {
	    					
	    						bool[l][inx1] = true;
	    						bool[l][inx2] = true;
	    						
	    						//creating the corresponding nodes and relationship concerning the co-cited documents or increases the number of
	    						//the relationship if the nodes and relationship already exists.
	    						try (Transaction tx = graphDb.beginTx()) {
	    						
	    							ResourceIterator<Node> allNodes = tx.findNodes(Labels.PUBLICATION);
	    							Boolean flag3 = false;
	    							Boolean flag4 = false;
	    							Node n1 = null;
	    							Node n2 = null;
	    							
	    							//checks if the nodes already exists
	    							while (allNodes.hasNext()) {
	    								                                     
	    								Node node = allNodes.next();
	    								String t = (String) node.getProperty("title");
	    								t = manipulateTitle(t);
	    							
	    								if (t.equalsIgnoreCase(titleM1)) {
	    									
	    									flag3 = true;
	    									n1 = node;
	    								}
	    							
	    								if (t.equalsIgnoreCase(titleM2)) {
	    									
	    									flag4 = true;
	    									n2 = node;
	    								}
	    							
	    								if( flag3 == true && flag4 == true) {
	    									
	    									break;
	    								}
	    							}
	    							//creating a new node if it does not exist yet
	    							if(flag3 == false) {
	    								
	    								n1 = tx.createNode(Labels.PUBLICATION);
	    	  							n1.setProperty("title", allRefs[j].getTitle());
	    	  							n1.setProperty("paper", l);
	    	  							if (allRefs2[inx1].getAuthors() != null) {
	    	  								
	    	  								n1.setProperty("authors", allRefs2[inx1].getAuthors());   
	    	  							}
	    	  							
	    								
	    							}
	    							//creating a new node if it does not exist yet
	    							if(flag4 == false) {
	    								
	    								n2 = tx.createNode(Labels.PUBLICATION);
	    	  							n2.setProperty("title", allRefs[k].getTitle());
	    	  							n2.setProperty("paper", l);
	    	  							if (allRefs2[inx2].getAuthors() != null) {
	    	  								
	    	  								n2.setProperty("authors", allRefs2[inx2].getAuthors());   
	    	  							}
	    							}
	    							boolean found = false;                                        
	    							Iterator<Relationship> Relationships = n1.getRelationships().iterator();
	    							
	    							//checking if the relationship between n1 and n2 already exists
	    							while( Relationships.hasNext()) {                                
	      								
	      								Relationship rel = Relationships.next();
	      								
	      								//if the relationship already exists, it increases the number of co-citations of the relationship
	      								if(n2.equals(rel.getOtherNode(n1))) {
	      									
	      									int numb = (int) rel.getProperty("cocitations");
	      									rel.setProperty("cocitations", numb + 1);
	      									found = true;
	      									break;
	      								}
	      							}
	      							
	      							//creates a new relationship between n1 and n2 if there is no relationship yet
	      							if( found == false) {
	      								
	      								Relationship newRel = n1.createRelationshipTo(n2, RelationshipTypes.CO_CITED);
	      								newRel.setProperty("cocitations", 2);
	      							}
	    							
	      							tx.commit();
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    }

		managementService.shutdown();
	 }
	 
	 
	 
	 
	 
	 private static void registerShutdownHook( final DatabaseManagementService managementService ){
		    
	       
	        Runtime.getRuntime().addShutdownHook( new Thread(){
	        
	            @Override
	            public void run(){
	            
	                managementService.shutdown();
	            }
	        } );
	}


	 private String manipulateTitle(String title) {
		  
		 title = title.replaceAll("[^A-Za-z0-9]", "");
			
		return title;
	 }

}
