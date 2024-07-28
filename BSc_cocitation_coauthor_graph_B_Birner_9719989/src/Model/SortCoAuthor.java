package Model;

import java.util.Comparator;

/**
 * This class implements the <code>Comparator<><code> interface and is responsible for sorting <code>CoAuthor<code> objects according to the
 * attribute <code>name<code> correctly.
 * 
 * @author Benjamin Birner
 *
 */
class SortCoAuthor implements Comparator<CoAuthor> {

	
	@Override
	public int compare(CoAuthor c1, CoAuthor c2) {
		
		String co1 = c1.getName();
        String co2 = c2.getName();
        
        char ch1 = co1.charAt(0);
        char ch2 = co2.charAt(0);
            
        if( ch1 < ch2 ) {return -1;}
        if( ch1 > ch2 ) {return 1;} 
        if(ch1 == ch2) {
        	
        	ch1 = co1.charAt(1);
        	ch2 = co2.charAt(1);
        	
        	if( ch1 < ch2 ) {return -1;}
            if( ch1 > ch2 ) {return 1;}
            if(ch1 == ch2) {
            	
            	ch1 = co1.charAt(2);
            	ch2 = co2.charAt(2);
            	
            	if( ch1 < ch2 ) {return -1;}
                if( ch1 > ch2 ) {return 1;}
                if(ch1 == ch2) {
                	
                	ch1 = co1.charAt(3);
                	ch2 = co2.charAt(3);
                	
                	if( ch1 < ch2 ) {return -1;}
                    if( ch1 > ch2 ) {return 1;}
                    if(ch1 == ch2) {
                    	
                    	ch1 = co1.charAt(4);
                    	ch2 = co2.charAt(4);
                    	
                    	if( ch1 < ch2 ) {return -1;}
                        if( ch1 > ch2 ) {return 1;}
                    }
                } 
            }   
        }
		return 0;
	}

}
