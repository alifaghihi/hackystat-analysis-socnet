/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.mailer;

import org.hackystat.socnet.server.test.SocNetRestApiHelper;

/**
 * 
 * @author Rachel Shadoan
 */
public class TestMailer {

  public static void main(String[] args) throws Exception {
    SocNetRestApiHelper.setupServer();
    Mailer mailer = Mailer.getInstance();
    System.out.println("I got the mailer instance.");
    boolean success = mailer.send("rachel.shadoan@gmail.com", "rachel.shadoan@gmail.com",
        "Testing Mailer", "This is just a test to see if this works.");

    if (success) {
      System.out.println("Supposedly the email was sent?");
    }
    System.exit(0);
  }
}
