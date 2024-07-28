package Model;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * An instance of this class represents all references of a publication.
 * 
 * @author Benjamin Birner
 *
 */
class References {   
	
	//contains all the single references of a publication
	private LinkedList<Publication> referenceList = new LinkedList<Publication>();
	
	
	/**
	 * creates a <code>Publication<code> object and adds it to the <code>referenceList<code>
	 * 
	 * @param title the title of the publication
	 * @param authors the authors of the publication
	 */
	void addReference(String title, String authors) {
		
		Publication ref = new Publication(title, authors);
		referenceList.add(ref);
	}
	
	
	/**
	 *  
	 * @return the <code>referenceList<code> that contains all references of a publication
	 */
	public LinkedList<Publication> getAllReferences(){
		
		return referenceList;
	}
	
	/**
	 * 
	 * @return the <code>referenceList<code> that contains all references of a publication as an array
	 */
	Publication[] getAllReferencesAsArray(){
		
		Publication[] a = new Publication[referenceList.size()];
		Iterator<Publication> it = referenceList.iterator();
		for(int i = 0 ; i < referenceList.size(); i++) {
			
			a[i] = it.next();
		}
	
		return a;
	}
	
	
	/**
	 * 
	 * @return the size of the <code>referenceList<code> that contains all references
	 */
	int size() {
		
		return referenceList.size();
	}
	
}
