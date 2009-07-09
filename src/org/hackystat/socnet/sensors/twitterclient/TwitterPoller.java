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
public class TwitterPoller
{

    public enum TwitterRelationships implements RelationshipType
    {

        IS_FOLLOWED_BY, IS_FOLLOWING,
        MENTIONED
    };
    private final String clientUsername;
    private final String clientPassword;
    private int allowableAPICallsPerHour;
    private int apiCallsMadeThisHour;
    private Twitter twitter;
    private ArrayList<User> users;
    private HashSet<String> usersInDB;
    private HashMap<String, List<User>> usersToFollowers;
    private HashMap<String, List<User>> usersToPeopleTheyFollow;
    private SocNetClient socnetclient;

    public TwitterPoller(String propertiesFile) throws FileNotFoundException, IOException, TwitterException, HitApiCallLimitException
    {
        BufferedReader br = new BufferedReader(new FileReader(propertiesFile));

        clientUsername = br.readLine();
        clientPassword = br.readLine();
        allowableAPICallsPerHour = Integer.parseInt(br.readLine());

        twitter = new Twitter(clientUsername, clientPassword);

        checkAPIcalls();

        usersToPeopleTheyFollow = new HashMap<String, List<User>>();
        usersToFollowers = new HashMap<String, List<User>>();

        socnetclient = new SocNetClient();

        ArrayList<String> twitterAcctsInDB = socnetclient.getNodes(
                IsARelationshipType.IS_TWITTER_ACCOUNT);

        for (String twitterScreenName : twitterAcctsInDB)
        {
            usersInDB.add(twitterScreenName);
        }

        ArrayList<User> currentUsers = (ArrayList<User>) twitter.getFollowers();
        ArrayList<String> toBeDataBased = new ArrayList<String>();
        ArrayList<String> toHaveRelationshipsTerminated = new ArrayList<String>();
        //in a moment we will use this to see if any of our followers have been
        //deleted
        HashSet<String> currentUsersHash = new HashSet<String>();
        String username;
        for (User follower : currentUsers)
        {
            username = follower.getScreenName();
            currentUsersHash.add(username);
            if (!usersInDB.contains(username))
            {
                toBeDataBased.add(username);
            }
        }

        Iterator it = usersInDB.iterator();

        while(it.hasNext())
        {
            username = (String) it.next();
            if(!currentUsersHash.contains(username))
                toHaveRelationshipsTerminated.add(username);
        }


    }

    public synchronized void checkAPIcalls() throws HitApiCallLimitException, TwitterException
    {
        if (apiCallsMadeThisHour == (allowableAPICallsPerHour - 5))
        {
            throw new HitApiCallLimitException(apiCallsMadeThisHour,
                    allowableAPICallsPerHour - apiCallsMadeThisHour);
        }
        else
        {
            apiCallsMadeThisHour = twitter.rateLimitStatus().getRemainingHits();
        }

    }
}
    
    
    
    

