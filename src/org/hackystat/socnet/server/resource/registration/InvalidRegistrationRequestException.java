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

    public InvalidRegistrationRequestException(String message)
    {
        super(message);
    }

}
