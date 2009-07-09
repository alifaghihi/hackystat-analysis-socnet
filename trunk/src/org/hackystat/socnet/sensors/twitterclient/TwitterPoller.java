/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.sensors.twitterclient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.hackystat.socnet.sensors.twitterclient.TwitterSocNetClient;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author Rachel Shadoan
 */
public class TwitterPoller
{

    public enum TwitterRelationships implements RelationshipType
    {

        IS_FOLLOWED_BY, IS_FOLLOWING,
        MENTIONED
    };
    
    private final String clientUsername;
    private final String clientPassword;
    private final int allowableAPICallsPerHour;
    private int apiCallsRemainingThisHour;
    private Twitter twitter;
    private HashSet<String> usersInDB;
    private HashMap<String, HashSet<String>> usersToFollowers;
    private HashMap<String, HashSet<String>> usersToPeopleTheyFollow;
    private TwitterSocNetClient socnetclient;

    public TwitterPoller(String propertiesFile) throws FileNotFoundException, IOException, TwitterException, HitApiCallLimitException
    {
        //create a buffered reader to read the password, username, and 
        //api limit from the config file
        BufferedReader br = new BufferedReader(new FileReader(propertiesFile));

        //read in the username of the TwitterClient's account (neccessary to call
        //authenticated methods, get friends lists, etc.
        clientUsername = br.readLine();
        
        //read in the password of the TwitterClient's accound
        clientPassword = br.readLine();
        
        //read in and parse the number of api calls we are allowed to make per 
        //hour. This is read from the config file instead of stored as a 
        //constant because it will eventually change.
        allowableAPICallsPerHour = Integer.parseInt(br.readLine());

        //create a Twitter API wrapper with the authentication information 
        //read from the configuration file
        twitter = new Twitter(clientUsername, clientPassword);

        //check the number of API calls remaining
        checkAPIcalls();

        //create hashes to map usernames to their list of followers and people
        //they are following
        usersToPeopleTheyFollow = new HashMap<String, HashSet<String>>();
        usersToFollowers = new HashMap<String, HashSet<String>>();

        //create a new socnet client
        socnetclient = new TwitterSocNetClient();

        //get all the Twitter Account nodes in the database
        ArrayList<String> twitterAcctsInDB = socnetclient.getNodes(
                IsARelationshipType.IS_TWITTER_ACCOUNT);

        //add all of the usernames of the twitter accounts in the database
        //to a hashset (n time)
        for (String twitterScreenName : twitterAcctsInDB)
        {
            usersInDB.add(twitterScreenName);
        }

        //get the list of current users (people who follow the HackystatSocnet
        //twitter account)
        ArrayList<User> currentUsers = (ArrayList<User>) twitter.getFollowers();
        
        //that was an API call so we need to make sure we're still under our limit.
        checkAPIcalls();
        
        //create two lists of users--one of new users who are not yet in the 
        //database, and one of users who have de-friended the HackystatSocnet
        //account and are assumed to have stopped being Twitter users and therefore
        //need to have their relationships in the database terminated
        ArrayList<String> toBeDataBased = new ArrayList<String>();
        ArrayList<String> toHaveRelationshipsTerminated = new ArrayList<String>();
        
        //create a HashSet to store the current users in a moment we will use 
        //this to see if any of our followers have been deleted
        HashSet<String> currentUsersHash = new HashSet<String>();
        
        String username;
        
        //for each user who follows the HackystatSocNet twitter account
        for (User follower : currentUsers)
        {
            //store their username, as we'll need it in a couple of places
            username = follower.getScreenName();
            
            //add the username to the current user hash
            currentUsersHash.add(username);
            
            //if the set of users already in the database does not contain this
            //user, we need to add it to the list of users to add to the database
            if (!usersInDB.contains(username))
            {
                toBeDataBased.add(username);
            }
        }

        //now we need to find out which users have been deleted, so we'll
        //need to iterate over the users already in the database
        Iterator it = usersInDB.iterator();

        List<User> userFollowers;
        List<User> usersTheyFollow;
        ArrayList<String> userFollowersInGraph;
        ArrayList<String> usersTheyFollowInGraph;
        
        HashSet<String> userFollowersInGraphHash = new HashSet<String>();
        HashSet<String> userFollowersHash = new HashSet<String>();
        HashSet<String> usersTheyFollowHash = new HashSet<String>();
        HashSet<String> usersTheyFollowInGraphHash = new HashSet<String>();
       
             
        //for each user in the database
        while(it.hasNext())
        {
            
            username = (String) it.next();
            
            //get the user's followers that are currently listed in the database
            userFollowersInGraph = socnetclient.getNodes(username, 
                IsARelationshipType.IS_TWITTER_ACCOUNT, 
                TwitterRelationships.IS_FOLLOWED_BY);
            
            //get the people that the user is following that are currently 
            //listed in the database
            usersTheyFollowInGraph = socnetclient.getNodes(username, 
                IsARelationshipType.IS_TWITTER_ACCOUNT, 
                TwitterRelationships.IS_FOLLOWING);
                        
            //get the followers as currently listed by twitter
            userFollowers = twitter.getFollowers(username);
            
            checkAPIcalls();
            
            //get the people they follow as currently listed by twitter
            usersTheyFollow = twitter.getFriends(username);
            
            checkAPIcalls();
            
            //add all of the followers of the user listed in the database to
            //a hashset
            for(String userFollowerInGraph : userFollowersInGraph)
            {
                userFollowersInGraphHash.add(userFollowerInGraph);
            }
            
            //add all of the people that the user follows listed in the database
            //to a hashet
            for(String userTheyFollowInGraph : usersTheyFollowInGraph)
            {
                usersTheyFollowInGraphHash.add(userTheyFollowInGraph);
            }
            
            //for each person that twitter lists this person as following
            for(User userFollower: userFollowers)
            {
                if(userFollowersInGraphHash.contains(userFollower.getScreenName()));
                
            }
            
            //check to make sure we haven't hit our API limit
            checkAPIcalls();
            
            //if the username is in the set of users currently in the database
            //but not in the list of current users, add it to the list of
            //users whose relationships need to be terminated.
            if(!currentUsersHash.contains(username))
                toHaveRelationshipsTerminated.add(username);
            
            for(User userFollower: userFollowers)
            {
                
            }
            
            
        }
        
        //terminate the relationships of those users who have de-followed the
        //hackystatsocnet account
        terminateRelationships(toHaveRelationshipsTerminated);
        
        //add the new users to the database
        addUsers(toBeDataBased);
        
        

    }
    
   
    
