package Model;

import java.util.LinkedList;


/**
 * An instance of this class represents a co-author.
 * 
 * @author Benjamin Birner
 *
 */
public class CoAuthor {
	
	
	private String name;
	private LinkedList<String> titleList;
	private int number;
	
	/**
	 * 
	 * @param name name of the author
	 * @param titleList all titles regarding the authors publication's
	 * @param number the number of publications regarding the author
	 */
	CoAuthor(String name, LinkedList<String> titleList, int number) {
		
		this.name = name;
		this.titleList = titleList;
		this.number = number;
	}
	
	/**
	 * 
	 * @return the name of the author
	 */
	public String getName() {
		
		return name;
	}
	
	
	/**
	 * 
	 * @return all titles regarding the authors publication's
	 */
	public LinkedList<String> getTitleList() {
		
		return titleList;
	}
	
	/**
	 * 
	 * @return the number of publications regarding the author
	 */
	public int getNumberOfPublications() {
		
		return number;
	}

}
