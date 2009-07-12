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
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
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
        IS_FOLLOWED_BY, IS_FOLLOWING, MENTIONED
    };
    
    /**The username of the Twitter account for this client*/
    private final String clientUsername;
    
    /**The password for the Twitter account for this client*/
    private final String clientPassword;
    
    /**The total number of API calls allowed per hour*/
    private final int allowableAPICallsPerHour;
    
    /**The number of API calls we have remaining to make this hour*/
    private int apiCallsRemainingThisHour;
    
    /**The Twitter API wrapper object*/
    private Twitter twitter;
    
    /**A cache of the followers of each user*/
    private HashMap<String, HashSet<String>> usersToFollowers;
    
    /**A cache of the friends (people each user follows*/
    private HashMap<String, HashSet<String>> usersToPeopleTheyFollow;
    
    /**A client for communicating with the SocNet server*/
    private TwitterSocNetClient socnetclient;
    
    
    public static final int USERS_TO_ADD = 0;
    public static final int USERS_TO_DELETE = 1;

    /**
     * This creates a TwitterPoller object with the authentication information
     * read in from the provided configuration file.
     * 
     * @param propertiesFile - the absolute path of the properties file holding 
     * the 
     * @throws java.io.FileNotFoundException 
     * @throws java.io.IOException
     * @throws twitter4j.TwitterException 
     * @throws org.hackystat.socnet.sensors.twitterclient.HitApiCallLimitException
     */
    public TwitterPoller(String username, String password, int apiCallsPerHour) throws FileNotFoundException, IOException, TwitterException, JAXBException
    {
        //create a buffered reader to read the password, username, and 
        //api limit from the config file
        //BufferedReader br = new BufferedReader(new FileReader(propertiesFile));
        clientUsername = username;
        //read in the username of the TwitterClient's account (neccessary to call
        //authenticated methods, get friends lists, etc.
        //clientUsername = br.readLine();
        
        //read in the password of the TwitterClient's accound
       // clientPassword = br.readLine();
        clientPassword = password;
        //read in and parse the number of api calls we are allowed to make per 
        //hour. This is read from the config file instead of stored as a 
        //constant because it will eventually change.
        //allowableAPICallsPerHour = Integer.parseInt(br.readLine());
        allowableAPICallsPerHour = apiCallsPerHour;
        //create a Twitter API wrapper with the authentication information 
        //read from the configuration file
        twitter = new Twitter(clientUsername, clientPassword);

        //create hashes to map usernames to their list of followers and people
        //they are following
        usersToPeopleTheyFollow = new HashMap<String, HashSet<String>>();
        usersToFollowers = new HashMap<String, HashSet<String>>();

        //create a new socnet client
        socnetclient = new TwitterSocNetClient();
            
            
    }

    /**
     * This method updates the database and the local cache 
     * with changes posted by twitter.
     * 
     * @throws twitter4j.TwitterException
     * @throws org.hackystat.socnet.sensors.twitterclient.HitApiCallLimitException
     */
    public void update() throws TwitterException, HitApiCallLimitException, JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException
    {
          //update the followers and friends of the twitter client account
        updateRelationships(clientUsername);
        
        Set<String> users = usersToFollowers.keySet();
        Iterator it = users.iterator();
        
        //now update the relationships for each of the users that follow the
        //twitter client account
        while(it.hasNext())
        {
            updateRelationships((String) it.next());
        }
    }
    
    
    /**
     * This method gets all of the current followers and friends of a user from 
     * Twitter, compares it to the cache of followers and friends of that user,
     * and updates the database and cache accordingly. If a user is in the cache
     * but not in the list of current friends or followers, the relationship in
     * the database between the user passed as a parameter and the user missing
     * from the current list. The user missing from the current friends or 
     * followers is then also deleted from the cache.
     * 
     * @param username
     * @throws twitter4j.TwitterException
     * @throws org.hackystat.socnet.sensors.twitterclient.HitApiCallLimitException
     */
    private void updateRelationships(String username) throws TwitterException, HitApiCallLimitException, JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException
    { 
        //declare lists for storing the lists of friends and followers that
        //we'll pull from twitter
        ArrayList<User> currentFollowers;
        ArrayList<User> currentFriends;
        
        //declare lists of lists for the followers we need to add and delete and
        //the friends we'll need to add and delete
        ArrayList<ArrayList<String>> followersToAddAndDelete;
        ArrayList<ArrayList<String>> friendsToAddAndDelete;
        
        //get the followers for this user that are listed in the DB
        HashSet<String> followersInDBHash = getConnectedUsers(username, 
                IsARelationshipType.IS_TWITTER_ACCOUNT, 
                TwitterRelationships.IS_FOLLOWED_BY);
        
        //get the friends (people this user follows) that are listed in the database
        HashSet<String> friendsInDBHash = getConnectedUsers(username, 
                IsARelationshipType.IS_TWITTER_ACCOUNT, 
                TwitterRelationships.IS_FOLLOWING);
        
        //get the list of current followers from twitter
        currentFollowers = (ArrayList<User>) twitter.getFollowers(username);
        
        //check to make sure we haven't exceeded our API calls
        checkAPIcalls();
        
        //get the list of current friends from twitter
        currentFriends = (ArrayList<User>) twitter.getFriends(username);
        
        //checks to make sure we haven't exceeded our API calls.
        checkAPIcalls();
        
        //find which followers need to be added to the database and which need
        //to be deleted
        followersToAddAndDelete = getUsersToAddAndDelete(followersInDBHash,
                currentFollowers);
        
        //find which friends need to be added to the database and which need
        //to be deleted
        friendsToAddAndDelete = getUsersToAddAndDelete(friendsInDBHash,
                 currentFriends);
    
        //add and deleted the appropriate followers
        addAndDeleteUsers(username, IsARelationshipType.IS_TWITTER_ACCOUNT,
                TwitterRelationships.IS_FOLLOWED_BY, 
                followersToAddAndDelete.get(USERS_TO_ADD),
                followersToAddAndDelete.get(USERS_TO_DELETE));
        
        //add and delete the appropriate friends
        addAndDeleteUsers(username, IsARelationshipType.IS_TWITTER_ACCOUNT,
                TwitterRelationships.IS_FOLLOWING, 
                friendsToAddAndDelete.get(USERS_TO_ADD),
                friendsToAddAndDelete.get(USERS_TO_DELETE));
        
    }
    
    
    /**
     * Note: This is the method that updates the cache.
     * 
     * This method adds the specified users to the database and the cache, and
     * deletes the specified users from the database and the cache.
     * 
     * @param startNodeName - the screenname of the user whose followers or friends
     * we are removing
     * @param startNodeType - the type of the start node (IS_TWITTER_ACCOUNT)
     * @param relationshipBetweenNodes - the relationship between the two users,
     * ie, whether the first user is followed by or is following the users we are
     * adding or deleting.
     * @param usersToAdd - a list of the users we are adding to the graph
     * @param usersToDelete - a list of the users we whose relationship to the
     * startnode in the database we are terminating, and whom we are deleting from
     * the cache
     */
    private void addAndDeleteUsers(String startNodeName, 
            IsARelationshipType startNodeType,
            TwitterRelationships relationshipBetweenNodes,
            ArrayList<String> usersToAdd, ArrayList<String> usersToDelete) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        //initialize the hashset that we'll cache the users in
        HashSet<String> usersInDB = new HashSet<String>();
        
        //if the users in the lists passed as parameters are followers of the
        //startnode
        if(relationshipBetweenNodes.equals(TwitterRelationships.IS_FOLLOWED_BY))
        {
            //check to see if we've already gotten the list of users following
            //our startnode user from the database. If so, retrieve it
            if(usersToFollowers.containsKey(startNodeName))
                usersInDB = usersToFollowers.get(startNodeName);
        }
        
        //if the users in the list passed as parameters are friends of (people
        //the user is following)
        else if(relationshipBetweenNodes.equals(TwitterRelationships.IS_FOLLOWING))
        {
            //check to see if we've already gotten the list of friends of our
            //startnode user from the database. If so, retrieve it
            if(usersToPeopleTheyFollow.containsKey(startNodeName))
                usersInDB = this.usersToPeopleTheyFollow.get(startNodeName);
        }
        
        //for each of the users in the list of users to add to the database and
        //cache
        for(String endNodeName : usersToAdd)
        {
            //add a relationship between the start node and the user we're adding
            System.out.println("Adding relationship: " +startNodeType.name() 
                                + ":" + startNodeName + " to " + startNodeType.name() + 
                                ":" + endNodeName + "of type " + relationshipBetweenNodes.name());
            
            socnetclient.addRelationshipTo(startNodeName, startNodeType, endNodeName, 
                    startNodeType, relationshipBetweenNodes);
            
            //add the user to the cache of the startnode
            usersInDB.add(endNodeName);
        }
        
        //for each of the users in the list of users to remove from the cache
        //and the database
        for(String endNodeName : usersToDelete)
        {
            //end the relationship between the user and the startnode user
            socnetclient.endRelationship(startNodeName, startNodeType, endNodeName, 
                    startNodeType, relationshipBetweenNodes);
            
            //if the user we're deleting is in our cache, we need to remove it as
            //well
            if(usersInDB.contains(endNodeName))
                usersInDB.remove(endNodeName);
        }
        
        //depending on what kind of relationship the lists passed were for,
        //update the appropriate map, mapping the startnode user to the cache
        //of friends or followers.
        if(relationshipBetweenNodes.equals(TwitterRelationships.IS_FOLLOWED_BY))
        {
            usersToFollowers.put(startNodeName, usersInDB);
        }
        
        else if(relationshipBetweenNodes.equals(TwitterRelationships.IS_FOLLOWING))
        {
            usersToPeopleTheyFollow.put(startNodeName, usersInDB);
        }
        
    }
    
    /**
     * This method returns a HashSet of all of the users in the database 
     * connected to the specified user by the specified relationship.
     * 
     * @param nodename - the screenname of the twitter user for whom we are 
     * retrieving friends or followers.
     * @param nodeType
     * @param relationship
     * @return a HashSet of the usernames of the users connected to the user 
     * passed as a parameter
     */
    private HashSet<String> getConnectedUsers(String nodename, IsARelationshipType 
            nodeType, TwitterRelationships relationship) throws IOException, JAXBException
    {
        HashSet<String> connectedUsersInDBHash = new HashSet<String>();
     
        ArrayList<String> connectedUsersInDB;
        
        //if the nodename is the username of the twitter client, we can just
        //retrieve all twitter account nodes in the database
        if(nodename.equals(clientUsername))
            connectedUsersInDB = socnetclient.getNodes(nodeType);
        
        //otherwise, get all the nodes in the database connected to the user
        //specified by the nodename parameter by the specified relationship
        else
            connectedUsersInDB = socnetclient.getNodes(nodename, nodeType, 
                relationship);
        
        //add each of the usernames to the HashSet
        for(String username: connectedUsersInDB)
        {
            connectedUsersInDBHash.add(username);
        }
        
        //return the HashSet
        return connectedUsersInDBHash;
    }
   
   /**
    * This method ascertains which users need to be deleted from the database
    * and which users need to be added, by comparing the cache to the list
    * returned from twitter. It returns an list of two lists: the first of those
    * being the users that need to be added to the database and the cache, the 
    * second being the users that need to be deleted from the database and the
    * cache
    * 
    * @param usernamesFromDB - the cache of users as a hashset
    * @param currentUsers - the list of current users from Twitter
    * @return
    * @throws org.hackystat.socnet.sensors.twitterclient.HitApiCallLimitException
    * @throws twitter4j.TwitterException
    */ 
    private ArrayList<ArrayList<String>> getUsersToAddAndDelete(HashSet<String>
            usernamesFromDB, ArrayList<User> currentUsers) throws HitApiCallLimitException, TwitterException
    {
        ArrayList<ArrayList<String>> newAndDeletedUsers = new 
                ArrayList<ArrayList<String>>();
        
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
            if (!usernamesFromDB.contains(username))
            {
                toBeDataBased.add(username);
            }
        }

        //now we need to find out which users have been deleted, so we'll
        //need to iterate over the users already in the database
        Iterator it = usernamesFromDB.iterator();

       //for each user in the database
        while(it.hasNext())
        {
            
            username = (String) it.next();
                     
          
            //if the username is in the set of users currently in the database
            //but not in the list of current users, add it to the list of
            //users whose relationships need to be terminated.
            if(!currentUsersHash.contains(username))
                toHaveRelationshipsTerminated.add(username);
               
        }
        
        newAndDeletedUsers.add(toBeDataBased);
        newAndDeletedUsers.add(toHaveRelationshipsTerminated);
        
        return newAndDeletedUsers;
        
        
    }

 
    /**
     * This method checks to make sure that the client has not exceeded the 
     * allowable number of API calls per hour. If we reach the allowable number
     * of API calls, it throws an exception.
     * 
     * @throws org.hackystat.socnet.sensors.twitterclient.HitApiCallLimitException
     * @throws twitter4j.TwitterException
     */
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
    
    
    
    

