/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.users;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.SocNetResource;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

/**
 *
 * @author Rachel Shadoan
 */
public class UserResource extends SocNetResource
{

    String userEmail;

    public UserResource(Context context, Request request, Response response)
    {
        super(context, request, response);

        userEmail = (String) request.getAttributes().get("user");
    }

    @Override
    public Representation represent(Variant variant)
    {

        if (!validateUriUserIsUser() ||
                !validateAuthUserIsAdminOrUriUser())
        {
            return null;
        }

        try
        {
            if (variant.getMediaType().equals(MediaType.TEXT_XML))
            {
                String xmlData = super.userManager.getUserString(this.uriUser);
                return super.getStringRepresentation(xmlData);
            }
        }
        catch (RuntimeException e)
        {
            setStatusInternalError(e);
        }
        return null;
        
    }

    /** 
     * Indicate the POST method is supported. 
     * @return True.
     */
    @Override
    public boolean allowPost()
    {
        return true;
    }

    /**
     * Implement the POST method that updates the properties associated with a user.
     * <ul> 
     * <li> The XMLUser must be currently defined in this UserManager.
     * <li> Only the authenticated XMLUser or the Admin can update their user's properties. 
     * <li> The payload must be an XML representation of a Properties instance.
     * </ul>
     * @param entity The entity to be posted.
     */
    @Override
    public void acceptRepresentation(Representation entity)
    {
    }

    /** 
     * Indicate the DELETE method is supported. 
     * @return True.
     */
    @Override
    public boolean allowDelete()
    {
        return true;
    }

    /**
     * Implement the DELETE method that deletes an existing XMLUser given their email.
     * Only the authenticated user (or the admin) can delete their XMLUser resource.
     */
    @Override
    public void removeRepresentations()
    {
    }
}
