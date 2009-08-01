/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.client;

import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationships;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;

/**
 *  This interface provides a specification for wrapping the REST API.
 * @author Rachel Shadoan
 */
public interface SocNetClientInterface {
    
    public void registerClient() throws Exception;

    /**
     * Constructs an http PUT call to insert a node of the given type and name 
     * into the database. 
     * 
     * @param nodeName - the name of the node to be added to the graph
     * @param nodeType - the type of the node to be added to the graph
     */
    public void addNode(String nodeName, String nodeType) throws Exception;
    
    /**
     * Creates a relationship between two nodes in the database.
     * 
     * @param startNodeName
     * @param startNodeType
     * @param endNodeName
     * @param endNodeType
     */
    public void addRelationshipTo(String startNodeName, String startNodeType,
            String endNodeName, String endNodeType, 
            String relationshipType) throws Exception;
    
    public XMLNodes getNodes(String nodeType) throws Exception;
    
    public XMLRelationships getNodes(String nodeName, 
                                       String nodeType, 
                                       String relationshipType) throws Exception;
    
    
    public boolean endRelationship(String startNodeName, String startNodeType,
            String endNodeName, String endNodeType, 
            String relationshipBetweenNodes) throws Exception;
    
    

}
