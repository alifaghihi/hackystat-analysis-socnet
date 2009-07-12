/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.graphmanagement;


/**
 *
 * @author Rachel Shadoan
 */
public class RelationshipNotFoundException extends Exception{
    
    private static final long serialVersionUID = 1;

    public RelationshipNotFoundException()
    {
        super();
    }

    public RelationshipNotFoundException(long id)
    {
        super("\nRelationship " + id + " does not exist in the graph.\n");
    }
    
    public RelationshipNotFoundException(String relationshiptype, String startNodeName,
            String startNodeType, String endNodeName, String endNodeType)
    {
        super("\nThe relationship of type " + relationshiptype + " starting at the " +
                "node of type " + startNodeType + " named " + startNodeName + " and " +
                "ending at the node of type " + endNodeType + " named " +endNodeName
                + " does not exist in the graph\n");
                
    }
    
    public RelationshipNotFoundException(String nodeName, String nodeType)
    {
        super("\nNo relationships connected to the node of type " + nodeType +
                " named " + nodeName + " exist in the graph.\n");
    }

    public RelationshipNotFoundException(String nodename, String nodetype, 
            String relationshipdirection)
    {
        super("\nNo "+ relationshipdirection + " relationships connected to the " +
                "node of type " + nodetype + " named " + nodename + " exist in the graph.\n");
    }

    public RelationshipNotFoundException(String nodename, String nodetype, 
            String relationshipdirection, String relationshipType)
    {
        super("\nNo "+ relationshipdirection + " relationships of type " + 
                relationshipType + " connected to the node of type " + nodetype 
                + " named " + nodename + " exist in the graph.\n");
    }
}
