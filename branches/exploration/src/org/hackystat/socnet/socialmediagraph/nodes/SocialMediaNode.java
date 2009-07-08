package org.hackystat.socnet.socialmediagraph.nodes;

import org.hackystat.socnet.socialmediagraph.nodes.interfaces.SocialMediaNodeInterface;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class SocialMediaNode implements SocialMediaNodeInterface
{
    protected Node underNode;

    protected static final String KEY_NAME = "name";
    
    public SocialMediaNode(Node underlyingNode)
    {
        underNode = underlyingNode;
    }
    
    public String getName()
    {
        return (String) underNode.getProperty(KEY_NAME);
    }
    
    public void setName(String name)
    {
        underNode.setProperty(KEY_NAME, name);
    }

    public Node getUnderNode()
    {
        return underNode;
    }
    public boolean hasName()
    {
       return underNode.hasProperty(KEY_NAME);
    }

    public Iterable<Relationship> getRelationships()
    {
        return underNode.getRelationships();
    }

    public Iterable<Relationship> getRelationships(RelationshipType type, Direction d)
    {
        return underNode.getRelationships(type, d);
    }

    public Iterable<Relationship> getRelationships(Direction d)
    {
        return underNode.getRelationships(d);
    }

    public Relationship relateTo(SocialMediaNode node2, RelationshipType relationship)
    {
        return this.underNode.createRelationshipTo(node2.underNode, relationship);
    }

}
