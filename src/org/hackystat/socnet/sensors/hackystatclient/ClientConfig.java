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
 * This is a handy wrapper for Property functions used for configuring the 
 * Hackystat Client. It contains all the keys necessary for a hackystat client
 * properties file, and provides functionality for accessing and writing the
 * properties file.
 * 
 * @author Rachel Shadoan
 */
public class ClientConfig {
    
    /**The default file name and path for the Hackystat client properties file.*/
  public static final String DEFAULT_FILENAME = System.getProperty("user.home")
      + "/.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties";
  
  /**The key to access the sensorbase server */
  public static final String SENSORBASE_SERVER_ADDRESS_KEY = "sensorshell.sensorbase.host";
  
  /**The key to access the user's sensorbase user name, ie, the email address
   under which they registered*/
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
  public static final String DEFAULT_TELEMETRY_HOST = "http://dasha.ics.hawaii.edu:9878/telemetry/";

  public Properties hackystatClientProperties;
  public Properties sensorshellProperties;
  public DateFormat df;

  /**
   * Creates a ClientConfig object using the default filename.
   * 
   * @throws java.io.IOException
   */
  public ClientConfig() throws IOException {
    this(DEFAULT_FILENAME);
  }

  /**
   * Creates a ClientConfig object using the filename passed as a parameter.
   * 
   * @param hackystatClientFilename - the absolute path and name of the hackystat
   * client properties file, which, from the hackystat hidden directory, should
   * be /.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties
   * @throws java.io.IOException
   */
  public ClientConfig(String hackystatClientFilename) throws IOException {
    hackystatClientProperties = new Properties();
    FileReader fr = new FileReader(hackystatClientFilename);
    hackystatClientProperties.load(fr);
    fr.close();
    sensorshellProperties = new Properties();
    fr = new FileReader(System.getProperty("user.home")
        + "/.hackystat/sensorshell/sensorshell.properties");
    sensorshellProperties.load(fr);
    fr.close();
    df = DateFormat.getInstance();
    
  }

  /**
   * Saves the properties of the ClientConfig object to the default file location,
   * which, from the directory holding the hackystat hidden directory, should be 
   * /.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties
   * @throws java.io.IOException
   */
  public void save() throws IOException {
    save(DEFAULT_FILENAME);
  }

  /**
   * Saves the properties of the ClientConfig object to the location specified
   * by the filename passed as a parameter
   * @param filename - the absolute path and name of the desired location for 
   * the hackystat client properties file. Default is, from the folder containing
   * the hackystat hidden directory: /.hackystat/socnet/hackystatclient/socnet.hackystatclient.properties
   * 
   * @throws java.io.IOException
   */
  public void save(String filename) throws IOException {
      FileWriter fw = new FileWriter(filename);
    hackystatClientProperties.store(fw, null);
    fw.close();
  }

  /**
   * Returns the email address under which the user is registered with the 
   * sensorbase, as recorded in the sensorshell.properties file.
   * @return the user's email address that is registered with the sensorbase
   */
  public String getSensorbaseUsername() {
    return sensorshellProperties.getProperty(SENSORBASE_USER_KEY);
  }

  /**
   * Returns the password under which the user is registered with the 
   * sensorbase, as recorded in the sensorshell.properties file.
   * 
   * @return the user's password that is registered with the sensorbase
   */
  public String getSensorbasePassword() {
    return sensorshellProperties.getProperty(SENSORBASE_PASSWORD_KEY);
  }

