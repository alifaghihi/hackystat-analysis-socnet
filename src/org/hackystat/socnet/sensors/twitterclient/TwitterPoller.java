/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.twitterclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hackystat.socnet.server.client.SocNetClient;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author Rachel Shadoan
 */
public class TwitterPoller {
    
    public enum TwitterRelationships implements RelationshipType{IS_FOLLOWED_BY, IS_FOLLOWING,
        MENTIONED};

    private final String clientUsername;
    
    private final String clientPassword;
    
    private int allowableAPICallsPerHour;
    
    private int apiCallsMadeThisHour;
    
    private Twitter twitter;
    
    private ArrayList<User> users;
    
    private HashMap<User, List<User>> usersToFollowers;
    
    private HashMap<User, List<User>> usersToPeopleTheyFollow;
    
    private SocNetClient socnetclient;
    
    public TwitterPoller(String propertiesFile) throws FileNotFoundException, IOException, TwitterException
    {
        BufferedReader br = new BufferedReader(new FileReader(propertiesFile));
        
        clientUsername = br.readLine();
        clientPassword = br.readLine();
        allowableAPICallsPerHour = Integer.parseInt(br.readLine());
        
        apiCallsMadeThisHour = 0;
        
        twitter = new Twitter(clientUsername, clientPassword);
        
        checkAPIcalls();
        
        usersToPeopleTheyFollow = new HashMap<User, List<User>>();
        usersToFollowers = new HashMap<User, List<User>>();
      
        socnetclient = new SocNetClient();
        
        ArrayList<String> twitterAcctsInDB = socnetclient.getNodes(
                IsARelationshipType.IS_TWITTER_ACCOUNT);
        
        for(String twitterScreenName : twitterAcctsInDB)
        {
            users.add(twitter.getUserDetail(twitterScreenName));
            
            checkAPIcalls();
        }
       

    }
        
           
        public synchronized void checkAPIcalls()
        {
            if(apiCallsMadeThisHour == (allowableAPICallsPerHour - 5))
                throw new HitApiCallLimitException(apiCallsMadeThisHour, 
                        allowableAPICallsPerHour - apiCallsMadeThisHour);
            
            else
                apiCallsMadeThisHour++;
            
        }
        
        public void addSocNetUsers() throws TwitterException
        {
            String userScreenName;
            ArrayList<User> currentUsers;
            
            currentUsers = (ArrayList<User>) twitter.getFollowers();
            
            //if this is not the initalization adding of the SocNet users and
            //the two lists are equal
            if(!isInitialization && users.equals(currentUsers))
            {
                //get the heck out of the method
                return;
            }
                
            else
            for(User newUser : currentUsers)
            {
                userScreenName = newUser.getScreenName();
            
                if(isInitialization)
                    socnetclient.addNode(userScreenName, 
                        IsARelationshipType.IS_TWITTER_ACCOUNT);
               
               
            }
            
        }
        
        public void addFollowersOfSocNetUser(boolean isInitialization) throws TwitterException
        {
            if(isInitialization)
            {
                users = twitter.getFollowers();
            }
        }
        
        public void addPeopleSocNetUserFollows(boolean isInitialization)
        {
            
        }
    }
    
    
    
    