    public void updateFollowers(String username) throws TwitterException
    {
        List<User> currentFollowers = twitter.getFollowers(username);
        
        
    }
    
    
    public void terminateRelationships(ArrayList<String> usersToHaveRelationshipsTerminated)
    {
        ArrayList<Integer> relationshipIDs;
        
        //for each user
        for(String user: usersToHaveRelationshipsTerminated)
        {
            //get all of the relationships ids of relationships connected to the 
            //user's twitter account node
            relationshipIDs = socnetclient.getRelationships(user, 
                    IsARelationshipType.IS_TWITTER_ACCOUNT);
            
            //for each relationship
            for(Integer relationship : relationshipIDs)
            {
                //end it (this puts an end timestamp on the relationship in the
                //DB, it doesn't actually delete the relationship)
                socnetclient.endRelationship(relationship);
            }
        }
    }
    
    public void addUsers(ArrayList<String> usersToAddToGraph)
    {
        //for each user
        for(String user: usersToAddToGraph)
        {
            //add them to the graph
            socnetclient.addNode(user, IsARelationshipType.IS_TWITTER_ACCOUNT);
            
            //add their username to the cache hash of those users already in the
            //graph
            usersInDB.add(user);
         
        }
    }
            
    
    
    public synchronized void checkAPIcalls() throws HitApiCallLimitException, TwitterException
    {
        //if we're within five API calls of the API limit
        if (apiCallsRemainingThisHour == 5)
        {
            //throw an exception
            throw new HitApiCallLimitException(allowableAPICallsPerHour - 
                    apiCallsRemainingThisHour, apiCallsRemainingThisHour);
        }
        
        else
        {
            //update the number of calls made this hour
            apiCallsRemainingThisHour = twitter.rateLimitStatus().getRemainingHits();
        }

    }
}
    
    
    
    

