/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.client;

import java.util.ArrayList;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;

/**
 *  This interface provides a specification for wrapping the REST API.
 * @author Rachel Shadoan
 */
public interface SocNetClientInterface {
    
    /**
     * Constructs an http PUT call to insert a node of the given type and name 
     * into the database. 
     * 
     * @param nodeName - the name of the node to be added to the graph
     * @param nodeType - the type of the node to be added to the graph
     */
    public void addNode(String nodeName, IsARelationshipType nodeType);
    
    /**
     * Creates a relationship between two nodes in the database.
     * 
     * @param startNodeName
     * @param startNodeType
     * @param endNodeName
     * @param endNodeType
     */
    public void addRelationshipTo(String startNodeName, IsARelationshipType startNodeType,
            String endNodeName, IsARelationshipType endNodeType, 
            RelationshipType relationshipBetweenNodes);
    
    public ArrayList<String> getNodes(IsARelationshipType nodeType);
    
    
    public boolean endRelationship(String startNodeName, IsARelationshipType startNodeType,
            String endNodeName, IsARelationshipType endNodeType, 
            RelationshipType relationshipBetweenNodes);
    
    

}
