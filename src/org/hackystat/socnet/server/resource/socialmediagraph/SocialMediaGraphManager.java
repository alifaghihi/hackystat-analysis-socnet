/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.socialmediagraph;


import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.ServerProperties;
import org.hackystat.socnet.server.db.GraphDBImpl;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.utils.JAXBHelper;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;

/**
 *
 * @author Rachel Shadoan
 */
public class SocialMediaGraphManager
{
    /** Talks to the database*/
    private GraphDBImpl graphImp;
    
    /** Holds the class-wide JAXBContext, which is thread-safe. */
    private JAXBContext jaxbContext;
    
    /** The Server associated with this SocialMediaGraphManager. */
    Server server;

    public SocialMediaGraphManager(Server server)
    {
        this.server = server;
        this.graphImp = (GraphDBImpl) server.getContext().getAttributes().get(
                ServerProperties.GRAPH_IMPL_KEY);
        
        try
        {
            this.jaxbContext =
                    JAXBContext.newInstance(
                    org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
        }
        catch (Exception e)
        {
            String msg = "Exception during SocialMediaGraphManager initialization processing";
            server.getLogger().warning(msg + "\n" + StackTrace.toString(e));
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Takes a String encoding of a XMLNode in XML format and converts it to an instance. 
     * 
     * @param xmlString The XML string representing a SensorData.
     * @return The corresponding XMLNode instance. 
     * @throws Exception If problems occur during unmarshalling.
     */
    public XMLNode makeNode(String xmlString) throws Exception
    {
       return (XMLNode) JAXBHelper.unmarshall(xmlString, jaxbContext);
    }

    public Representation getNodeRepresentation(XMLNode node) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        return new StringRepresentation(JAXBHelper.marshall(node, jaxbContext));
    }
    
    public XMLRelationship makeRelationship(String xmlString) throws Exception
    {
        return (XMLRelationship) JAXBHelper.unmarshall(xmlString, jaxbContext);
    }
    
    public Representation getRelationshipRepresentation(XMLRelationship rel) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        return new StringRepresentation(JAXBHelper.marshall(rel, jaxbContext));
    }
    
    public void storeRelationship(XMLRelationship rel)
    {
        graphImp.storeRelationship(rel);
    }
    
    public void storeNode(XMLNode node)
    {
        graphImp.storeNode(node);
    }
    
    public List<XMLNode> getNodes()
    {
        return graphImp.getNodes();
    }
    
    public XMLNode getNode(String nodetype, String nodename)
    {
        return graphImp.getNode(nodetype, nodename);
    }
}
