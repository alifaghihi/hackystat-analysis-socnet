/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.sensors.twitterclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import twitter4j.TwitterException;

/**
 *
 * @author Rachel Shadoan
 */
public class TwitterClient
{

     public static final String TWITTER_USERNAME_KEY = "socnet.twitter.username";
     public static final String TWITTER_PASSWORD_KEY = "socnet.twitter.password";
     public static final String EMAIL_KEY = "socnet.twitter.email";
     public static final String SERVER_PASSWORD_KEY = "socnet.twitter.server.password";
     public static final String TWITTER_API_LIMIT_KEY ="socnet.twitter.apilimit";
     public static final String SERVER_ADDRESS_KEY = "socnet.twitter.serveraddress";
    
    
    public static void printErrorMsg(String propFile)
    {
        System.err.println("Error!  Cannot start the app without a propeties file!");
        System.err.println("The file must be located at " + propFile);
        System.err.println("It must contain the following keys: ");
        System.err.println(TWITTER_USERNAME_KEY);
        System.err.println(TWITTER_PASSWORD_KEY);
        System.err.println(TWITTER_API_LIMIT_KEY);
        System.err.println(SERVER_ADDRESS_KEY);
        System.err.println(EMAIL_KEY);
        System.err.println(SERVER_PASSWORD_KEY);
        System.err.println("See SocNet documentation");
        System.exit(-1);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException, JAXBException, Exception
    {

        //create the twitterPoller object
        String userHome = System.getProperty("user.home");
        String propFile = userHome + "/.hackystat/socnet/socnet.twitter.properties";

        if (!new File(propFile).exists())
        {
            printErrorMsg(propFile);
        }

        Properties properties = new Properties();
        properties.load(new FileReader(propFile));

        String username = properties.getProperty(TWITTER_USERNAME_KEY);
        String password = properties.getProperty(TWITTER_PASSWORD_KEY);
        String apistr = properties.getProperty(TWITTER_API_LIMIT_KEY);
        String serveraddress = properties.getProperty(SERVER_ADDRESS_KEY);
        String email = properties.getProperty(EMAIL_KEY);
        String server_password = properties.getProperty(SERVER_PASSWORD_KEY);
        
        if(username == null || password == null || apistr == null || serveraddress == null)
        {
            printErrorMsg(propFile);
        }
        
        int apiCallsPerHour = Integer.parseInt(apistr);
        TwitterPoller twitterPoller = new TwitterPoller(username, password, email,
                                                        server_password,
                                                        apiCallsPerHour, 
                                                        serveraddress);

        //begin polling!
        while (true)
        {
            try
            {
                //update the database with information from twitter, if anything
                //is new and different
                twitterPoller.update();

                //wait for an hour before doing it again
                System.out.println("Sleeping for an hour!");
                Thread.sleep(60*60*1000);
                System.out.println("Awake!");
                
            }
            catch (HitApiCallLimitException ex)
            {
                //if we've hit the API limit, print out the error message
                System.out.println(ex.getMessage());
                System.out.println("Sleeping cause we hit our API limit!");

                //and then go to sleep.
                Thread.sleep(60*60*1000);
                System.out.println("Awake!");
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                System.out.println("Unknown problem.  Sleeping for 15 minutes" +
                        "before trying again.");
                Thread.sleep(15*60*1000);
            }
        }
    }
}