  /**
   * Returns a list of the projects listed in the hackystat client properties
   * file. The client has permission to send Telemetry data from all of the 
   * projects in the returned list to the SocNet server.
   * 
   * @return a list of the user's projects that the Hackystat client has 
   * permission to send information about to the SocNet server
   */
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

  
  /**
   * Saves the list of projects to the configuration file. Saving a project name
   * in the hackystat client properties file gives permission for the Hackystat
   * client to send Telemetry data about that project from the Sensorbase to 
   * the Socnet server
   * 
   * The project names in the list should be identical to the project names 
   * returned by the SensorBaseClient in a project object. 
   * 
   * @param projectNames - the list of project names for which the Hackystat
   * client has permission to send data to the SocNet server
   */
  public void setProjectNames(List<String> projectNames) {

    if (projectNames.isEmpty()) {
      hackystatClientProperties.clear();
      return;
    }

    StringBuffer val = new StringBuffer(projectNames.get(0));

    for (int i = 1; i < projectNames.size(); i++) {
        val.append(",");
        val.append(projectNames.get(i));
    }

    hackystatClientProperties.setProperty(PROJECT_NAMES_KEY, val.toString());
  }

  /**
   * Gets the earliest date for which the Hackystat client is allowed to access
   * data about the project passed as a parameter
   * 
   * @param projectname - the project for which we want to send data to the 
   * SocNet server 
   * @return the earliest date for which we are allowed to access and send data
   * @throws java.text.ParseException
   */
  public Date getStartDate(String projectname) throws ParseException {
    String d = hackystatClientProperties.getProperty(PROJECT_STARTDATE_PREFIX + projectname);

    return df.parse(d);
  }

  /**
   * Saves the earliest date for which the Hackystat client is allowed to acced
   * and send data about the project passed as a parameter.
   * 
   * @param projectname - the project for which we want to send data to the 
   * SocNet server
   * @param d - the earliest date for which we are allowed to access the data
   * of the project specified by the projectname parameter
   */
  public void setStartDate(String projectname, Date d) {
    hackystatClientProperties.setProperty(PROJECT_STARTDATE_PREFIX + projectname, df.format(d));
  }

   /**
   * Gets the latest date for which the Hackystat client is allowed to access
   * data about the project passed as a parameter
   * 
   * @param projectname - the project for which we want to send data to the 
   * SocNet server 
   * @return the latest date for which we are allowed to access and send data
   * @throws java.text.ParseException
   */
  public Date getEndDate(String projectname) throws ParseException {
    String d = hackystatClientProperties.getProperty(PROJECT_ENDDATE_PREFIX + projectname);

    return df.parse(d);
  }

   /**
   * Saves the latest date for which the Hackystat client is allowed to acced
   * and send data about the project passed as a parameter.
   * 
   * @param projectname - the project for which we want to send data to the 
   * SocNet server
   * @param d - the latest date for which we are allowed to access the data
   * of the project specified by the projectname parameter
   */
  public void setEndDate(String projectname, Date d) {
    hackystatClientProperties.setProperty(PROJECT_ENDDATE_PREFIX + projectname, df.format(d));
  }

  /**
   * Returns the address of the sensorbase, as specified by the 
   * sensorshell.properties file, or the default value for the sensorbase address
   * if none is found in the sensorshell.properties file. The default value is
   * "http://dasha.ics.hawaii.edu:9876/sensorbase/"
   * 
   * @return the address of the sensorbase
   */
  public String getSensorbaseHost() {
    return sensorshellProperties.getProperty(SENSORBASE_HOST_KEY, DEFAULT_SENSORBASE_HOST);
  }

  /**
   * Returns the address of the socnet server as specified by the hackystat client
   * properties file, or a default value if there is none specified in the 
   * hackystat client properties file. The default value is:
   * "http://localhost:9999/socnet/";
   * 
   * @return - the address of the SocNet server
   */
  public String getSocnetHost() {
    return hackystatClientProperties.getProperty(SOCNET_HOST_KEY, DEFAULT_SOCNET_HOST);
  }

  /**
   * Returns the address of the Telemetry server as specified by the hackystat client
   * properties file, or a default value if there is none specified in the 
   * hackystat client properties file. The default value is:
   * "http://dasha.ics.hawaii.edu:9878/telemetry/"
   * 
   * @return - the address of the Telemetry server
   */
  public String getTelemetryHost() {
    return hackystatClientProperties.getProperty(TELEMETRY_HOST_KEY, DEFAULT_TELEMETRY_HOST);
  }

}
