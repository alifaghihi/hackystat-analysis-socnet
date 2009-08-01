/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.resource.registration;

/**
 *
 * @author rachel
 */
public class InvalidRegistrationRequestException extends Exception{
    
    private static final long serialVersionUID = 1;

    public InvalidRegistrationRequestException(String message)
    {
        super(message);
    }

}
