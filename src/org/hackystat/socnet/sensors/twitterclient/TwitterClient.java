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
 * This class launches the Twitter client. It also stores the keys required for the twitter client
 * properties file.
 * 
 * @author Rachel Shadoan
 */
public class TwitterClient {

  /** Constant for the twitter username property */
  public static final String TWITTER_USERNAME_KEY = "socnet.twitter.username";

  /** Constant for the twitter password propert */
  public static final String TWITTER_PASSWORD_KEY = "socnet.twitter.password";

  /** Constant for the email of the socnet server admin property */
  public static final String EMAIL_KEY = "socnet.twitter.email";

  /** Constant for the socnet server admin password property */
  public static final String SERVER_PASSWORD_KEY = "socnet.twitter.server.password";

  /** Constant for the Twitter API call limit property */
  public static final String TWITTER_API_LIMIT_KEY = "socnet.twitter.apilimit";

  /** Constant for the socnet server address property */
  public static final String SERVER_ADDRESS_KEY = "socnet.twitter.serveraddress";

  /** Constant for storing the location of the twitter client properties file */
  public static final String PROPERTIES_FILE_LOCATION = "/.hackystat/socnet/socnet.twitter.properties";

  /**
   * This prints an error message stating what keys must be located in the Twitter client properties
   * file, and where the twitter client properties file must be located. Afterwards, it exists with
   * an error.
   * 
   * @param propFile - the correct location of the properties file, which, for the Twitter client,
   * is (from the hackystat hidden directory) <user home
   * directory>/.hackystat/socnet/socnet.twitter.properties
   */
  public static void printErrorMsg(String propFile) {
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

  public static void main(String[] args) throws FileNotFoundException, IOException,
      TwitterException, InterruptedException, JAXBException, Exception {

    // get the location of the user home directory
    String userHome = System.getProperty("user.home");

    // append the appropriate location of the Twitter client properties file
    String propFile = userHome + PROPERTIES_FILE_LOCATION;

    // if the properties file doesn't exist, print an error message and
    // exit with an error (printErrorMsg exits with an error)
    if (!new File(propFile).exists()) {
      printErrorMsg(propFile);
    }

    // if we made it here, we're in good shape, so load up the properties
    // file
    Properties properties = new Properties();
    properties.load(new FileReader(propFile));

    // load in all of the properties
    String username = properties.getProperty(TWITTER_USERNAME_KEY);
    String password = properties.getProperty(TWITTER_PASSWORD_KEY);
    String apistr = properties.getProperty(TWITTER_API_LIMIT_KEY);
    String serveraddress = properties.getProperty(SERVER_ADDRESS_KEY);
    String email = properties.getProperty(EMAIL_KEY);
    String server_password = properties.getProperty(SERVER_PASSWORD_KEY);

    // if any of the strings loaded from the properties file ended up as
    // null, then it wasn't found or something else has gone wrong. In that
    // case, print an error message and exit with an error.
    if (username == null || password == null || apistr == null || serveraddress == null) {
      printErrorMsg(propFile);
    }

    // turn the number of calls per hour into an int
    int apiCallsPerHour = Integer.parseInt(apistr);

    // make the twitterPoller
    TwitterPoller twitterPoller = new TwitterPoller(username, password, email, server_password,
        apiCallsPerHour, serveraddress);

    // begin polling!
    while (true) {
      try {
        // update the database with information from twitter, if
        // anything
        // is new and different
        twitterPoller.update();

        // wait for an hour before doing it again
        System.out.println("Sleeping for an hour!");
        Thread.sleep(60 * 60 * 1000);
        System.out.println("Awake!");

      }
      catch (HitApiCallLimitException ex) {
        // if we've hit the API limit, print out the error message
        System.out.println(ex.getMessage());
        System.out.println("Sleeping cause we hit our API limit!");

        // and then go to sleep for an hour
        Thread.sleep(60 * 60 * 1000);
        System.out.println("Awake!");
      }
      catch (Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        System.out.println("Unknown problem.  Sleeping for 15 minutes" + "before trying again.");
        Thread.sleep(15 * 60 * 1000);
      }
    }
  }
}
