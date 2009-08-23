package org.hackystat.socnet.socialmediagraph.nodes.interfaces;

import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public interface SocialMediaNodeInterface {

  public String getName();

  public void setName(String name);

  public Relationship relateTo(SocialMediaNode node2, RelationshipType relationship);

}
