package org.hackystat.socnet.socialmediagraph.graphmanagement;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.BetweenNodesRelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaRelationship;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;

public class GraphManager
{

    private NodeFactory nodeBuilder;
    private NeoService neo;
    private DatatypeFactory dtf;

    /**
     * Creates an instance of neo that will store information to the location
     * passed as a parameter, and initializes a NodeFactory to build nodes.
     * @param databaseLocation
     */
    public GraphManager(String databaseLocation) throws DatatypeConfigurationException
    {
        neo = new EmbeddedNeo(databaseLocation);

        dtf = DatatypeFactory.newInstance();

        Transaction tx = neo.beginTx();

        try
        {
            nodeBuilder = new NodeFactory(neo);
            tx.success();
        }
        finally
        {
            tx.finish();
        }
    }

    /**
     * Returns the node at the end of the given path from the start node.
     *
     * @param startNode
     * @param relationshipPath
     * @param relationshipPathDirections
     * @param nodeNamesOnPath
     * @return
     */
    public SocialMediaNode getNode(SocialMediaNode startNode,
            ArrayList<String> relationshipPath,
            ArrayList<String> relationshipPathDirections,
            ArrayList<String> nodeNamesOnPath)
    {
        SocialMediaNode node = null;
        Transaction tx = neo.beginTx();
        String pathDirection;
        Direction d;
        try
        {
            for (int i = 0; i < relationshipPath.size(); i++)
            {
                pathDirection = relationshipPathDirections.get(i);

                if (pathDirection.equals(Direction.INCOMING.name()))
                {
                    d = Direction.INCOMING;
                }
                else if (pathDirection.equals(Direction.OUTGOING.name()))
                {
                    d = Direction.OUTGOING;
                }
                else
                {
                    d = Direction.BOTH;
                }
                node = nodeBuilder.getNode(startNode,
                        BetweenNodesRelationshipType.getEnum(relationshipPath.get(i)),
                        d,
                        nodeNamesOnPath.get(i));
                startNode = node;
            }

            tx.success();
        }
        finally
        {
            tx.finish();

        }

        return node;
    }

    public SocialMediaNode getNode(long nodeID)
    {
        Transaction tx = neo.beginTx();
        SocialMediaNode node;
        try
        {
            node = new SocialMediaNode(nodeBuilder.getNode(nodeID));
            tx.success();
        }
        finally
        {
            tx.finish();
        }

        return node;
    }

    public SocialMediaNode getNode(String nodeType, String nodeName)
    {

        Transaction tx = neo.beginTx();
        SocialMediaNode node;
        try
        {
            node = new SocialMediaNode(nodeBuilder.getNode(
                    IsARelationshipType.getEnum(nodeType),
                    SocialMediaNode.NAME_KEY, nodeName));

            tx.success();
        }
        finally
        {
            tx.finish();
        }

        return node;
    }

    /**
     * Adds a node to the graph
     * @param nodeType - the kind of node being added
     * @param name - the value of the name property of the node
     */
    public void addNode(String nodeType, String name)
    {
        //start a transaction
        Transaction tx = neo.beginTx();

        try
        {
            //create the node
            SocialMediaNode node = nodeBuilder.createNode(
                    IsARelationshipType.getEnum(nodeType));

            //set the name property
            node.setName(name);
            node.setType(nodeType);

            //mark the transaction as successful. If you don't do this,
            //the transaction will fail on tx.finish() and the changes
            //won't be committed to the database
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();
        }
    }

