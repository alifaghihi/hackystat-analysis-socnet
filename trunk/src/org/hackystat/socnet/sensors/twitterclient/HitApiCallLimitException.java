/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.twitterclient;

/**
 * This Exception class sets a message informing the user of how many API calls were made during the
 * hour, and how many they are still allowed to make before the API limit resets.
 * 
 * For instance, in Twitter's case, our application can make 200 API calls per hour. If the
 * exception is thrown before the limit is hit, such as at 195 calls during the hour, then the
 * exception would say that 195 calls were made and 5 calls remain.
 * 
 * 
 * @author Rachel Shadoan
 */
class HitApiCallLimitException extends Exception {

  /** This is a necessary field for serializable */
  public static final long serialVersionUID = 1l;

  /**
   * This constructor adds a message to the exception informing the user of how many API calls were
   * made in this hour and how many API calls can still be made.
   * 
   * @param callsMade - the number of calls made in this hour
   * @param callsRemaining - the allowable number of calls remaining this hour
   */
  public HitApiCallLimitException(int callsMade, int callsRemaining) {
    // pass the message to the super constructor
    super("You have made " + callsMade + " API calls this hour. You have " + callsRemaining
        + " calls remaining.");
  }

}
