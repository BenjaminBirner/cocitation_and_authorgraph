package Model;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.grobid.core.data.BiblioItem;
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

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.bibref.KMeansBibReferenceExtractor;
import pl.edu.icm.cermine.structure.model.BxDocument;





/**
 * This class is responsible for extracting the reference-strings from the PDFs and extracting the metadata from the reference-strings.
 * Then, it creates or extends a co-citation database with the extracted data.
 * For doing so, multithreading is applied.
 * 
 * @author Benjamin Birner
 *
 */
public class CoCitationDBCreator {


	//each list belongs to one thread. It contains the extracted reference-strings
	//a string array contains all references of one publication
	private  LinkedList<String[]> l1 = new LinkedList<String[]>();
	private  LinkedList<String[]> l2 = new LinkedList<String[]>();
	private  LinkedList<String[]> l3 = new LinkedList<String[]>();
	private  LinkedList<String[]> l4 = new LinkedList<String[]>();
	private  LinkedList<String[]> l5 = new LinkedList<String[]>();
	private  LinkedList<String[]> l6 = new LinkedList<String[]>();
	private  LinkedList<String[]> l7 = new LinkedList<String[]>();
	private  LinkedList<String[]> l8 = new LinkedList<String[]>();
	private  LinkedList<String[]> l9 = new LinkedList<String[]>();
	private  LinkedList<String[]> l10 = new LinkedList<String[]>();
	private  LinkedList<String[]> l11 = new LinkedList<String[]>();
	private  LinkedList<String[]> l12 = new LinkedList<String[]>();
	private  LinkedList<String[]> l13 = new LinkedList<String[]>();
	private  LinkedList<String[]> l14 = new LinkedList<String[]>();
	private  LinkedList<String[]> l15 = new LinkedList<String[]>();
	private  LinkedList<String[]> l16 = new LinkedList<String[]>();
	

	DatabaseManagementService  managementService;
	GraphDatabaseService graphDb;


