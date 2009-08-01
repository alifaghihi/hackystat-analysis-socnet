/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.resource.socialmediagraph;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hackystat.socnet.server.resource.SocNetResource;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
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
public class RelationshipResource extends SocNetResource{

    SocialMediaGraphManager manager;
    String relationshiptype;
    String startnodetype;
    String startnodename;
    String endnodetype;
    String endnodename;

    public RelationshipResource(Context context, Request request, Response response)
    {
        super(context, request, response);
        manager = (SocialMediaGraphManager) getContext().getAttributes().get("SocialMediaGraphManager");
        relationshiptype = (String) request.getAttributes().get("relationshiptype");
        startnodetype  = (String) request.getAttributes().get("startnodetype");
        startnodename  = (String) request.getAttributes().get("startnodename");
        endnodetype  = (String) request.getAttributes().get("endnodetype");
        endnodename  = (String) request.getAttributes().get("endnodename");
   
    }
    
    @Override
    public Representation represent(Variant variant)
    {
        XMLNode startNode;
        XMLNode endNode;
        
        try
        {
            if (!validateAuthUserIsUser() ||
                    !validateAuthUserIsAdminOrUser())
            {
                System.out.println("User not validated!");
                return null;
                
            }
            
            if(startnodetype == null || startnodename == null 
                    || endnodetype == null || startnodename == null
                    || relationshiptype == null)
                throw new InsufficientArgumentsException("Too few arguments were " +
                        "supplied with the http request");
           
            
            startNode = manager.getNode(startnodetype, startnodename);
            endNode = manager.getNode(endnodetype, endnodename);
                        
            return manager.getRelationshipRepresentation(
                    manager.getRelationship(relationshiptype, startNode, endNode));
            
        }
        catch(InsufficientArgumentsException iae)
        {
            iae.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, iae);
            Logger.getLogger(RelationshipResource.class.getName()).log(Level.SEVERE, null, iae);
        }
        catch(RelationshipNotFoundException rnfe)
        {
            rnfe.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, rnfe);
            Logger.getLogger(RelationshipResource.class.getName()).log(Level.SEVERE, null, rnfe);
        }
        catch(NodeNotFoundException nnfe)
        {
            nnfe.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, nnfe);
            Logger.getLogger(RelationshipResource.class.getName()).log(Level.SEVERE, null, nnfe);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ex);
            Logger.getLogger(RelationshipResource.class.getName()).log(Level.SEVERE, null, ex);
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
            if (!validateAuthUserIsUser() ||
                    !validateAuthUserIsAdminOrUser())
            {
                System.out.println("User not validated!");
                throw new UserNotAuthorizedException();
            }
            
            entityString = entity.getText();
        }
        catch(UserNotAuthorizedException unae)
        {
            unae.printStackTrace();
            setStatusMiscError("The Authenticated User is not authorized to " +
                    "put.");
        }
        catch (IOException e)
        {
            setStatusMiscError("Bad or missing content");
            return;
        }
        XMLRelationship r = null;
        try
        {
            r = manager.makeRelationship(entityString);
            try
            {
                manager.getNode(r.getXMLNode().get(0).getType(), r.getXMLNode().get(0).getName());
            }
            catch(NodeNotFoundException nnfe)
            {
                System.out.println("The node " + r.getXMLNode().get(0).getName() + "does not exist." +
                        "Creating it now.");
                manager.storeNode(r.getXMLNode().get(0));
            }
            
            try
            {
                manager.getNode(r.getXMLNode().get(1).getType(), r.getXMLNode().get(1).getName());
            }
            catch(NodeNotFoundException nnfe)
            {
                System.out.println("The node " + r.getXMLNode().get(1).getName() + "does not exist." +
                        "Creating it now.");
                manager.storeNode(r.getXMLNode().get(1));
            }
            
            manager.storeRelationship(r);
        }
        catch (Exception ex)
        {
            setStatusMiscError("JAXB failed for some reason");
            ex.printStackTrace();
            return;
        }
                              
    }

    class InsufficientArgumentsException extends Exception
    {
        private static final long serialVersionUID = 1;

        private InsufficientArgumentsException(String message)
        {
            super(message);
        }
        
    }
}
