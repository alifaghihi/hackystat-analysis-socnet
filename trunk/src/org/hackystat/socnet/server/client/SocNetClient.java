/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.client;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationships;
import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.hackystat.socnet.utils.JAXBHelper;
import org.restlet.data.Response;

/**
 *
 * @author Rachel Shadoan
 */
public class SocNetClient implements SocNetClientInterface
{

    JAXBContext jaxbContext;
    String serveruri;

    public SocNetClient(String serverPath) throws JAXBException
    {
        this.jaxbContext = JAXBContext.newInstance(
                org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
        this.serveruri = serverPath;
    }

    public void addNode(String nodeName, String nodeType) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        XMLNode node = new XMLNode();
        node.setName(nodeName);
        node.setType(nodeType);

        String xml = JAXBHelper.marshall(node, jaxbContext);
        String serviceuri = SocNetRestApiHelper.getURI(serveruri, "nodes/" + node.getType() + "/" + node.getName());
        Response response = SocNetRestApiHelper.PUT(serviceuri, xml);
        
        if (!response.getStatus().isSuccess())
        {
            throw new RuntimeException("Add node failed for " + nodeType + ":" + nodeName + "!  Message was: " + response.getStatus().getDescription());
        }
    }

    public void addRelationshipTo(String startNodeName, String startNodeType, String endNodeName, String endNodeType, String relationshipType) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        addNode(startNodeName, startNodeType);
        addNode(endNodeName, endNodeType);

        XMLNode startNode = new XMLNode();
        startNode.setType(startNodeType);
        startNode.setName(startNodeName);

        XMLNode endNode = new XMLNode();
        endNode.setType(endNodeType);
        endNode.setName(endNodeName);

        XMLRelationship rel = new XMLRelationship();
        rel.setType(relationshipType);

        ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) rel.getXMLNode();

        nodes.add(startNode);
        nodes.add(endNode);
        String servicePath = "relationships/" + rel.getType() + "/" +
                startNode.getType() + "/" + startNode.getName() + "/" +
                endNode.getType() + "/" + endNode.getName();

        String serviceuri = SocNetRestApiHelper.getURI(serveruri, servicePath);
        String xml = JAXBHelper.marshall(rel, jaxbContext);

        Response response = SocNetRestApiHelper.PUT(serviceuri, xml);

        if (!response.getStatus().isSuccess())
        {
            throw new RuntimeException("Add relationship failed!  Message was: " + response.getStatus().getDescription());
        }
    }

    public XMLNodes getNodes(String nodeType) throws IOException, JAXBException
    {
        String serviceuri = SocNetRestApiHelper.getURI(serveruri,
                "nodes/" + nodeType);
        
        String xml = SocNetRestApiHelper.GET(serviceuri);
        XMLNodes nodes = (XMLNodes) JAXBHelper.unmarshall(xml, jaxbContext);
        return nodes;
    }

    public XMLRelationships getNodes(String nodeName, String nodeType, 
                                     String relationshipType) throws IOException, JAXBException
    {
        String path = "nodes/" + nodeName +"/" + nodeType + "/" 
                + relationshipType + "/" + "INCOMING";
        String serviceuri = SocNetRestApiHelper.getURI(serveruri, path);
        
        String xml = SocNetRestApiHelper.GET(path);
        
        XMLRelationships rels = (XMLRelationships) JAXBHelper.unmarshall(xml, jaxbContext);
        
        return rels;
    }

    public boolean endRelationship(String startNodeName, String startNodeType, String endNodeName, String endNodeType, String relationshipBetweenNodes) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
