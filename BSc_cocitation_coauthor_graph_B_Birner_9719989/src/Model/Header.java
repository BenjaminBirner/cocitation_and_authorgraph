package Model;

import java.util.LinkedList;

/**
 * An instance of this class represents the header (authors and title) of a Publication.
 * 
 * @author Benjamin Birner
 *
 */
 class Header {   
	
	
	private String title;
	private LinkedList<String> authors = new LinkedList<String>();
	
	
	/**
	 * 
	 * @param title title of the publication
	 * @param authors authors of the publication
	 */
	Header(String title, LinkedList<String> authors) {
		
		this.title = title;
		this.authors = cleanNames(authors);

	}
	
	/**
	 * 
	 * @return the original authors of the publication 
	 */
	LinkedList<String> getAuthors() {
		 
		return authors;
	}
	
	/**
	 * 
	 * @return the title of the publication
	 */
	String getTitle() {
		
		return title;
	}
	
	
	/**
	 * removes the title from the names
	 * @param name
	 * @return
	 */
	private LinkedList<String> cleanNames(LinkedList<String> name) {
		
		LinkedList<String> authors = new LinkedList<String>();
		
		for(String auth : name) {
			
			auth.replace("Dr. ", "");
			auth.replace("Prof. ", "");
			auth.replace("Mr.", "");
			auth.replace("Mrs.", "");
			auth.replace("-", "");
			auth.replace("-", "");
			
			authors.add(auth);
		
		}
		return authors;
	}
	
}
