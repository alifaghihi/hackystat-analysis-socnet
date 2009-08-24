/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.users;

import org.hackystat.socnet.server.resource.SocNetResource;
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
        System.out.println("In UserResouce Init: " + userEmail);
    }

    @Override
    public Representation represent(Variant variant)
    {
        System.out.println("UserResource: represent");
        if (!validateUriUserIsUser() || !validateAuthUserIsAdminOrUriUser()) {
            System.out.println("User not validated!");
            return null;
        }

        try {
            if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
                String xmlData = super.userManager.getUserString(this.uriUser);
                System.out.println("UserResouce returning");
                return super.getStringRepresentation(xmlData);
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            setStatusInternalError(e);
        }
        return null;

    }

    /**
     * Indicate the POST method is supported.
     * 
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
     * <li>The XMLUser must be currently defined in this UserManager.
     * <li>Only the authenticated XMLUser or the Admin can update their user's properties.
     * <li>The payload must be an XML representation of a Properties instance.
     * </ul>
     * 
     * @param entity The entity to be posted.
     */
    @Override
    public void acceptRepresentation(Representation entity)
    {
    }

    /**
     * Indicate the DELETE method is supported.
     * 
     * @return True.
     */
    @Override
    public boolean allowDelete()
    {
        return true;
    }

    /**
     * Implement the DELETE method that deletes an existing XMLUser given their email. Only the
     * authenticated user (or the admin) can delete their XMLUser resource.
     */
    @Override
    public void removeRepresentations()
    {
    }
}
