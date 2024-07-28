package Model;


/**
 * An instance of this class saves the data regarding the loaded database respectively the database that
 * the user has selected.
 * 
 * @author Benjamin Birner
 *
 */
public class CurrentLoad {
	
	private String db;
	
	//describes the type (author or co-citation database)
	//author = 0
	//co-citation = 1
	private int type;
	
	public CurrentLoad() {
		
		// -1 means that there is no selected/loaded database
		db = "-1";
		type = -1;
	}
	
	/**
	 * 
	 * @return the name of the loaded database
	 */
	public String getDB() {
		
		return db;
	}
	
	
	/**
	 * sets the name of the loaded database
	 * @param db database name
	 */
	public void setDB(String db) {
		
		this.db = db;
	}
	
	/**
	 * sets the type of the loaded database.
	 * author-database: 0
	 * co-citation-database: 1
	 * @param type 0 or 1
	 */
	public void setType(int type) {
		
		this.type = type;
	}

	/**
	 * 
	 * @return  -1 if no database is loaded; 0 for author database; 1 for co-citation database
	 */
	public int getType() {
		
		return type;
	}
}
