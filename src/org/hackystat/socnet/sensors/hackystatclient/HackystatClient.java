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
    
      /*
       String userHome = System.getProperty("user.home");
    System.out.println(userHome);
    String propFile = userHome + "/.hackystat/sensorshell/sensorshell.properties";
    String telPropFile = userHome + "/.hackystat/telemetry/telemetry.properties";
    System.out.println(telPropFile);
    Properties properties = new Properties();
    properties.load(new FileReader(propFile));

    Properties telProperties = new Properties();
    properties.load(new FileReader(telPropFile));

    String user = properties.getProperty(USER_KEY);
    String password = properties.getProperty(PASSWORD_KEY);
    String serveraddress = properties.getProperty(SERVER_ADDRESS_KEY);

    String telHost = telProperties.getProperty("telemetry.sensorbase.host");
    System.out.println(telHost);
    String telDPD = telProperties.getProperty("telemetry.dailyprojectdata.host");
    String telHostName = telProperties.getProperty("telemetry.hostname ");

    if (user == null || password == null || serveraddress == null) {
      printErrorMsg(propFile);
    }

    TelemetryClient tc = new TelemetryClient("http://dasha.ics.hawaii.edu:9878/telemetry/", user,
        password);

    TelemetryChartData tcd = tc.getChart("Build", user, "Default", "Day", Tstamp
        .makeTimestamp("2009-05-30T23:59:59.999-10:00"), Tstamp.makeTimestamp(), "*,*,*,false");

    System.out.println(tcd.getTelemetryStream().size());*/
    
    while(true)
    {
        ClientConfig conf = new ClientConfig();
        String sensorbaseUsername = conf.getSensorbaseUsername();
        String sensorbasePass = conf.getSensorbasePassword();

        System.out.println(sensorbasePass);
        
        SocNetClient snc = new SocNetClient(conf.getSocnetHost(), 
                sensorbaseUsername, sensorbasePass);
        
        TelemetryClient tc = new TelemetryClient(conf.getTelemetryHost(), 
                sensorbaseUsername, sensorbasePass);
        
        
        SensorBaseClient sbc = new SensorBaseClient(conf.getSensorbaseHost(), 
                sensorbaseUsername, sensorbasePass);
                      
       HackystatPoller poller = new HackystatPoller(sbc, snc, tc, sensorbaseUsername);
       
       List<String> projects = conf.getProjectNames();
       
       for(String project : projects)
       {
           poller.addProject(project, conf.getStartDate(project), 
                   conf.getEndDate(project));
       }
       
       poller.update();
       
       Thread.sleep(1000* 60 * 60 * 24);
    }
    
    
  }

}
