package Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class manages all actions regarding the Properties. Especially, it caches a Properties-Object
 * with its data and provides access to this Object so that the data must only be loaded and stored
 * once to the file.
 * 
 * 
 * @author Benjamin Birner
 *
 */
public class PropertiesCache {
	
	//the Properties-Object that this class holds
	private final Properties prop = new Properties();
	private final String fileName = "database_names.properties";
	private boolean loadStatus;
	
        private PropertiesCache(){
		InputStream in = null;
		try {
			in = new FileInputStream(System.getProperty("user.dir") + File.separator + fileName);
			prop.load(in);
			loadStatus = true;
		} catch (FileNotFoundException e1) {
			System.err.println("Datei konnte nicht gefunden werden!!!");
			e1.printStackTrace();
			loadStatus = false;
		} catch (IOException e) {
			System.err.println("Ein- Ausgebefehler!");
			loadStatus = false;
			e.printStackTrace();
		}
	}
	//holds an instance of the PropertiesCache class
    //this object is returned if a Properties is required
	private static class PropertiesHolder{
		
		private static final PropertiesCache INSTANCE = new PropertiesCache();
		
	}
	
	
	/**
	 * returns the PropertiesCach-instance that is held by the PropertiesHolder.
	 * With the help of this object the data can be set, received or removed from the Properties. 
	 * Data can also be stored to the file.
	 * 
	 * @return the PropertiesCache-instance
	 */
	public static PropertiesCache getInstance() {
		return PropertiesHolder.INSTANCE;
	}
	
	
	
	/**
	 * gets and returns the load-status
	 * 
	 * @return true if the loading-process was successfully. Else false.
	 */
	public boolean getLoadStatus() {
		return loadStatus;
	}
	
	
	
	/**
	 * This method gets and returns the value that is saved in the Properties 
	 * for the key that the parameter <code>key</code> defines.
	 * 
	 * @param key the key to get the required value
	 * @return the value for the key
	 */
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	
	
	/**
	 * sets the <code>value</code> to the Properties.
	 * 
	 * @param key the key to set the value
	 * @param value the value that should be set
	 */
	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	
	
	/**
	 * removes the entry from the Properties for the <code>key</code>.
	 * 
	 * @param key the key that defines the entry that should be removed
	 */
	public void removeProperty(String key) {
		prop.remove(key);
	}
	
	
	
	/**
	 * stores the Properties to the file.
	 * This method is only called up in the program`s closing process.
	 * 
	 * @throws FileNotFoundException is thrown if the file can not be found
	 * @throws IOException is thrown if there is a problem regarding the IO
	 */
	public void store() throws FileNotFoundException, IOException{
		try(final OutputStream outputstream = new FileOutputStream(System.getProperty("user.dir") + File.separator + fileName);){
			prop.store(outputstream, null);
			outputstream.close();
		}
	}
}
