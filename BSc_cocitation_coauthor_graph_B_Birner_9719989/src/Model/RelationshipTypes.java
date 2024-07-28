package Model;

import org.neo4j.graphdb.RelationshipType;


/**
 * Includes the relationship labels
 * 
 * @author Benjamin Birner
 *
 */
public enum RelationshipTypes implements RelationshipType{ PUBLISHED_TOGETHER, CO_CITED;

}