	/**
	 * This method splits each PDF file. It extracts the pages that contains the references and saves these pages as a new PDF document.
	 * 
	 * @throws IOException
	 */
	private void splitPDF(String pathPDFs) throws IOException {                 
		
		 //getting all PDF files
		 File f2 = new File(pathPDFs);
	     File[] farray2 = f2.listFiles();

	     //splitting each file so that only the references are considered
	     for(int l=0; l < farray2.length; l++) {

	    	 //PDFBox is used here
	    	 PDDocument document = PDDocument.load(farray2[l]);
			 Splitter splitter = new Splitter();
			 int numb = document.getNumberOfPages();
			 
			 //setting the parameters for the split depending on the absolute page-number
			 int r = 2;
			 int t = 3;
			 if (numb == 2) {
				 r = 1;
				 t = 2;
			 }

			 if (numb == 1) {
				 r = 0;
				 t = 1;
			 }

			 if (numb > 10) {
				 r = 4;
				 t = 5;
			 }
			 
			 if (numb > 25) {
				 r = 5;
				 t = 6;
			 }
			 
			 if (numb > 50) {
				 r = 7;
				 t = 8;
			 }
			 
			 //splitting the Document
			 splitter.setEndPage(numb);
			 splitter.setStartPage(numb-r);
			 splitter.setSplitAtPage(t);
			 
			 //Due to the chosen parameters, the list only contains one element
			 List<PDDocument> list = splitter.split(document);

			 Iterator<PDDocument> iterator = list.listIterator();
			 int k = 0;
			 
			 //saves the splitted document
	         while(iterator.hasNext()) {
	            PDDocument pd = iterator.next();
	            pd.save(System.getProperty("user.dir")+ File.separator + "pdf_collection_splitted" + File.separator + "part_"+ k + "__ " + l + ".pdf");
	            k++;
	            pd.close();
	         }
	         document.close();
	     }

	}
	
	
	
	
	/**
	  * This method loads all PDF files and extracts the metadata from each reference of a publication.
	  * For each publication it creates one <code>References<code> object. A <code>References<code> object includes 
	  * all references of a publication in shape of the corresponding number of <code>Reference<code> objects. 
	  * 
	  * 
	  * @param pathPDFs the path to the folder that contains the PDF files
	  * @return A list that contains all <code>References<code> objects
	  * @throws Exception
	  */
	private References[] referenceParser1(String pathPDFs) throws Exception {
		
		 //loads the split state
		 PropertiesCache prop = PropertiesCache.getInstance();
		 String split = prop.getProperty("split");
		 
		 String path = pathPDFs;
		 
		 if(split == null || split.equals("on")) {
			 
			//extracts the pages which contain the references of the PDF
			 this.splitPDF(pathPDFs);
			 path = System.getProperty("user.dir")+ File.separator + "pdf_collection_splitted" + File.separator;
		 }
		 
		 

		 //getting the PDF files which contain the references
       File f1 = new File(path); 
	    File[] farray1 = f1.listFiles();
		
	    LinkedList<String[]> refs = new LinkedList<String[]>();
	    
	    if(farray1 != null) {

	    	  for (File file : farray1) {
	    		  
	    		  if(file != null) {
	    			 
	    			  //gets a new ContentExtractor instance
	    			  ContentExtractor ce = new ContentExtractor();  //this is the method which takes long runtime

		    		  String pathF = file.getAbsolutePath();

		    		  InputStream pdfIS = new FileInputStream(pathF);
		    		  ce.setPDF(pdfIS);
		    	      BxDocument bx = ce.getBxDocument();

		    	      //extracts the reference string
		    	      KMeansBibReferenceExtractor ext = new KMeansBibReferenceExtractor();

		    	      //contains all the reference strings of a publication
		    	      String[] sArray = ext.extractBibReferences(bx);
		    	      refs.add(sArray);
	    		  }
	    	  }
		}
	    
	     //getting a GROBID Engine instance in order to parse the reference strings
	     String pGrobidHome = System.getProperty("user.dir") + File.separator + "grobid";     
	     GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
	     GrobidProperties.getInstance(grobidHomeFinder);
	     Engine e = GrobidFactory.getInstance().createEngine();

	     //each PDF document corresponds to a References object. In this list all the References objects will be saved
	     LinkedList<References> refList = new LinkedList<References>();

	     
	     //loads the consolidation state
		 String con = prop.getProperty("consolidation");
		 
		 //checks if consolidation is on
		 int c = 0;
		 if (con != null) {
			 if(con.equals("on")) {
				 c = 1;
			 }
		 }
	     
	     
	  
	     //extracts the metadata from each reference string
	     for( String[] refa : refs) {
	    	 //creates a new References object in which the title and the authors of each reference string are saved
	    	 References ref = new References();
	    	 for (String element : refa) {
	    		 
	    		 //extracts the metadata from the reference string
	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 //adding the extracted title and authors of a reference string to the References object as a Reference object
	    			 ref.addReference(item.getTitle(), item.getAuthors());
		
	    		 }
	    		 
	    	 }
	    	 //adding the References object to the list
	    	 refList.add(ref);	 
	     }
	    
        e.close();
	     

	     //converts the list to an array
	     References[] refArray = new References[refList.size()];
		 Iterator<References> it = refList.iterator();
		 for(int j = 0 ; j < refList.size(); j++) {
				
			 refArray[j] = it.next();
		 }
	     
		 //checks if a split was carried out
		 if (split == null || split.equals("on")) {
			 
			 //deleting all splitted pdfs
			 for(int k = 0; k < farray1.length; k++) {
				 
				 farray1[k].delete();
			 } 
		 }
		
