/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.nodes;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;

/**
 *
 * @author Rachel Shadoan
 */
public class SocialMediaRelationship {
    
    private Relationship underRelationship;
    
    public static final String START_TIME_KEY = "startTime";
    
    public static final String END_TIME_KEY = "endTime";
    
    public static final String DEFAULT_START_TIME = "";
    
    public static final String DEFAULT_END_TIME = "";
        
    public SocialMediaRelationship(Relationship underRel)
    {
        underRelationship = underRel;
      
    }
    
    public String getStartTime()
    {
        return (String) getUnderRelationship().getProperty(START_TIME_KEY, DEFAULT_START_TIME);
    }
    
    public String getEndTime()
    {
        return (String) getUnderRelationship().getProperty(END_TIME_KEY, DEFAULT_END_TIME);
    }
    
    public void setStartTime(String startTime)
    {
        getUnderRelationship().setProperty(START_TIME_KEY, startTime);
    }
    
    public void setEndTime(String endTime)
    {
        getUnderRelationship().setProperty(END_TIME_KEY, endTime);
    }

    public Relationship getUnderRelationship()
    {
        return underRelationship;
    }

    public void setUnderRelationship(Relationship underRelationship)
    {
        this.underRelationship = underRelationship;
    }
    
    public Node getStartNode()
    {
        return underRelationship.getStartNode();
    }
    
    public Node getEndNode()
    {
        return underRelationship.getEndNode();
    }
    
    public long getID()
    {
        return underRelationship.getId();
    }
    
    public String getType()
    {
        return underRelationship.getType().name();
    }
    
}
