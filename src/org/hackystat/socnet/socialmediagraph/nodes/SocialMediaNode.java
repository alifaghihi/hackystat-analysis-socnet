package org.hackystat.socnet.socialmediagraph.nodes;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.socnet.socialmediagraph.nodes.interfaces.SocialMediaNodeInterface;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class SocialMediaNode implements SocialMediaNodeInterface {
  protected Node underNode;

  public static final String NAME_KEY = "name";

  public static final String START_TIME_KEY = "startTime";

  public static final String END_TIME_KEY = "endTime";

  public static final String TYPE_KEY = "type";

  public static final String NAME_DEFAULT = "No Name Assigned";

  public static final String START_TIME_DEFAULT = "1696-09-01T00:00:00Z";

  public static final String END_TIME_DEFAULT = "1696-09-01T00:00:00Z";

  public static final String TYPE_DEFAULT = "No Type Assigned";

  public SocialMediaNode(Node underlyingNode) {
    underNode = underlyingNode;
  }

  public String getName() {
    return (String) underNode.getProperty(NAME_KEY, NAME_DEFAULT);
  }

  public void setName(String name) {
    underNode.setProperty(NAME_KEY, name);
  }

  public Node getUnderNode() {
    return underNode;
  }

  public boolean hasName() {
    return underNode.hasProperty(NAME_KEY);
  }

  public Iterable<Relationship> getRelationships() {
    return underNode.getRelationships();
  }

  public Iterable<Relationship> getRelationships(RelationshipType type, Direction d) {
    return underNode.getRelationships(type, d);
  }

  public Iterable<Relationship> getRelationships(Direction d) {
    return underNode.getRelationships(d);
  }

  public Relationship relateTo(SocialMediaNode node2, RelationshipType relationship) {
    return this.underNode.createRelationshipTo(node2.underNode, relationship);
  }

  public void setType(String nodeType) {
    underNode.setProperty(TYPE_KEY, nodeType);
  }

  public void setStartTime(String startTime) {
    underNode.setProperty(START_TIME_KEY, startTime);
  }

  public void setEndTime(String endTime) {
    underNode.setProperty(END_TIME_KEY, endTime);
  }

  public String getStartTime() {
    return (String) underNode.getProperty(START_TIME_KEY, START_TIME_DEFAULT);
  }

  public String getEndTime() {
    return (String) underNode.getProperty(END_TIME_KEY, START_TIME_DEFAULT);
  }

  public String getType() {
    return (String) underNode.getProperty(TYPE_KEY, TYPE_DEFAULT);

  }

  public boolean hasType() {
    return underNode.hasProperty(TYPE_KEY);
  }

}
