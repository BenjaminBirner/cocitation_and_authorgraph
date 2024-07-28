package Model;



/**
 * An instance of this class represents a publication with the corresponding data.
 * 
 * @author Benjamin Birner
 *
 */
public class Publication {
	
	
	
	private String title;
	private String manipulatedTitle;
	private String authors;
	private String cocitations;
	private String degree;
	
	
	/**
	 * 
	 * @param title title of the publication
	 * @param authors authors of the publication
	 */
	Publication(String title, String authors) {
		
		this.title = title; 
		this.authors = authors;
		this.manipulatedTitle = manipulateTitle(title);	
		this.cocitations = "-1";
		this.degree = "-1";
	}
	
	
	
	/**
	 * 
	 * @param title title of the publication
	 * @param authors authors of the publication
	 * @param cocitations number of co-citations
	 */
	Publication(String title, String authors, String cocitations) {
		
		this.title = title; 
		this.authors = authors;
		this.manipulatedTitle = manipulateTitle(title);	
		this.cocitations = cocitations;
		this.degree = "-1";
	}


	/**
	 * 
	 * @param title title of the publication
	 * @param authors authors of the publication
	 * @param cocitations number of co-citations
	 * @param degree degree of the node respectively the number of different co-citations
	 */
	Publication(String title, String authors, String cocitations, String degree) {
	
	this.title = title; 
	this.authors = authors;
	this.manipulatedTitle = manipulateTitle(title);	
	this.cocitations = cocitations;
	this.degree = degree;
	}
	
	/**
	 * 
	 * @return authors of the publication
	 */
	public String getAuthors() {
		 
		return authors;
	}
	
	/**
	 * 
	 * @return title of the publication without signs and spaces
	 */
	public String getManipulatedTitle() {
		
		return manipulatedTitle;
	}
	
	
	/**
	 * 
	 * @return title of the publication
	 */
	public String getTitle() {
		
		return title;
	}
	
	/**
	 * 
	 * @return number of co-citations
	 */
	public String getCocitations() {
		
		return cocitations;
	}
	
	/**
	 * 
	 * @return degree of the node respectively the number of different co-citations
	 */
	public String getDegree() {
		
		return degree;
	}
	
	
	
	
	
	
	/**
	 * This method manipulates the title. It removes several signs from the title.
	 * 
	 * @param title the title that should be manipulated
	 * @return the manipulated title
	 */
	private String manipulateTitle(String title) {
		
		String maniTitle = title;
		maniTitle = maniTitle.replaceAll("[^A-Za-z0-9]", "");
		return maniTitle;
	}
	
	
}
