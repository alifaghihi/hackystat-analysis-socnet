/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.sensors.hackystatclient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;

/**
 * 
 * @author Rachel Shadoan
 */
public class ProjectSelector {

  public static void main(String[] args) throws IOException, SensorBaseClientException,
      ParseException {
    ClientConfig conf = new ClientConfig();
    String sensorbaseUsername = conf.getSensorbaseUsername();
    String sensorbasePass = conf.getSensorbasePassword();

    SensorBaseClient sbc = new SensorBaseClient(conf.getSensorbaseHost(), sensorbaseUsername,
        sensorbasePass);

    ArrayList<String> projects = new ArrayList<String>();
    HashMap<String, Date> starts = new HashMap<String, Date>();
    HashMap<String, Date> ends = new HashMap<String, Date>();

    ProjectIndex projInd = sbc.getProjectIndex(sensorbaseUsername);

    List<ProjectRef> projectRefs = projInd.getProjectRef();

    for (ProjectRef ref : projectRefs) {
      Project project = sbc.getProject(ref);
      String projectName = project.getName();
      projects.add(projectName);
      starts.put(projectName, project.getStartTime().toGregorianCalendar().getTime());
      ends.put(projectName, project.getEndTime().toGregorianCalendar().getTime());
    }

    SelectionGUI select = new SelectionGUI(projects, starts, ends);

    select.setVisible(true);
  }

}
