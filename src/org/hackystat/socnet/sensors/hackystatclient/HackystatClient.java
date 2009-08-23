/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.hackystatclient;

import java.io.IOException;
import java.util.List;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.socnet.server.client.SocNetClient;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;

/**
 * This is the launcher for the Hackystat client, which polls the Sensorbase for
 * Telemetry information and then sends it to the Socnet Server.
 * 
 * @author Rachel Shadoan
 */
public class HackystatClient {

    
  public static final String SERVER_ADDRESS_KEY = "sensorshell.sensorbase.host";
  public static final String USER_KEY = "sensorshell.sensorbase.user";
  public static final String PASSWORD_KEY = "sensorshell.sensorbase.password";

  public static void printErrorMsg(String propFile) {
    System.err.println("Error!  Cannot start the app without a propeties file!");
    System.err.println("The file must be located at " + propFile);
    System.err.println("It must contain the following keys: ");
    System.err.println(USER_KEY);
    System.err.println(PASSWORD_KEY);
    System.err.println(SERVER_ADDRESS_KEY);
    System.err.println("See SocNet documentation");
    System.exit(-1);
  }

  public static void main(String[] args) throws IOException, SensorBaseClientException,
      TelemetryClientException, Exception {

    while (true) {
      ClientConfig conf = new ClientConfig();
      String sensorbaseUsername = conf.getSensorbaseUsername();
      String sensorbasePass = conf.getSensorbasePassword();

      System.out.println(sensorbasePass);

      SocNetClient snc = new SocNetClient(conf.getSocnetHost(), sensorbaseUsername, sensorbasePass);

      TelemetryClient tc = new TelemetryClient(conf.getTelemetryHost(), sensorbaseUsername,
          sensorbasePass);

      SensorBaseClient sbc = new SensorBaseClient(conf.getSensorbaseHost(), sensorbaseUsername,
          sensorbasePass);

      HackystatPoller poller = new HackystatPoller(sbc, snc, tc, sensorbaseUsername);

      List<String> projects = conf.getProjectNames();

      for (String project : projects) {
        poller.addProject(project, conf.getStartDate(project), conf.getEndDate(project));
      }

      try {

        poller.update();

        // wait for an hour before doing it again
        System.out.println("Sleeping for an day!");
        Thread.sleep(1000 * 60 * 60 * 24);
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
