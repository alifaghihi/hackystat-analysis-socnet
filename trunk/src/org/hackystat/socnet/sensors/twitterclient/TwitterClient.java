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

    public static void printErrorMsg(String propFile)
    {
        System.err.println("Error!  Cannot start the app without a propeties file!");
        System.err.println("The file must be located at " + propFile);
        System.err.println("It must contain the following keys: ");
        System.err.println("socnet.twitter.username");
        System.err.println("socnet.twitter.password");
        System.err.println("socnet.twitter.apilimit");
        System.err.println("See SocNet documentation");
        System.exit(-1);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException, JAXBException
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

        String username = properties.getProperty("socnet.twitter.username");
        String password = properties.getProperty("socnet.twitter.password");
        String apistr = properties.getProperty("socnet.twitter.apilimit");
        
        if(username == null || password == null || apistr == null)
        {
            printErrorMsg(propFile);
        }
        
        int apiCallsPerHour = Integer.parseInt(apistr);
        TwitterPoller twitterPoller = new TwitterPoller(username, password, apiCallsPerHour);

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
