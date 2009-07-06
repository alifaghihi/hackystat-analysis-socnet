package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.ArrayList;

import org.hackystat.socnet.socialmediagraph.nodes.interfaces.SocialMediaNodeInterface;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class NodeFactory
{

    private final NeoService neo;

    /**
     * ReferenceNodeRelationshipType connect the database's reference node (the
     * entry into the graph) to subreference nodes to which all nodes of a certain
     * type are connected. For instance, the COUNTRIES relationship goes from 
     * the reference node to the Countries subreference node. All country nodes
     * are connected to the subreference node by a IS_COUNTRY relationship type.
     * 
     * Please note that if one is ever considering changing these for any reason, 
     * parallel changes must be made to the IsARelationshipType enum. So if one, 
     * for instance, is planning to delete "INTERESTS", one must also delete 
     * "IS_INTEREST"
     * 
     * It is vital that the two sets of enums be parallel--if 
     * FACEBOOK_ACCOUNTS in ReferenceNodeRelationshipType has an ordinal of 5, 
     * then IS_FACEBOOK_ACCOUNT in IsARelationshipType should also have an 
     * ordinal of 5.
     */
    public enum ReferenceNodeRelationshipType implements RelationshipType
    {
        BOOKS, CITIES, CONCENTRATIONS, COUNTRIES, EMPLOYERS, FACEBOOK_ACCOUNTS,
        GROUPS, HACKYSTAT_ACCOUNTS, INTERESTS, MOVIES, MUSIC, NETWORKS, NONUSERS,
        POLITICAL_PARTIES, PROGRAMMING_LANGUAGES, PROJECTS, RELIGIONS, SCHOOLS, 
        SHOWS, STATES, TWITTER_ACCOUNTS, UNIVERSITIES, USER
    };

    /**
     * IsARelationshipTypes connect the appropriate subreference node to all of the
     * nodes of that type. For instance, an IS_COUNTRY relationship goes from every
     * Country node to the Countries subreference node.
     * 
     * Please note that if one is ever considering changing these for any reason, 
     * parallel changes must be made to the ReferenceNodeRelationshipType enum. 
     * So if one, for instance, is planning to delete "IS_MOVIE", one must also 
     * delete "MOVIES". 
     * 
     * It is vital that the two sets of enums be parallel--if 
     * FACEBOOK_ACCOUNTS in ReferenceNodeRelationshipType has an ordinal of 5, 
     * then IS_FACEBOOK_ACCOUNT in IsARelationshipType should also have an 
     * ordinal of 5.
     */
    public enum IsARelationshipType implements RelationshipType
    {

        IS_BOOK, IS_CITY, IS_CONCENTRATION, IS_COUNTRY, IS_EMPLOYER, IS_FACEBOOK_ACCOUNT,
        IS_GROUP, IS_HACKYSTAT_ACCOUNT, IS_INTEREST, IS_MOVIE, IS_MUSIC, IS_NETWORK,
        IS_POLITICAL_PARTY, IS_PROGRAMMING_LANGUAGE, IS_PROJECT, IS_RELIGION, IS_SCHOOL, IS_SHOW, IS_STATE,
        IS_TWITTER_ACCOUNT, IS_UNIVERSITY, IS_USER
    };
    
    /**An arraylist to store the subreference nodes for quick and easy access*/
    private final ArrayList<Node> subreferenceNodes;
    
    /**The key to access each subreference node's name. */
    public final String KEY_SUBREFERENCE_NAME = "subreferenceName";

    /**
     * Constructs the subreference nodes for each type of node in the database
     * and relates the subreference node to the database's reference node.
     * @param neo 
     */
    public NodeFactory(NeoService neo)
    {
        //saves a reference to the NeoService object--we need this to create
        //nodes
        this.neo = neo;

        //create an arraylist to store the subreference nodes. All nodes of a 
        //certain type are connect to a subreference node of that type.
        //For instance, all Coder objects are connected to the Coders subreference
        //node by an IS_Coder relationship.
        subreferenceNodes = new ArrayList<Node>();

        //for each kind of reference node relationship
        for (ReferenceNodeRelationshipType relationship : ReferenceNodeRelationshipType.values())
        {
            int relationshipIndex = relationship.ordinal();

            //get the relationship that goes out from the database's reference node
            Relationship rel = this.neo.getReferenceNode().getSingleRelationship(
                    relationship,
                    Direction.OUTGOING);

            //if there are no relationships going trom the database's reference node,
            //that means there are no subreference nodes yet and we will need to 
            //create some
            if (rel == null)
            {
                //create the subreference node and add it to the subreference node
                //arraylist
                subreferenceNodes.add(neo.createNode());

                //sets the name of the subreference node
                subreferenceNodes.get(relationshipIndex).setProperty(
                        KEY_SUBREFERENCE_NAME, relationship.name());
                
                //get the reference node and create a relationship between it
                //and the subreference node that we just created
                this.neo.getReferenceNode().createRelationshipTo(
                        subreferenceNodes.get(relationshipIndex),
                        ReferenceNodeRelationshipType.values()[relationshipIndex]);

            } //if the relationship is not null, we're cool, and can
            else
            {
                subreferenceNodes.add(rel.getEndNode());
            }

        }

    }

    /**
     * Creates a node of the specified type and connects it to the appropriate
     * subreference node with an "IsA" type relationship
     * 
     * @param whatKindOfNode
     * @return a SocialMediaNode wrapping the underlying node in the database
     */
    public SocialMediaNode createNode(IsARelationshipType whatKindOfNode)
    {
        //create the underlying node for our new SocialMediaNode
        Node newNode = neo.createNode();

        //create a relationship between the subreference node of the appropriate
        //type and the new node we just created
        newNode.createRelationshipTo(subreferenceNodes.get(
                whatKindOfNode.ordinal()), whatKindOfNode);

        //now we need to create the wrapper node for the underlying node
        SocialMediaNode vertex = new SocialMediaNode(newNode);
        
        return vertex;
    }

    /**
     * Returns all of the nodes of the specified type.
     * 
     * @param whatKindOfNode
     * @return an ArrayList of all of the nodes of the type specified by whatKindOfNode
     */
    public Iterable<Node> getNodes(IsARelationshipType whatKindOfNode)
    {
        //get all of the relationships connected to the subreference node 
        //indicated by the parameter
        Iterable<Relationship> isARels = subreferenceNodes.get(
                whatKindOfNode.ordinal()).getRelationships(whatKindOfNode);

        //create an array list to store the nodes 
        ArrayList<Node> nodes = new ArrayList<Node>();

        //for each relationship connected to the subreference node
        for (Relationship relationship : isARels)
        {
            //get the node and add it to the list
            nodes.add(relationship.getStartNode());
        }

        //return the list of nodes
        return nodes;
    }
    
    /**
     * Returns the node of the specified type matching the specified property.
     * 
     * @param whatKindOfNode
     * @param key - the key to access the property
     * @param property - the value of the property we are looking for
     * @return
     */
    public Node getNode(IsARelationshipType whatKindOfNode, String key, String property)
    {
        for(Node node: getNodes(whatKindOfNode))
        {
            if(node.getProperty(key).equals(property))
                return node;
        }
        
        return null;
    }
    
    public Relationship relateNodes(IsARelationshipType node1Type, String node1name,
            IsARelationshipType node2Type, String node2name, RelationshipType relationship)
    {
   
        
   
    }
}
