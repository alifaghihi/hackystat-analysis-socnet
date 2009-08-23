/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream;
import org.hackystat.socnet.socialmediagraph.graphmanagement.InvalidArgumentException;
import org.hackystat.socnet.socialmediagraph.nodes.ContributesToRelationship.Streams;
import org.hackystat.socnet.socialmediagraph.nodes.ContributesToRelationship.Streams;
import org.neo4j.api.core.Relationship;

/**
 * 
 * @author Rachel Shadoan
 */
public class ContributesToRelationship extends SocialMediaRelationship {

  public final String BUILD_TIMESTAMPS_KEY = "buildTimestamps";

  public final String BUILD_VALUES_KEY = "buildValues";

  public final String CHURN_TIMESTAMPS_KEY = "churnTimestamps";

  public final String CHURN_VALUES_KEY = "churnValues";

  public final String COMMIT_TIMESTAMPS_KEY = "commitTimestamps";

  public final String COMMIT_VALUES_KEY = "commitValues";

  public final String CODE_ISSUE_TIMESTAMPS_KEY = "codeIssueTimestamps";

  public final String CODE_ISSUE_VALUES_KEY = "codeIssueValues";

  public final String COVERAGE_TIMESTAMPS_KEY = "coverageTimestamps";

  public final String COVERAGE_VALUES_KEY = "cverageValues";

  public final String CYCLOMATIC_COMPLEXITY_TIMESTAMPS_KEY = "cyclomaticComplexityTimestamps";

  public final String CYCLOMATIC_COMPLEXITY_VALUES_KEY = "cyclomaticComplexityValues";

  public final String DEV_TIME_TIMESTAMPS_KEY = "devTimeTimestamps";

  public final String DEV_TIME_VALUES_KEY = "devTimeValues";

  public final String ISSUE_TIMESTAMPS_KEY = "issueTimestamps";

  public final String ISSUE_VALUES_KEY = "issueValues";

  public final String UNIT_TEST_TIMESTAMPS_KEY = "unitTestTimestamps";

  public final String UNIT_TEST_VALUES_KEY = "unitTestValues";

  public final String[] TIMESTAMP_KEYS = { BUILD_TIMESTAMPS_KEY, CHURN_TIMESTAMPS_KEY,
      CODE_ISSUE_TIMESTAMPS_KEY, COMMIT_TIMESTAMPS_KEY, COVERAGE_TIMESTAMPS_KEY,
      CYCLOMATIC_COMPLEXITY_TIMESTAMPS_KEY, DEV_TIME_TIMESTAMPS_KEY, ISSUE_TIMESTAMPS_KEY,
      UNIT_TEST_TIMESTAMPS_KEY };

  public final String[] VALUE_KEYS = { BUILD_VALUES_KEY, CHURN_VALUES_KEY, CODE_ISSUE_VALUES_KEY,
      COMMIT_VALUES_KEY, COVERAGE_VALUES_KEY, CYCLOMATIC_COMPLEXITY_VALUES_KEY,
      DEV_TIME_VALUES_KEY, ISSUE_VALUES_KEY, UNIT_TEST_VALUES_KEY };

  public final String[] DEFAULT_PROPERTY = {};

  public void updateStreams(List<TelemetryStream> streams) {
    TelemetryDataStream tds;

    for (int i = 0; i < streams.size(); i++) {
      tds = new TelemetryDataStream(getTimestampStream(TIMESTAMP_KEYS[i]),
          getValueStream(VALUE_KEYS[i]), streams.get(i));

      this.setStream(TIMESTAMP_KEYS[i], tds.getTimestampsArray(), VALUE_KEYS[i], tds
          .getValuesArray());
    }
  }

  public enum Streams {
    BUILD, CHURN, CODE_ISSUE, COMMIT, COVERAGE, CYCLOMATIC_COMPLEXITY, DEV_TIME, ISSUE, UNIT_TEST
  }

  public ContributesToRelationship(Relationship underRel) {
    super(underRel);
  }

  public ContributesToRelationship(Relationship underRel, List<TelemetryStream> streams) {
    super(underRel);

    TelemetryDataStream tds;

    for (int i = 0; i < streams.size(); i++) {
      tds = new TelemetryDataStream(streams.get(i));

      this.setStream(TIMESTAMP_KEYS[i], tds.getTimestampsArray(), VALUE_KEYS[i], tds
          .getValuesArray());
    }
  }

