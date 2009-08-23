/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.hackystatclient;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author Rachel Shadoan
 */
public class ClientConfig {
  public static final String DEFAULT_FILENAME = System.getProperty("user.home")+
          "/.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties";
  public static final String SENSORBASE_SERVER_ADDRESS_KEY = "sensorshell.sensorbase.host";
  public static final String SENSORBASE_USER_KEY = "sensorshell.sensorbase.user";
  public static final String SENSORBASE_PASSWORD_KEY = "sensorshell.sensorbase.password";
  public static final String SOCNET_SERVER_ADDRESS_KEY = "socnet.host";
  public static final String PROJECT_NAMES_KEY = "socnet.project.names";
  public static final String PROJECT_STARTDATE_PREFIX = "socnet.project.start.";
  public static final String PROJECT_ENDDATE_PREFIX = "socnet.project.end.";
  public static final String SENSORBASE_HOST_KEY = "sensorshell.sensorbase.host";
  public static final String SOCNET_HOST_KEY = "socnet.server.host";
  public static final String TELEMETRY_HOST_KEY = "telemetry.sensorbase.host";
  
  public static final String DEFAULT_SENSORBASE_HOST = "http://dasha.ics.hawaii.edu:9876/sensorbase/";
  public static final String DEFAULT_SOCNET_HOST = "http://localhost:9999/socnet/";
  public static final String DEFAULT_TELEMETRY_HOST = 
          "http://dasha.ics.hawaii.edu:9878/telemetry/";
  
  public Properties hackystatClientProperties;
  public Properties sensorshellProperties;
  public DateFormat df;

  public ClientConfig() throws IOException {
    this(DEFAULT_FILENAME);
  }

  public ClientConfig(String hackystatClientFilename) throws IOException {
    hackystatClientProperties = new Properties();
    hackystatClientProperties.load(new FileReader(hackystatClientFilename));
    sensorshellProperties = new Properties();
    sensorshellProperties.load(new FileReader(System.getProperty("user.home")+
            "/.hackystat/sensorshell/sensorshell.properties"));
    df = DateFormat.getInstance();
    
  }

  public void save() throws IOException
  {
      save(DEFAULT_FILENAME);
  }
  
  public void save(String filename) throws IOException {
    hackystatClientProperties.store(new FileWriter(filename), null);
  } 

  public String getSensorbaseUsername() {
    return sensorshellProperties.getProperty(SENSORBASE_USER_KEY);
  }
   
  public String getSensorbasePassword() {
    return sensorshellProperties.getProperty(SENSORBASE_PASSWORD_KEY);
  }
  
  public void setSensorbasePassword(String password)
  {
      sensorshellProperties.setProperty(SENSORBASE_PASSWORD_KEY, password);
  }
  
  public List<String> getProjectNames() {
    String namesStr = hackystatClientProperties.getProperty(PROJECT_NAMES_KEY);

    ArrayList<String> nameList = new ArrayList<String>();

    if (namesStr == null || namesStr.isEmpty()) {
      return nameList;
    }

    String[] names = namesStr.split(",");

    for (String name : names) {
      nameList.add(name);
    }

    return nameList;
  }

  public void setProjectNames(List<String> projectNames) {

      if(projectNames.isEmpty())
      {
          hackystatClientProperties.clear();
          return;
      }
      
      String val = projectNames.get(0);

    for (int i = 1; i < projectNames.size(); i++) {
      val += "," + projectNames.get(i);
    }

    hackystatClientProperties.setProperty(PROJECT_NAMES_KEY, val);
  }

  public Date getStartDate(String projectname) throws ParseException {
    String d = hackystatClientProperties.getProperty(PROJECT_STARTDATE_PREFIX + projectname);

    return df.parse(d);
  }

  public void setStartDate(String projectname, Date d) {
    hackystatClientProperties.setProperty(PROJECT_STARTDATE_PREFIX + projectname, df.format(d));
  }

  public Date getEndDate(String projectname) throws ParseException {
    String d = hackystatClientProperties.getProperty(PROJECT_ENDDATE_PREFIX + projectname);

    return df.parse(d);
  }

  public void setEndDate(String projectname, Date d) {
    hackystatClientProperties.setProperty(PROJECT_ENDDATE_PREFIX + projectname, df.format(d));
  }

  public String getSensorbaseHost()
  {
      return sensorshellProperties.getProperty(SENSORBASE_HOST_KEY, DEFAULT_SENSORBASE_HOST);
  }
  
  public String getSocnetHost()
  {
      return hackystatClientProperties.getProperty(SOCNET_HOST_KEY, DEFAULT_SOCNET_HOST);
  }
  
  public String getTelemetryHost()
  {
      return hackystatClientProperties.getProperty(TELEMETRY_HOST_KEY, DEFAULT_TELEMETRY_HOST);
  }
  
  
}