	     return refArray;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 /**
	  * This method loads all PDF files and extracts the metadata from each reference of a publication.
	  * For each publication it creates one <code>References<code> object. A <code>References<code> object includes 
	  * all references of a publication in shape of the corresponding number of <code>Reference<code> objects. 
	  * This Method uses multithreading with 16 Threads.
	  * 
	  * @param pathPDFs the path to the folder that contains the PDF files
	  * @return A list that contains all <code>References<code> objects
	  * @throws Exception
	  */
	 private References[] referenceParser16(String pathPDFs) throws Exception {

		 //loads the split state
		 PropertiesCache prop = PropertiesCache.getInstance();
		 String split = prop.getProperty("split");
		 
		 String path = pathPDFs;
		 
		 if(split == null || split.equals("on")) {
			 
			//extracts the pages which contain the references of the PDF
			 this.splitPDF(pathPDFs);
			 path = System.getProperty("user.dir")+ File.separator + "pdf_collection_splitted" + File.separator;
		 }
		 
		 

		 //getting the PDF files which contain the references
         File f1 = new File(path); 
	     File[] farray1 = f1.listFiles();

	     
	     //this section starts 16 Threads. Each of them extracts the single reference strings of the PDF documents 
	     int size = (farray1.length / 16) + 1;

	     //the absolute file number is divided into 16 so that each thread has the same number of files +-1
	     File[] files1 = new File[size];       
	     File[] files2 = new File[size];
	     File[] files3 = new File[size];
	     File[] files4 = new File[size];
	     File[] files5 = new File[size];
	     File[] files6 = new File[size];
	     File[] files7 = new File[size];
	     File[] files8 = new File[size];
	     File[] files9 = new File[size];
	     File[] files10 = new File[size];
	     File[] files11 = new File[size];
	     File[] files12 = new File[size];
	     File[] files13 = new File[size];
	     File[] files14 = new File[size];
	     File[] files15 = new File[size];
	     File[] files16 = new File[size];
	     
	     //filling the file arrays for each thread
	     int d = 0;
	     int i = 0;
	     int length = farray1.length;
	     while( d < length ){  

	    	 if(d < length ) {
	    		 files1[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files2[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files3[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files4[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files5[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files6[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files7[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files8[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files9[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files10[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files11[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files12[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files13[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files14[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files15[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 if(d < length ) {
	    		 files16[i] = farray1[d++];
	    	 }else {
	    		 break;
	    	 }
	    	 
	    	 i++;

	     }
	     //creates and starts 16 Threads
	     ReferenceExtractionThread t1 = new ReferenceExtractionThread(files1);
	     ReferenceExtractionThread t2 = new ReferenceExtractionThread(files2);
	     ReferenceExtractionThread t3 = new ReferenceExtractionThread(files3);
	     ReferenceExtractionThread t4 = new ReferenceExtractionThread(files4);
	     ReferenceExtractionThread t5 = new ReferenceExtractionThread(files5);
	     ReferenceExtractionThread t6 = new ReferenceExtractionThread(files6);
	     ReferenceExtractionThread t7 = new ReferenceExtractionThread(files7);
	     ReferenceExtractionThread t8 = new ReferenceExtractionThread(files8);
	     ReferenceExtractionThread t9 = new ReferenceExtractionThread(files9);
	     ReferenceExtractionThread t10 = new ReferenceExtractionThread(files10);
	     ReferenceExtractionThread t11 = new ReferenceExtractionThread(files11);
	     ReferenceExtractionThread t12 = new ReferenceExtractionThread(files12);
	     ReferenceExtractionThread t13 = new ReferenceExtractionThread(files13);
	     ReferenceExtractionThread t14 = new ReferenceExtractionThread(files14);
	     ReferenceExtractionThread t15 = new ReferenceExtractionThread(files15);
	     ReferenceExtractionThread t16 = new ReferenceExtractionThread(files16);


	     t1.start();
	     t2.start();
	     t3.start();
	     t4.start();
	     t5.start();
	     t6.start();
	     t7.start();
	     t8.start();
	     t9.start();
	     t10.start();
	     t11.start();
	     t12.start();
	     t13.start();
	     t14.start();
	     t15.start();
	     t16.start();

	     t1.join();
	     t2.join();
	     t3.join();
	     t4.join();
	     t5.join();
	     t6.join();
	     t7.join();
	     t8.join();
	     t9.join();
	     t10.join();
	     t11.join();
	     t12.join();
	     t13.join();
	     t14.join();
	     t15.join();
	     t16.join();


	     //each Thread gets the list which contains string arrays. 
	     //each string array contains the reference strings of one PDF
	     l1 = t1.getRefs();
	     l2 = t2.getRefs();
	     l3 = t3.getRefs();
	     l4 = t4.getRefs();
	     l5 = t5.getRefs();
	     l6 = t6.getRefs();
	     l7 = t7.getRefs();
	     l8 = t8.getRefs();
	     l9 = t9.getRefs();
	     l10 = t10.getRefs();
	     l11 = t11.getRefs();
	     l12 = t12.getRefs();
	     l13 = t13.getRefs();
	     l14 = t14.getRefs();
	     l15 = t15.getRefs();
	     l16 = t16.getRefs();
	

	     //getting a GROBID Engine instance in order to parse the reference strings
	     String pGrobidHome = System.getProperty("user.dir") + File.separator + "grobid";     
	     GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
	     GrobidProperties.getInstance(grobidHomeFinder);
	     Engine e = GrobidFactory.getInstance().createEngine();

	     //each PDF document corresponds to a References object. In this list all the References objects will be saved
	     LinkedList<References> refList = new LinkedList<References>();

	     
	     //loads the consolidation state
		 String con = prop.getProperty("consolidation");
		 
		 //checks if consolidation is on
		 int c = 0;
		 if (con != null) {
			 if(con.equals("on")) {
				 c = 1;
			 }
		 }
	     
	     
	     
	     /*
	      * The comments in the next block are also referred to the further 15 blocks 
	      */
	     
	     
	     
	     
	     int i1 = 1;
	     //extracts the metadata from each reference string
	     for( String[] refa : l1) {
	    	 //creates a new References object in which the title and the authors of each reference string are saved
	    	 References refs = new References();
	    	 for (String element : refa) {
	    		 
	    		 //extracts the metadata from the reference string
	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 //adding the extracted title and authors of a reference string to the References object as a Reference object
	    			 refs.addReference(item.getTitle(), item.getAuthors());
		
	    		 }
	    		 
	    	 }
	    	 //adding the References object to the list
	    	 refList.add(refs);	 
	    	 i1 = i1 + 16;
	     }

	     int i2 = 2;
	     for( String[] refa : l2) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i2 = i2 + 16;
	     }

	     int i3 = 3;
	     for( String[] refa : l3) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i3 = i3 + 16;
	     }

	     int i4 = 4;
	     for( String[] refa : l4) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i4 = i4 + 16;
	     }

	     int i5 = 5;
	     for( String[] refa : l5) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());
	
	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i5 = i5 + 16;
	     }
	     int i6 = 6;
	     for( String[] refa : l6) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i6 = i6 + 16;
	     }


	     int i7 = 7;
	     for( String[] refa : l7) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i7 = i7 + 16;
	     }

	     int i8 = 8;
	     for( String[] refa : l8) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i8 = i8 + 16;
	     }

	     int i9 = 9;
	     for( String[] refa : l9) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());
 
	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i9 = i9 + 16;
	     }

	     int i10 = 10;
	     for( String[] refa : l10) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i10 = i10 + 16;
	     }

	     int i11 = 11;
	     for( String[] refa : l11) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i11 = i11 + 16;
	     }

	     int i12 = 12;
	     for( String[] refa : l12) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i12 = i12 + 16;
	     }

	     int i13 = 13;
	     for( String[] refa : l13) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());
		
	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i13 = i13 + 16;
	     }
	     
	     int i14 = 14;
	     for( String[] refa : l14) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i14 = i14 + 16;
	     }

