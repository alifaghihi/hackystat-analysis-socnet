/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.db.neo;

import java.io.File;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.hackystat.socnet.server.ServerProperties.GRAPH_DB_DIR_KEY;

import java.util.List;
import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.db.GraphDBImpl;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.socialmediagraph.graphmanagement.GraphManager;
import org.hackystat.socnet.socialmediagraph.graphmanagement.InvalidArgumentException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.NodeNotFoundException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.RelationshipNotFoundException;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;

/**
 *
 * @author Rachel Shadoan
 */
public class NeoGraphDB implements GraphDBImpl{

    private Server server;
    
    private String graphDir;
    
    private GraphManager graphManager;
    boolean freshlyCreated = false;
    public NeoGraphDB(Server server) throws DatatypeConfigurationException
    {
        this.server = server;
        graphDir = server.getServerProperties().get(GRAPH_DB_DIR_KEY);
        
        graphManager = null;
        initialize();
    }

    NeoGraphDB(String graphDirectory) throws DatatypeConfigurationException
    {
        graphManager = new GraphManager(graphDirectory);
    }
    
    public void initialize() throws DatatypeConfigurationException
    {
        if(graphManager == null)
        {
            File dir = new File(graphDir);
            freshlyCreated = !dir.exists();
            graphManager = new GraphManager(graphDir);
        }
    }

    public boolean isFreshlyCreated()
    {
        return freshlyCreated;
    }

    public void storeNode(XMLNode xmlNode)
    {
        graphManager.addNode(xmlNode.getType(), xmlNode.getName());
    }

    public void storeRelationship(XMLRelationship xmlRel) throws NodeNotFoundException
    {
        graphManager.addRelationship(xmlRel);
    }

    public XMLNode getNode(String type, String name) throws NodeNotFoundException
    {
        SocialMediaNode smNode = graphManager.getNode(type, name);

        XMLNode returnedNode = graphManager.convertToXMLNode(smNode);
      
        return returnedNode;
    }

    public XMLNode getNode(int nodeID) throws NodeNotFoundException
    {
        return graphManager.convertToXMLNode(graphManager.getNode(nodeID));
    }

    public XMLRelationship getRelationship(String type, XMLNode startNode, XMLNode endNode) throws NodeNotFoundException, RelationshipNotFoundException
    {
       return graphManager.getRelationship(type, startNode, endNode);
    }
    
    public XMLRelationship getRelationship(int relationshipID) throws RelationshipNotFoundException
    {
        return graphManager.getRelationship(relationshipID);
    }

    public void deleteNode(int nodeID)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteNode(XMLNode toDelete)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void terminateRelationship(int relationshipID)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void terminateRelationship(XMLRelationship xmlRel)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<XMLNode> getNodes() throws NodeNotFoundException
    {
        return graphManager.getNodes();
    }

    public List<XMLNode> getNodes(String typeOfNodes) throws NodeNotFoundException
    {
        return graphManager.getNodes(typeOfNodes);
    }

    public List<XMLNode> getNodes(XMLNode connectedTo, String relationshipType, String direction) throws NodeNotFoundException, RelationshipNotFoundException, InvalidArgumentException
    {
        return graphManager.getNodes(connectedTo, relationshipType, direction);
    }

    public List<XMLRelationship> getRelationships() throws RelationshipNotFoundException
    {
        return graphManager.getRelationships();
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo) throws RelationshipNotFoundException
    {
       return graphManager.getRelationships(connectedTo);
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo, 
            String direction) throws InvalidArgumentException, 
            NodeNotFoundException, RelationshipNotFoundException
    {
        return graphManager.getRelationships(connectedTo, direction);
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo, 
            String relationshipType, String direction) throws 
            InvalidArgumentException, NodeNotFoundException, 
            RelationshipNotFoundException
    {
        return graphManager.getRelationships(connectedTo, relationshipType, direction);
    }

    public void shutdown()
    {
        if (graphManager != null)
        {
            graphManager.exit();
            graphManager = null;
        }
    }

    public XMLGregorianCalendar getDateLastTelemetryData(XMLNode startNode,
            XMLNode endNode) throws NodeNotFoundException, InvalidArgumentException
    {
        return graphManager.getLatestTelemetryDate(startNode, endNode);
    }

    public void updateRelationship(XMLRelationship r)
    {
        graphManager.updateRelationship(r);
    }

}
