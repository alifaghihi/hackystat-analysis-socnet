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

        ArrayList<User> currentFollowers = (ArrayList<User>) myTwitter.getFollowers();
        
         System.out.println("You have " + myTwitter.rateLimitStatus().getRemainingHits()
                 + " API hits remaining");

         System.out.println("People I follow");
         
        for(User u : currentFriends)
        {
            System.out.println(u.getScreenName());
        }

        System.out.println("People who follow me");
        
        for(User u : currentFollowers)
        {
            System.out.println(u.getScreenName());
        }
        
        if(!currentFriends.equals(currentFollowers))
        {
            System.out.println("The friends and followers lists are not the same");
            
        }
        System.out.println("You have " + myTwitter.rateLimitStatus().getRemainingHits()
                 + " API hits remaining");
    }

}
