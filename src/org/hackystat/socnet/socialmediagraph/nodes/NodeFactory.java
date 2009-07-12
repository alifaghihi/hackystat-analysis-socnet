package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.hackystat.socnet.socialmediagraph.graphmanagement.NodeNotFoundException;
import org.hackystat.socnet.socialmediagraph.graphmanagement.RelationshipNotFoundException;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.ReturnableEvaluator;
import org.neo4j.api.core.StopEvaluator;
import org.neo4j.api.core.Traverser;
import org.neo4j.api.core.Traverser.Order;

public class NodeFactory
{

    private final NeoService neo;

    public Iterable<Node> getNodes()
    {
        return neo.getAllNodes();
    }

    public Iterable<Relationship> getRelationships()
    {
        Iterable<Node> allNodes = neo.getAllNodes();
        
        ArrayList<Relationship> allRels = new ArrayList<Relationship>();
        
        for(Node node: allNodes)
        {
            allRels.addAll((Collection<Relationship>) node.getRelationships());
        }
        
        return allRels;
    }

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
        SHOWS, STATES, TWITTER_ACCOUNTS, UNIVERSITIES, USER;
        
        private static HashMap<String, ReferenceNodeRelationshipType> nameMap;
        
        static
        {
           nameMap = new HashMap<String, ReferenceNodeRelationshipType>();
           
           for(ReferenceNodeRelationshipType type : ReferenceNodeRelationshipType.values())
           {
               nameMap.put(type.name(), type);
           }
        }
        
        public static ReferenceNodeRelationshipType getEnum(String name)
        {
            return nameMap.get(name);
        }
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
        IS_TWITTER_ACCOUNT, IS_UNIVERSITY, IS_USER;
                
                
        private static HashMap<String, IsARelationshipType> nameMap;
        
        static
        {
           nameMap = new HashMap<String, IsARelationshipType>();
           
           for(IsARelationshipType type : IsARelationshipType.values())
           {
               nameMap.put(type.name(), type);
           }
        }
        
        public static IsARelationshipType getEnum(String name)
        {
            return nameMap.get(name);
        }
    };
    
    
    public enum BetweenNodesRelationshipType implements RelationshipType
    {

        IS_FOLLOWING, IS_FOLLOWED_BY, MENTIONS, LIKES, CONTRIBUTES_TO;
                
                
        private static HashMap<String, BetweenNodesRelationshipType> nameMap;
        
        static
        {
           nameMap = new HashMap<String, BetweenNodesRelationshipType>();
           
           for(BetweenNodesRelationshipType type : BetweenNodesRelationshipType.values())
           {
               nameMap.put(type.name(), type);
           }
        }
        
        public static BetweenNodesRelationshipType getEnum(String name)
        {
            return nameMap.get(name);
        }
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

        if(neo == null)
            throw new RuntimeException("Neo cannot be null.");
        
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

        if(whatKindOfNode==null)
            throw new RuntimeException("whatKindOfNode is null, which is a bad sign");
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
    
    public Iterable<Node> getNodes(String connectedToNodeName, 
            IsARelationshipType connectedToNodeType, 
            BetweenNodesRelationshipType relationshipType,
            Direction relationshipDirection) 
            throws NodeNotFoundException, RelationshipNotFoundException
    {
        Node hub = this.getNode(connectedToNodeType, SocialMediaNode.NAME_KEY, connectedToNodeName);
    
        if(hub == null)
            throw new NodeNotFoundException(connectedToNodeName, connectedToNodeType.name());
        
        Iterable<Relationship> rels = hub.getRelationships(relationshipType, relationshipDirection);
        
        if(rels == null)
            throw new RelationshipNotFoundException(connectedToNodeName, connectedToNodeType.name(), 
            relationshipDirection.name(), relationshipType.name());
        
        ArrayList<Node> nodes = new ArrayList<Node>();
        int i = 0;
        for(Relationship rel: rels)
        {
            if(nodes.contains(rel))
                continue;
            else
                nodes.add(rel.getOtherNode(hub));
        }
        
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
    
    public Node getNode(long nodeID)
    {
        return neo.getNodeById(nodeID);
    }

    public SocialMediaNode getNode(SocialMediaNode startNode, RelationshipType
            relationship, Direction d, String nextNodeName)
    {
            Traverser traverser = startNode.getUnderNode().traverse(Order.BREADTH_FIRST,
                    StopEvaluator.DEPTH_ONE, ReturnableEvaluator.ALL, relationship,
                    d);
            Iterator it = traverser.iterator();
            Node node = null;
            
            while(it.hasNext())
            {
                node = (Node) it.next();
                
                if(node.getProperty(SocialMediaNode.NAME_KEY) == nextNodeName)
                    return new SocialMediaNode(node);
            }

            return null;
    }

    /**
     * Findes the two nodes of the matching the parameters and then creates a
     * relationship of the given type from the first node to the second node.
     * @param node1Type
     * @param node1Key
     * @param node1Name
     * @param node2Type
     * @param node2Key
     * @param node2Name
     * @param relationship
     * @return
     */
    public Relationship relateNodes(IsARelationshipType node1Type, String node1Key,
            String node1Name, IsARelationshipType node2Type, String node2Key,
            String node2Name, RelationshipType relationship) throws NodeNotFoundException
    {
            Node node1 = getNode(node1Type, node1Key, node1Name);
            Node node2 = getNode(node2Type, node2Key, node2Name);
            
            if(node1 == null)
                throw new NodeNotFoundException(node1Name, node1Type.name());
            
            if(node2 == null)
                throw new NodeNotFoundException(node2Name, node2Type.name());
            

            
            return node1.createRelationshipTo(node2, relationship);
    }
    
    public Relationship getRelationship(long relationshipID)
    {
       return neo.getRelationshipById(relationshipID);
    }
    
    public Relationship getRelationship(RelationshipType relationship, 
            IsARelationshipType node1Type, String node1Key, 
            String node1Name, IsARelationshipType node2Type, String node2Key, 
            String node2Name) throws NodeNotFoundException
    {
        Node startNode = this.getNode(node1Type, node1Key, node1Name);
        Node endNode = this.getNode(node2Type, node2Key, node2Name);
        
        if(startNode == null)
                throw new NodeNotFoundException(node1Name, node1Type.name());
            
        if(endNode == null)
                throw new NodeNotFoundException(node2Name, node2Type.name());
        
        Iterable<Relationship> rels;
        
        rels = startNode.getRelationships(relationship, Direction.OUTGOING);
        
        Iterator it = rels.iterator();
        Relationship r;
       
        while(it.hasNext())
        {
            r = (Relationship) it.next();
            
            if(r.getEndNode().equals(endNode))
                return r;
        }
        
        return (Relationship) it.next();
    }
}
