package Model;

import java.util.Comparator;


/**
 * This class implements the <code>Comparator<><code> interface and is responsible for sorting <code>Publication<code> objects according to the
 * attribute <code>cocitations<code> correctly.
 * 
 * @author Benjamin Birner
 *
 */
class SortRelevance implements Comparator<Publication> {
	
	@Override
	public int compare(Publication p1, Publication p2) {
		
		int co1 =  Integer.parseInt(p1.getCocitations());
        int co2 =  Integer.parseInt(p2.getCocitations());
        
        if( co1 < co2 ) {return -1;}
        if( co1 > co2 ) {return 1;} 
        
		return 0;
	}

}
