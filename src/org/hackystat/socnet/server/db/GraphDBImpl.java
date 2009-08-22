/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.db;

import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.socialmediagraph.graphmanagement.InvalidArgumentException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.NodeNotFoundException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.RelationshipNotFoundException;

/**
 *
 * @author cody
 */
public interface GraphDBImpl extends DBImpl
{

    public XMLGregorianCalendar getDateLastTelemetryData(XMLNode startNode,
            XMLNode endNode) throws NodeNotFoundException, InvalidArgumentException;

    public void storeNode(XMLNode xmlNode);
    
    public void storeRelationship(XMLRelationship xmlRel) throws NodeNotFoundException;
    
    public XMLNode getNode(String type, String name)throws NodeNotFoundException;
    
    public XMLNode getNode(int nodeID) throws NodeNotFoundException;
    
    public List<XMLNode> getNodes() throws NodeNotFoundException;
    
    public List<XMLNode> getNodes(String typeOfNodes) throws NodeNotFoundException;
    
    public List<XMLNode> getNodes(XMLNode connectedTo, String relationshipType, String direction) 
            throws NodeNotFoundException, RelationshipNotFoundException, InvalidArgumentException;
           
    public XMLRelationship getRelationship(String type, XMLNode startNode, XMLNode endNode) throws NodeNotFoundException, RelationshipNotFoundException;
    
    public XMLRelationship getRelationship(int relationshipID) throws RelationshipNotFoundException;
    
    public List<XMLRelationship> getRelationships() throws RelationshipNotFoundException;
    
    public List<XMLRelationship> getRelationships(XMLNode connectedTo) throws RelationshipNotFoundException, NodeNotFoundException;
    
    public List<XMLRelationship> getRelationships(XMLNode connectedTo, String direction) throws InvalidArgumentException, RelationshipNotFoundException, NodeNotFoundException;
    
    public List<XMLRelationship> getRelationships(XMLNode connectedTo, String relationshipType, String direction) throws InvalidArgumentException, RelationshipNotFoundException, NodeNotFoundException;
    
    public void deleteNode(int nodeID) throws NodeNotFoundException;
    
    public void deleteNode(XMLNode toDelete) throws NodeNotFoundException;
    
    public void terminateRelationship(int relationshipID) throws NodeNotFoundException, RelationshipNotFoundException;
    
    public void terminateRelationship(XMLRelationship xmlRel)throws NodeNotFoundException, RelationshipNotFoundException;

    public void updateRelationship(XMLRelationship r);
            
    
}
