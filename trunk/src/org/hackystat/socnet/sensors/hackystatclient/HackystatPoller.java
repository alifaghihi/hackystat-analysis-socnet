/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.sensors.hackystatclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;
import org.hackystat.socnet.server.client.SocNetClient;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.BetweenNodesRelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * 
 * @author Rachel Shadoan
 */
public class HackystatPoller {

  private String email;
  private HashSet<String> projectNames;
  private HashMap<String, XMLGregorianCalendar> projectStarts;
  private HashMap<String, XMLGregorianCalendar> projectEnds;
  private HashMap<String, XMLGregorianCalendar> dateLastSent;
  private HashMap<String, Boolean> isUserSetEndDate;

  private final SensorBaseClient sensorBaseClient;
  private final SocNetClient socNetClient;
  private final TelemetryClient telemetryClient;
  public static final String BUILD_DEFAULTS = "*,*,*,false";
  public static final String CHURN_DEFAULTS = "*,false";
  public static final String CODE_ISSUE_DEFAULTS = "*,*";
  public static final String COMMIT_DEFAULTS = "*,false";
  public static final String COVERAGE_DEFAULTS = "Percentage,method";
  public static final String CYCLOMATIC_COMPLEXITY_DEFAULTS = "AverageComplexityPerMethod,10,JavaNCSS";
  public static final String DEV_TIME_DEFAULTS = "*,false";
  public static final String ISSUE_DEFAULTS = "*,all";
  public static final String UNIT_TEST_DEFAULTS = "TotalCount,*,false";
  public static final String[] DEFAULT_PARAMETERS = { BUILD_DEFAULTS, CHURN_DEFAULTS,
      CODE_ISSUE_DEFAULTS, COMMIT_DEFAULTS, COVERAGE_DEFAULTS, CYCLOMATIC_COMPLEXITY_DEFAULTS,
      DEV_TIME_DEFAULTS, ISSUE_DEFAULTS, UNIT_TEST_DEFAULTS };
  public static final String[] CHART_NAMES = { "Build", "Churn", "CodeIssue", "Commit", "Coverage",
      "CyclomaticComplexity", "DevTime", "Issue", "UnitTest" };

  public HackystatPoller(SensorBaseClient sbc, SocNetClient snc, TelemetryClient tc,
      String userEmail) throws SensorBaseClientException {
    email = userEmail;
    sensorBaseClient = sbc;
    socNetClient = snc;
    telemetryClient = tc;

    projectNames = new HashSet<String>();
    projectStarts = new HashMap<String, XMLGregorianCalendar>();
    projectEnds = new HashMap<String, XMLGregorianCalendar>();
    dateLastSent = new HashMap<String, XMLGregorianCalendar>();
    isUserSetEndDate = new HashMap<String, Boolean>();
  }

  public List<String> getProjects() throws SensorBaseClientException {
    ArrayList<String> projectNames = new ArrayList<String>();
    ProjectIndex projects = sensorBaseClient.getProjectIndex(email);

    List<ProjectRef> projectRefs = projects.getProjectRef();

    for (ProjectRef project : projectRefs) {
      projectNames.add(project.getName());
    }

    return projectNames;
  }

  public void addProject(String projectName, Date start, Date end) throws SensorBaseClientException {
    projectNames.add(projectName);

    GregorianCalendar startCal = new GregorianCalendar();
    startCal.setTime(start);
    projectStarts.put(projectName, Tstamp.makeTimestamp(startCal.getTimeInMillis()));

    dateLastSent.put(projectName, projectStarts.get(projectName));

    GregorianCalendar endCal = new GregorianCalendar();
    endCal.setTime(end);
    projectEnds.put(projectName, Tstamp.makeTimestamp(endCal.getTimeInMillis()));

    if (projectEnds.get(projectName).equals(
        sensorBaseClient.getProject(email, projectName).getEndTime())) {
      isUserSetEndDate.put(projectName, false);
    }
    else {
      isUserSetEndDate.put(projectName, true);
    }
  }

  public void clearProjects() {
    projectNames.clear();
    projectStarts.clear();
    projectEnds.clear();
    dateLastSent.clear();
    isUserSetEndDate.clear();
  }

  public org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream getProjectStream(
      String chartName, String project) throws TelemetryClientException, SensorBaseClientException {
    XMLGregorianCalendar now = Tstamp.makeTimestamp();
    XMLGregorianCalendar end;

    if (now.compare(projectEnds.get(project)) == DatatypeConstants.LESSER) {
      end = now;
    }
    else if (now.compare(projectEnds.get(project)) == DatatypeConstants.GREATER) {
      end = projectEnds.get(project);
    }
    else {
      end = now;
    }

    dateLastSent.put(project, end);

    org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartData tcd = telemetryClient
        .getChart(chartName, email, project, "Day", dateLastSent.get(project), end);

    List<org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream> stream = tcd
        .getTelemetryStream();

    return convertTelemetryStreamObjects(stream.get(0));
  }

