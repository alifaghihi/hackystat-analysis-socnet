/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.socialmediagraph;


import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.Node;
import org.hackystat.socnet.utils.JAXBHelper;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.w3c.dom.Document;

/**
 *
 * @author Rachel Shadoan
 */
public class SocialMediaGraphManager
{

    /** Holds the class-wide JAXBContext, which is thread-safe. */
    private JAXBContext jaxbContext;
    /** The Server associated with this SocialMediaGraphManager. */
    Server server;

    public SocialMediaGraphManager(Server server)
    {
        this.server = server;
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
     * Takes a String encoding of a Node in XML format and converts it to an instance. 
     * 
     * @param xmlString The XML string representing a SensorData.
     * @return The corresponding Node instance. 
     * @throws Exception If problems occur during unmarshalling.
     */
    public Node makeNode(String xmlString) throws Exception
    {
       return (Node) JAXBHelper.unmarshall(xmlString, jaxbContext);
    }

    public String getNodeRepresentation(Node node) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        return JAXBHelper.marshall(node, jaxbContext);
    }
}