	     int i15 = 15;
	     for( String[] refa : l15) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());

	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i15 = i15 + 16;
	     }

	     int i16 = 16;
	     for( String[] refa : l16) {
	    	 References refs = new References();
	    	 for (String element : refa) {

	    		 BiblioItem item = e.processRawReference(element, c);
	    		 
	    		 if( item.getTitle() != null) {
	    			 refs.addReference(item.getTitle(), item.getAuthors());
	
	    		 }
	    	 }
	    	 refList.add(refs);
	    	 i16 = i16 + 16;
	     }
	     e.close();
	     

	     //converts the list to an array
	     References[] refArray = new References[refList.size()];
		 Iterator<References> it = refList.iterator();
		 for(int j = 0 ; j < refList.size(); j++) {
				
			 refArray[j] = it.next();
		 }
	     
		 //checks if a split was carried out
		 if (split == null || split.equals("on")) {
			 
			 //deleting all splitted pdfs
			 for(int k = 0; k < farray1.length; k++) {
				 
				 farray1[k].delete();
			 } 
		 }
		
	     
	     return refArray;

	 }
	 
	 
	 
	 /**
	  * This method extracts the metadata from the references of each publication and creates or extents a database with
	  * these data.
	  * 
	  * @param pathPDFs the path to the folder that contains the PDF files
	  * @param nameDB the name of the database
	  * @param type specifies if a new database shall be created or an existing one shall be extended
	  * @throws Exception
	  */
	 public void createDB(String pathPDFs, String nameDB, String type) throws Exception {
		
		//creates an new Database or loads an already existing one
	    String db_path = System.getProperty("user.dir") + File.separator + "co_citation_db" + File.separator + nameDB;
		Path paths =  Paths.get(db_path);
		managementService =  new DatabaseManagementServiceBuilder(paths).build();
	    graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	    registerShutdownHook( managementService );
	    
	    //loads the multithreading state
		PropertiesCache prop = PropertiesCache.getInstance();
		String multi = prop.getProperty("multi");
	    References[] refObjs;
		
	    //checks if multithreading shell be used
		if( multi.equals("on")) {
			//contains all the extracted titles and authors of each PDF in a structured form  
			refObjs = this.referenceParser16(pathPDFs);
		}else {
			//contains all the extracted titles and authors of each PDF in a structured form  
			refObjs = this.referenceParser1(pathPDFs);
		}
	    
	    
		
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
	    					
	    					
	    						if(allRefs2[n].getManipulatedTitle().equalsIgnoreCase(titleM1)){                 
	    						
	    							flag1 = true;
	    							inx1 = n;
	    						}
	    					
	    						if(allRefs2[n].getManipulatedTitle().equalsIgnoreCase(titleM2)){
	    						
	    							flag2 = true;
	    							inx2 = n;
	    						}
	    					}
	    					//true means that title1 and title2 occur together in this publication. So, they are co-cited.
	    					if(flag1 == true && flag2 == true) {
	    					
	    						bool[l][inx1] = true;
	    						bool[l][inx2] = true;
	    						
	    						//creating the corresponding nodes and relationships concerning the co-cited documents or increases the number/weight of
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
	    						
	    								if ( t.equalsIgnoreCase(titleM1)) {
	    									
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
	    	  							if (allRefs2[inx1].getAuthors() != null) {
	    	  								
	    	  								n1.setProperty("authors", allRefs2[inx1].getAuthors());    
	    	  							}else {
	    	  								n1.setProperty("authors", "no entry");
	    	  							}
	    	  							
	    								
	    							}
	    							//creating a new node if it does not exist yet
	    							if(flag4 == false) {
	    								
	    								n2 = tx.createNode(Labels.PUBLICATION);
	    	  							n2.setProperty("title", allRefs[k].getTitle());
	    	  							if (allRefs2[inx2].getAuthors() != null) {
	    	  								
	    	  								n2.setProperty("authors", allRefs2[inx2].getAuthors());   
	    	  							}else {
	    	  								n2.setProperty("authors", "no entry");
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
		
		 //stores the name in the properties 
	    if( type.equals("new")) {
	    	storeDBName(nameDB);
	    }
	 }
	 
	 
	 
	 
	 
	 private static void registerShutdownHook( final DatabaseManagementService managementService ){
		    
	       
	        Runtime.getRuntime().addShutdownHook( new Thread(){
	        
	            @Override
	            public void run(){
	            
	                managementService.shutdown();
	            }
	        } );
	    }
	 
	 
	 
	 
	 /**
	  * This method stores the name of the database in the properties file
   	  * 
	  * @param name name of the database
	  */
	 private void storeDBName(String name) {
		 
		 PropertiesCache prop = PropertiesCache.getInstance();
		 String names = prop.getProperty("cocit_dbs");
		 
		 if( names == null) {
			 
			 names = name + "$";
		 }else {
			 
			 names = names + name + "$";
		 }
		  prop.setProperty("cocit_dbs", names);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 /**
	  * This method manipulates the title. It removes several signs from the title.
	  * 
	  * @param title the title that should be manipulated
	  * @return the manipulated title
	  */
	 private String manipulateTitle(String title) {
		  
		 title = title.replaceAll("[^A-Za-z0-9]", "");
			
		return title;
	 }
	 
	
	 
}






