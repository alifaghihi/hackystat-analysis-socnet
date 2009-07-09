/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.twitterclient;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author Rachel Shadoan
 */
public class Test {
    
    public static void main(String[] args) throws TwitterException
    {
        Twitter myTwitter = new Twitter("rachelshadoan", "bravo422piper");
        
        ArrayList<User> currentFriends = (ArrayList<User>) myTwitter.getFriends();

         System.out.println("You have " + myTwitter.rateLimitStatus().getRemainingHits()
                 + " API hits remaining");

        for(User u : currentFriends)
        {
            System.out.println(u.getScreenName());
        }
        
        for(User u : currentFriends)
        {
            System.out.println(u.getId());
        }
        System.out.println("You have " + myTwitter.rateLimitStatus().getRemainingHits()
                 + " API hits remaining");
    }

}