  public org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream convertTelemetryStreamObjects(
      org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream hackystatStream) {
    org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream socnetStream = new org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream();

    if (hackystatStream.isSetTelemetryPoint()) {
      List<org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryPoint> socnetPoints = socnetStream
          .getTelemetryPoint();
      List<org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint> hackystatPoints = hackystatStream
          .getTelemetryPoint();

      socnetPoints.clear();

      for (org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint hackystatPoint : hackystatPoints) {
        socnetPoints.add(convertTelemetryPointObjects(hackystatPoint));
      }
    }

    if (hackystatStream.isSetYAxis()) {
      socnetStream.setYAxis(convertYAxisObjects(hackystatStream.getYAxis()));
    }

    if (hackystatStream.isSetName()) {
      socnetStream.setName(hackystatStream.getName());
    }
    return socnetStream;
  }

  public org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryPoint convertTelemetryPointObjects(
      org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint hackystatPoint) {
    org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryPoint socnetPoint = new org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryPoint();

    if (hackystatPoint.isSetTime()) {
      socnetPoint.setTime(hackystatPoint.getTime());
    }
    if (hackystatPoint.isSetValue()) {
      socnetPoint.setValue(hackystatPoint.getValue());
    }
    return socnetPoint;
  }

  public org.hackystat.socnet.server.resource.socialmediagraph.jaxb.YAxis convertYAxisObjects(
      org.hackystat.telemetry.service.resource.chart.jaxb.YAxis hackystatYAxis) {
    org.hackystat.socnet.server.resource.socialmediagraph.jaxb.YAxis socnetYAxis = new org.hackystat.socnet.server.resource.socialmediagraph.jaxb.YAxis();

    if (hackystatYAxis.isSetName()) {
      socnetYAxis.setName(hackystatYAxis.getName());
    }
    if (hackystatYAxis.isSetUnits()) {
      socnetYAxis.setUnits(hackystatYAxis.getUnits());
    }
    if (hackystatYAxis.isSetUpperBound()) {
      socnetYAxis.setUpperBound(hackystatYAxis.getUpperBound());
    }
    if (hackystatYAxis.isSetLowerBound()) {
      socnetYAxis.setLowerBound(hackystatYAxis.getLowerBound());
    }
    if (hackystatYAxis.isSetNumberType()) {
      socnetYAxis.setNumberType(hackystatYAxis.getNumberType());
    }
    return socnetYAxis;
  }

  private XMLRelationship buildRelationship(String project) throws TelemetryClientException,
      SensorBaseClientException {

    XMLRelationship rel = new XMLRelationship();
    List<org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream> relStreams = rel
        .getTelemetryStream();

    for (String chartName : CHART_NAMES) {
      relStreams.add(getProjectStream(chartName, project));
    }

    XMLNode startNode = new XMLNode();

    startNode.setName(email);
    startNode.setType(IsARelationshipType.IS_HACKYSTAT_ACCOUNT.name());

    XMLNode endNode = new XMLNode();

    endNode.setName(project);
    endNode.setType(IsARelationshipType.IS_PROJECT.name());

    rel.getXMLNode().add(startNode);
    rel.getXMLNode().add(endNode);

    return rel;
  }

  public void sendRelationships() throws JAXBException, ParserConfigurationException,
      TransformerConfigurationException, TransformerException, TelemetryClientException,
      SensorBaseClientException {
    for (String project : projectNames) {
      socNetClient.addRelationship(buildRelationship(project));
    }
  }

  public void updateRelationship(String projectName) throws TelemetryClientException,
      JAXBException, ParserConfigurationException, TransformerConfigurationException,
      TransformerException, SensorBaseClientException {
    socNetClient.updateRelationship(buildRelationship(projectName));
  }

  public void update() throws SensorBaseClientException, Exception {

    for (String projectName : projectNames) {
      dateLastSent.put(projectName, socNetClient.getDateLastUpdated(email,
          IsARelationshipType.IS_HACKYSTAT_ACCOUNT.name(), projectName,
          IsARelationshipType.IS_PROJECT.name(),
          BetweenNodesRelationshipType.CONTRIBUTES_TO.name(), dateLastSent.get(projectName)
              .toXMLFormat())); // not sure
      // about the
      // toXMLFormat
      // part

      if (!isUserSetEndDate.get(projectName)) {
        projectEnds.put(projectName, sensorBaseClient.getProject(email, projectName).getEndTime());
      }

      updateRelationship(projectName);
    }
  }

  public void initialize() throws JAXBException, ParserConfigurationException,
      TransformerConfigurationException, TransformerException, TelemetryClientException,
      SensorBaseClientException {
    sendRelationships();
  }
}