    public ArrayList<XMLNode> getNodes()
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        Iterable<Node> nodes = null;
        ArrayList<XMLNode> xmlNodes = new ArrayList<XMLNode>();
        try
        {
            //get the nodes of the appropriate type from the database
            nodes = nodeBuilder.getNodes();

            for (Node node : nodes)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlNodes.add(convertToXMLNode(new SocialMediaNode(node)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlNodes;
    }
    
   public void relateNodes(String node1Type, String node1Name, 
           String node2Type, String node2Name, String relationship)
   {
       Transaction tx = neo.beginTx();
       
       try
       {
           nodeBuilder.relateNodes(IsARelationshipType.getEnum(node1Type), 
                   SocialMediaNode.NAME_KEY, node1Name, 
                   IsARelationshipType.getEnum(node2Type), 
                   SocialMediaNode.NAME_KEY, node2Name, 
                   BetweenNodesRelationshipType.getEnum(relationship));
           tx.success();
       }
       finally
       {
           tx.finish();
       }
       
   }

    public ArrayList<XMLRelationship> getRelationships()
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        Iterable<Relationship> rels = null;
        ArrayList<XMLRelationship> xmlRels = new ArrayList<XMLRelationship>();
        try
        {
            //get the nodes of the appropriate type from the database
            rels = nodeBuilder.getRelationships();

            for (Relationship rel : rels)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlRels.add(convertToXMLRelationship(new SocialMediaRelationship(rel)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlRels;
    }

    /**
     * This method retrieves all of the nodes of a certain type from the
     * database.
     *
     * @param nodeType
     * @return
     */
    public ArrayList<XMLNode> getNodes(String nodeType)
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        Iterable<Node> nodes = null;
        ArrayList<XMLNode> xmlNodes = new ArrayList<XMLNode>();
        try
        {
            //get the nodes of the appropriate type from the database
            nodes = nodeBuilder.getNodes(
                    IsARelationshipType.getEnum(nodeType));

            for (Node node : nodes)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlNodes.add(convertToXMLNode(new SocialMediaNode(node)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlNodes;
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo)
    {
        //start a new transaction
        Transaction tx = neo.beginTx();

        Iterable<Relationship> rels = null;

        ArrayList<XMLRelationship> xmlRels = new ArrayList<XMLRelationship>();
        try
        {
            //get the nodes of the appropriate type from the database
            rels = nodeBuilder.getNode(IsARelationshipType.getEnum(connectedTo.getName()),
                    SocialMediaNode.NAME_KEY,
                    connectedTo.getName()).getRelationships();

            for (Relationship rel : rels)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlRels.add(convertToXMLRelationship(new SocialMediaRelationship(rel)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlRels;
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo, String direction) throws Exception
    {
        //start a new transaction
        Transaction tx = neo.beginTx();

        Iterable<Relationship> rels = null;

        ArrayList<XMLRelationship> xmlRels = new ArrayList<XMLRelationship>();

        try
        {
            //get the nodes of the appropriate type from the database
            rels = nodeBuilder.getNode(IsARelationshipType.getEnum(connectedTo.getName()),
                    SocialMediaNode.NAME_KEY,
                    connectedTo.getName()).getRelationships(getDirection(direction));

            for (Relationship rel : rels)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlRels.add(convertToXMLRelationship(new SocialMediaRelationship(rel)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlRels;
    }

    public List<XMLRelationship> getRelationships(XMLNode connectedTo,
            String relationshipType,
            String direction) throws Exception
    {
        //start a new transaction
        Transaction tx = neo.beginTx();

        Iterable<Relationship> rels = null;

        ArrayList<XMLRelationship> xmlRels = new ArrayList<XMLRelationship>();

        try
        {
            //get the nodes of the appropriate type from the database
            rels = nodeBuilder.getNode(IsARelationshipType.getEnum(connectedTo.getName()),
                    SocialMediaNode.NAME_KEY,
                    connectedTo.getName()).getRelationships(
                    IsARelationshipType.getEnum(relationshipType), 
                    getDirection(direction));
            
            for (Relationship rel : rels)
            {
                //wrap each node in a SocialMediaNode wrapper
                xmlRels.add(convertToXMLRelationship(new SocialMediaRelationship(rel)));
            }
            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlRels;
    }

    public void addRelationship(XMLRelationship rel)
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        try
        {
            //XMLNode startNode = rel.getStartNode();
            //XMLNode endNode = rel.getEndNode();
            XMLNode startNode = rel.getXMLNode().get(0);
            XMLNode endNode = rel.getXMLNode().get(1);
            nodeBuilder.relateNodes(
                    IsARelationshipType.getEnum(startNode.getType()),
                    SocialMediaNode.NAME_KEY,
                    startNode.getName(),
                    IsARelationshipType.getEnum(endNode.getType()),
                    SocialMediaNode.NAME_KEY,
                    endNode.getName(),
                    BetweenNodesRelationshipType.getEnum(rel.getType()));

            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
    }

    public XMLRelationship getRelationship(long id)
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        XMLRelationship xmlRel = null;
        try
        {
            xmlRel = convertToXMLRelationship(new SocialMediaRelationship(
                    nodeBuilder.getRelationship(id)));

            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }
        return xmlRel;
    }

    public XMLRelationship getRelationship(String type, XMLNode startNode, XMLNode endNode)
    {
        //start a new transaction
        Transaction tx = neo.beginTx();
        XMLRelationship xmlRel = null;
        try
        {
            Relationship rel = nodeBuilder.getRelationship(
                    BetweenNodesRelationshipType.getEnum(type),
                    IsARelationshipType.getEnum(startNode.getType()),
                    SocialMediaNode.NAME_KEY, startNode.getName(),
                    IsARelationshipType.getEnum(endNode.getType()),
                    SocialMediaNode.NAME_KEY, endNode.getName());
            xmlRel = convertToXMLRelationship(new SocialMediaRelationship(rel));
                    

            //make the transaction as a success (This is important!)
            tx.success();
        }
        finally
        {
            //close the transaction
            tx.finish();

        }

        return xmlRel;
    }

    /**
     * This method is ONLY for testing purposes. All interactions with the
     * database must be wrapped in transations, and in order to test certain
     * functions outside of GraphManager, it's necessary to be able to
     * create a transaction without necessarily having direct access to the
     * instance of EmbeddedNeo. However, I suspect that it would have
     * potentially very strange and unpredictable effects if used other than
     * in very specific circumstances.
     * @return a Transaction
     */
    public Transaction getTransaction()
    {
        return neo.beginTx();
    }

    /**
     * This method cleanly exits the database.
     */
    public void exit()
    {
        neo.shutdown();
    }

    public boolean isFreshlyCreated()
    {
        System.out.println("This neo instance is so not fresh, it will not pinch your butt.");
        return false;
    }

    public XMLNode convertToXMLNode(SocialMediaNode smNode)
    {
        XMLNode n = new XMLNode();

        n.setName(smNode.getName());
        n.setID(smNode.getUnderNode().getId());
        n.setType(smNode.getType());
        n.setEndTime(dtf.newXMLGregorianCalendar(smNode.getEndTime()));
        n.setStartTime(dtf.newXMLGregorianCalendar(smNode.getStartTime()));

        return n;

    }

    public XMLRelationship convertToXMLRelationship(SocialMediaRelationship smRel)
    {
        XMLRelationship xmlRel = new XMLRelationship();
        ArrayList<XMLNode> nodes = (ArrayList<XMLNode>)xmlRel.getXMLNode();
        nodes.add(convertToXMLNode(new SocialMediaNode(smRel.getStartNode())));
        nodes.add(convertToXMLNode(new SocialMediaNode(smRel.getEndNode())));
        //xmlRel.setStartNode(convertToXMLNode(new SocialMediaNode(smRel.getStartNode())));
        //xmlRel.setEndNode(convertToXMLNode(new SocialMediaNode(smRel.getEndNode())));
        xmlRel.setID(smRel.getID());
        xmlRel.setType(smRel.getType());

        return xmlRel;

    }

    public Direction getDirection(String directionName) throws Exception
    {
        if (directionName.equals(Direction.INCOMING.name()))
        {
            return Direction.INCOMING;
        }
        else if (directionName.equals(Direction.OUTGOING))
        {
            return Direction.OUTGOING;
        }
        else if (directionName.equals(Direction.BOTH))
        {
            return Direction.BOTH;
        }
        else
        {
            throw new Exception(directionName + "is not a valid direction name!");
        }
    } 
    
    public static boolean areEqual(XMLNode n1, XMLNode n2)
    {
        boolean areEqual = false;
        
        if(n1.getName().equals(n2.getName()) 
                && n1.getType().equals(n2.getType()))
            areEqual = true;
        
        return areEqual;
    }
    
    public static boolean areEqual(XMLRelationship r1, XMLRelationship r2)
    {
        boolean areEqual = false;
        
        if(r1.getType().equals(r2.getType())
                && GraphManager.areEqual(r1.getXMLNode().get(0), r2.getXMLNode().get(0))
                && GraphManager.areEqual(r1.getXMLNode().get(1), r2.getXMLNode().get(1)))
            areEqual = true;
        
        return areEqual;
    }
}