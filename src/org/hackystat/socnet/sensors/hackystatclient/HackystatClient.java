/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.hackystatclient;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;

/**
 *
 * @author Rachel Shadoan
 */
public class HackystatClient {
    
    public static final String SERVER_ADDRESS_KEY = "sensorshell.sensorbase.host";
    public static final String USER_KEY = "sensorshell.sensorbase.user";
    public static final String PASSWORD_KEY = "sensorshell.sensorbase.password";
    
    public static void printErrorMsg(String propFile)
    {
        System.err.println("Error!  Cannot start the app without a propeties file!");
        System.err.println("The file must be located at " + propFile);
        System.err.println("It must contain the following keys: ");
        System.err.println(USER_KEY);
        System.err.println(PASSWORD_KEY);
        System.err.println(SERVER_ADDRESS_KEY);
        System.err.println("See SocNet documentation");
        System.exit(-1);
    }
    
    public static void main(String[] args) throws IOException, SensorBaseClientException
    {
        String userHome = System.getProperty("user.home");
        String propFile = userHome + "/.hackystat/.hackystat/sensorshell/sensorshell.properties";
        
        if (!new File(propFile).exists())
        {
            printErrorMsg(propFile);
        }

        Properties properties = new Properties();
        properties.load(new FileReader(propFile));
        
        
        String user = properties.getProperty(USER_KEY);
        String password = properties.getProperty(PASSWORD_KEY);
        String serveraddress = properties.getProperty(SERVER_ADDRESS_KEY);
        
        
        if(user == null || password == null ||  serveraddress == null)
        {
            printErrorMsg(propFile);
        }
        
        SensorBaseClient sensorBaseClient = new SensorBaseClient(serveraddress, user, password);
        
        ProjectIndex projects = sensorBaseClient.getProjectIndex();
        
        List<ProjectRef> projectRefs = projects.getProjectRef();
        
        for(ProjectRef ref : projectRefs)
        {
            System.out.println(ref.getName());
        }
        
        
        
    }

}
