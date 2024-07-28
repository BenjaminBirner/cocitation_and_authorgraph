package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.bibref.KMeansBibReferenceExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.structure.model.BxDocument;


/**
 * In instance of this class is responsible for the reference-string-extraction of a publication.
 * Moreover, it extends the <code>Thread<code> class because the reference-string-extraction is parallelized
 * 
 * @author Benjamin Birner
 *
 */
class ReferenceExtractionThread extends Thread {

	private File[] files;
	private LinkedList<String[]> refs = new LinkedList<String[]>();


	ReferenceExtractionThread(File[] files) {

		this.files = files;
	}


	LinkedList<String[]> getRefs() {

		return refs;
	}


	@Override
	public void run() {

		try {
			extractRefs();
		} catch (AnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * This method is called by the <code>run</code> method. It extracts the reference-strings of the PDF files which the attribute <code>files</code> 
	 * contains. The results are saved in the List in the attribute <code>refs</code>. Each string-array in this List contains the references of one 
	 * PDF document.
	 * Here the CERMINE library is used.
	 * 
	 * @throws AnalysisException
	 * @throws IOException
	 */
	private void extractRefs() throws AnalysisException, IOException {


		if(files != null) {

	    	  for (File file : files) {
	    		  
	    		  if(file != null) {
	    			 
	    			  //gets a new ContentExtractor instance
	    			  ContentExtractor ce = new ContentExtractor();  //this is the method which takes long runtime

		    		  String path = file.getAbsolutePath();

		    		  InputStream pdfIS = new FileInputStream(path);
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

	}

}
