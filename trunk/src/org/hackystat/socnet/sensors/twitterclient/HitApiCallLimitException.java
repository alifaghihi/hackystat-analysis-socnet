/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.twitterclient;

/**
 *
 * @author Rachel Shadoan
 */
class HitApiCallLimitException extends Exception{
    
    public static final long serialVersionUID = 1l;
    public HitApiCallLimitException(int callsMade, int callsRemaining)
    {
        super("You have made " + callsMade + " API calls this hour. You have " 
                + callsRemaining + " calls remaining.");        
    }

}