  public void setStream(String timestampKey, long[] timestamps, String valKey, double[] values) {
    underRelationship.setProperty(timestampKey, timestamps);
    underRelationship.setProperty(valKey, values);
  }

  public long[] getTimestampStream(String timestampKey) {
    return (long[]) underRelationship.getProperty(timestampKey, DEFAULT_PROPERTY);
  }

  public double[] getValueStream(String values) {
    return (double[]) underRelationship.getProperty(values, DEFAULT_PROPERTY);
  }

  public void setBuildStream(long[] timestamps, double[] buildvalues) {
    underRelationship.setProperty(BUILD_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(BUILD_VALUES_KEY, buildvalues);
  }

  public void setChurnStream(long[] timestamps, double[] churnValues) {
    underRelationship.setProperty(CHURN_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(CHURN_VALUES_KEY, churnValues);
  }

  public void setCodeIssueStream(long[] timestamps, double[] codeIssueValues) {
    underRelationship.setProperty(CODE_ISSUE_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(CODE_ISSUE_VALUES_KEY, codeIssueValues);
  }

  public void setCommitStream(long[] timestamps, double[] commitValues) {
    underRelationship.setProperty(COMMIT_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(COMMIT_VALUES_KEY, commitValues);
  }

  public void setCoverageStream(long[] timestamps, double[] coverageValues) {
    underRelationship.setProperty(COVERAGE_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(COVERAGE_VALUES_KEY, coverageValues);
  }

  public void setCyclomaticComplexityStream(long[] timestamps, double[] cyclomaticComplexityValues) {
    underRelationship.setProperty(CYCLOMATIC_COMPLEXITY_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(CYCLOMATIC_COMPLEXITY_VALUES_KEY, cyclomaticComplexityValues);
  }

  public void setDevTimeStream(long[] timestamps, double[] devTimeValues) {
    underRelationship.setProperty(DEV_TIME_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(DEV_TIME_VALUES_KEY, devTimeValues);
  }

  public void setIssueStream(long[] timestamps, double[] issueValues) {
    underRelationship.setProperty(ISSUE_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(ISSUE_VALUES_KEY, issueValues);
  }

  public void setUnitTestStream(long[] timestamps, double[] unitTestValues) {
    underRelationship.setProperty(UNIT_TEST_TIMESTAMPS_KEY, timestamps);
    underRelationship.setProperty(UNIT_TEST_VALUES_KEY, unitTestValues);
  }

  public TelemetryDataStream getBuildStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(BUILD_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(BUILD_VALUES_KEY));
  }

  public TelemetryDataStream getChurnStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(CHURN_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(CHURN_VALUES_KEY));
  }

  public TelemetryDataStream getCommitStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(COMMIT_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(COMMIT_VALUES_KEY));
  }

  public TelemetryDataStream getCoverageStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(COVERAGE_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(COVERAGE_VALUES_KEY));
  }

  public TelemetryDataStream getCodeIssueStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship
        .getProperty(CODE_ISSUE_TIMESTAMPS_KEY), (double[]) underRelationship
        .getProperty(CODE_ISSUE_VALUES_KEY));
  }

  public TelemetryDataStream getCyclomaticComplexityStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship
        .getProperty(CYCLOMATIC_COMPLEXITY_TIMESTAMPS_KEY), (double[]) underRelationship
        .getProperty(CYCLOMATIC_COMPLEXITY_VALUES_KEY));
  }

  public TelemetryDataStream getDevTimeStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(DEV_TIME_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(DEV_TIME_VALUES_KEY));
  }

  public TelemetryDataStream getIssueStream() throws InvalidArgumentException {
    return new TelemetryDataStream((long[]) underRelationship.getProperty(ISSUE_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(ISSUE_VALUES_KEY));
  }

  public TelemetryDataStream getUnitTestStream() throws InvalidArgumentException {
    return new TelemetryDataStream(
        (long[]) underRelationship.getProperty(UNIT_TEST_TIMESTAMPS_KEY),
        (double[]) underRelationship.getProperty(UNIT_TEST_VALUES_KEY));
  }

  public XMLGregorianCalendar getDateLastTelemetryData() throws InvalidArgumentException {
    return getBuildStream().getLastDate();
  }

}
