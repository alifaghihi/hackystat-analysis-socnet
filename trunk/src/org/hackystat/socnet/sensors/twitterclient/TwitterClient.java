/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.twitterclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;

/**
 *
 * @author Rachel Shadoan
 */
public class TwitterClient {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException
    {
 
        //create the twitterPoller object
        TwitterPoller twitterPoller = new TwitterPoller(args[0]);
        
        //begin polling!
        while(true)
        {
            try
            {
                //update the database with information from twitter, if anything
                //is new and different
                twitterPoller.update();
                
                //wait for an hour before doing it again
                twitterPoller.wait(3600000);
            }
            catch (HitApiCallLimitException ex)
            {
                //if we've hit the API limit, print out the error message
                System.out.println(ex.getMessage());
                System.out.println("Sleeping now.");
                
                //and then go to sleep.
                twitterPoller.wait(3600000);
            }
        }
    }

}
