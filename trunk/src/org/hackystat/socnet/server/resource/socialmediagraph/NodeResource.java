/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.socialmediagraph;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.SocNetResource;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.socialmediagraph.graphmanagement.InvalidArgumentException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.NodeNotFoundException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.RelationshipNotFoundException;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

/**
 *
 * @author Rachel Shadoan
 */
public class NodeResource extends SocNetResource
{
    SocialMediaGraphManager manager;
    String nodeName;
    String nodeType;
    String relationshipType;
    String relationshipDirection;

    public NodeResource(Context context, Request request, Response response)
    {
        super(context, request, response);
        manager = (SocialMediaGraphManager) getContext().getAttributes().get("SocialMediaGraphManager");
        
        nodeName = (String) request.getAttributes().get("node");
        nodeType = (String) request.getAttributes().get("nodetype");
        relationshipType = (String) request.getAttributes().get("relationshiptype");
        relationshipDirection = (String) request.getAttributes().get("relationshipdirection");
        //System.out.println("NODE RESOURCE CREATED FOR" + "NAME: " + nodeName + " TYPE: " + nodeType);
    }

    @Override
    public Representation represent(Variant variant)
    {
        try
        {
          
            if(nodeName == null 
                    && relationshipType == null 
                    && relationshipDirection == null)
            {
                return manager.getRepresentation(manager.getNodes(nodeType));
            }
            else if(nodeName != null 
                    && nodeType != null
                    && relationshipType == null 
                    && relationshipDirection == null)
            {
                XMLNode returnedNode = manager.getNode(nodeType, nodeName);
                return manager.getNodeRepresentation(returnedNode);
            }
            else if(nodeName!= null
                    && nodeType != null
                    && relationshipType != null 
                    && relationshipDirection != null)
            {
                 return manager.getRepresentation(manager.getNodes(
                        manager.getNode(nodeType, nodeName), relationshipType, 
                        relationshipDirection));
            }
            
            else
                throw new InvalidArgumentException("The HTTP request is malformed.");
        }
        catch(InvalidArgumentException iae)
        {
            iae.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, iae);
            Logger.getLogger(NodeResource.class.getName()).log(Level.SEVERE, null, iae);
        }
        catch(NodeNotFoundException nnfe)
        {
            nnfe.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, nnfe);
            Logger.getLogger(NodeResource.class.getName()).log(Level.SEVERE, null, nnfe);
        }
        catch(RelationshipNotFoundException rnfe)
        {
            rnfe.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, rnfe);
            Logger.getLogger(NodeResource.class.getName()).log(Level.SEVERE, null, rnfe);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ex);
            Logger.getLogger(NodeResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
       
    }

    /**
     * Indicate the PUT method is supported.
     * 
     * @return True.
     */
    @Override
    public boolean allowPut()
    {
        return true;
    }

    /**
     * Implement the PUT method that creates a new sensor data instance.
     * <ul>
     * <li> The XML must be marshallable into a sensor data instance.
     * <li> The timestamp in the URL must match the timestamp in the XML.
     * <li> The User and SDT must exist.
     * </ul>
     * Note that we are not validating that this sensor data instance contains all of the Required
     * Properties specified by the SDT. This should be done later, on demand, as part of analyses.
     * <p>
     * We are also not at this point checking to see whether the User and SDT exist.
     * 
     * @param entity The XML representation of the new sensor data instance..
     */
    @Override
    public void storeRepresentation(Representation entity)
    {
        // Get the payload.
        String entityString = null;
        try
        {
            entityString = entity.getText();
        }
        catch (IOException e)
        {
            setStatusMiscError("Bad or missing content");
            return;
        }
        XMLNode n = null;
        try
        {
            n = manager.makeNode(entityString);
            manager.storeNode(n);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            ex.printStackTrace();
            setStatusMiscError("JAXB failed for some reason");
            return;
        }
           
    }
}
