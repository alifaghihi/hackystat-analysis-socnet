/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.users;

import org.hackystat.socnet.server.resource.SocNetResource;
import org.restlet.Context;
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

    public UserResource(Context context, Request request, Response response)
    {
        super(context, request, response);
    }

    @Override
    public Representation represent(Variant variant)
    {
        throw new UnsupportedOperationException("Not supported yet.");
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
     * <li> The User must be currently defined in this UserManager.
     * <li> Only the authenticated User or the Admin can update their user's properties. 
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
     * Implement the DELETE method that deletes an existing User given their email.
     * Only the authenticated user (or the admin) can delete their User resource.
     */
    @Override
    public void removeRepresentations()
    {
    }
}
