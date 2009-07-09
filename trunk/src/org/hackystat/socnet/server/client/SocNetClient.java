/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.client;

import java.util.ArrayList;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;

/**
 *
 * @author Rachel Shadoan
 */
public class SocNetClient implements SocNetClientInterface {

    public void addNode(String nodeName, IsARelationshipType nodeType)
    {
        //yet to be implemented
    }

    public void addRelationshipTo(String startNodeName, 
            IsARelationshipType startNodeType, 
            String endNodeName, IsARelationshipType endNodeType,
            RelationshipType relationshipBetweenNodes)
    {
        
    }

    public ArrayList<String> getNodes(IsARelationshipType nodeType)
    {
        return new ArrayList<String>();
    }

}
