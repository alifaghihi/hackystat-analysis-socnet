/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.socialmediagraph.graphmanagement;

import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.BetweenNodesRelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;
import org.hackystat.socnet.utils.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @author Rachel Shadoan
 */
public class TestGraphManager
{

    public static final String DIRECTORY =
            "/home/cody/.hackystat/socnet/testdb/graph";

    @Test
    public void testGraphAddAndRetrieveNode() throws 
            DatatypeConfigurationException,
            NodeNotFoundException
    {
        assertTrue("Testing putting a node in and retrieving from the graph",
                isSuccessfulAddAndRetrieveNode());
    }

    public static boolean isSuccessfulAddAndRetrieveNode() throws 
            DatatypeConfigurationException,
            NodeNotFoundException
    {
        boolean wasSuccessful = false;

        String directory = DIRECTORY;
        FileUtils.deleteDirectory(directory);

        GraphManager gm = new GraphManager(directory);

        String name = "Rachel Shadoan";
        String type = IsARelationshipType.IS_USER.name();

        gm.addNode(type, name);

        SocialMediaNode smNode = gm.getNode(type, name);

        if (smNode.hasName() && smNode.hasType()) {
            if (smNode.getName().equals(
                    name) && smNode.getType().equals(type)) {
                return true;
            }
        }
        return wasSuccessful;

    }

    @Test
    public void testGraphAddAndRetrieveRelationship() throws 
            DatatypeConfigurationException,
            NodeNotFoundException, RelationshipNotFoundException
    {
        assertTrue(
                "Testing putting two nodes and a relationship in and retrieving them from the graph",
                isSuccessfulAddAndRetrieveRelationship());
    }

    public static boolean isSuccessfulAddAndRetrieveRelationship()
            throws DatatypeConfigurationException, NodeNotFoundException,
            RelationshipNotFoundException
    {
        boolean wasSuccessful = false;

        String directory = DIRECTORY;
        FileUtils.deleteDirectory(directory);

        GraphManager gm = new GraphManager(directory);

        String node1name = "RachelShadoan";
        String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String node2name = "HackystatSocNet";
        String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String relationshipType =
                BetweenNodesRelationshipType.IS_FOLLOWING.name();

        gm.addNode(node1type, node1name);
        gm.addNode(node2type, node2name);

        gm.relateNodes(node1type, node1name, node2type, node2name,
                relationshipType);

        XMLNode xmlNode2 = gm.convertToXMLNode(gm.getNode(node2type, node2name));
        XMLNode xmlNode1 = gm.convertToXMLNode(gm.getNode(node1type, node1name));

        XMLRelationship xmlRel = gm.getRelationship(relationshipType, xmlNode1,
                xmlNode2);

        if (xmlRel.getXMLNode().get(0).getName().equals(node1name) && xmlRel.
                getXMLNode().get(1).getName().equals(node2name) && xmlRel.
                getXMLNode().get(0).getType().equals(node1type) && xmlRel.
                getXMLNode().get(1).getType().equals(node2type) && xmlRel.
                getType().equals(relationshipType)) {
            return true;
        }
        return wasSuccessful;

    }

    @Test
    public void testGraphRetrieveAllNodes() throws 
            DatatypeConfigurationException,
            NodeNotFoundException
    {
        assertTrue("testGraphRetrieveAllNodes", isSuccessfulRetrieveAllNodes());
    }

    public static boolean isSuccessfulRetrieveAllNodes() throws 
            DatatypeConfigurationException,
            NodeNotFoundException
    {
        boolean wasSuccessful = false;

        String directory = DIRECTORY;
        FileUtils.deleteDirectory(directory);

        GraphManager gm = new GraphManager(directory);

        String node1name = "RachelShadoan";
        String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String node2name = "HackystatSocNet";
        String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String relationshipType1to2 = BetweenNodesRelationshipType.IS_FOLLOWING.
                name();

        String node3name = "Kittens";
        String node3type = IsARelationshipType.IS_INTEREST.name();
        String relationshipType1to3 = BetweenNodesRelationshipType.LIKES.name();

        gm.addNode(node1type, node1name);
        gm.addNode(node2type, node2name);
        gm.addNode(node3type, node3name);

        gm.relateNodes(node1type, node1name, node2type, node2name,
                relationshipType1to2);
        gm.relateNodes(node1type, node1name, node3type, node3name,
                relationshipType1to3);
        XMLNode xmlNode2 = gm.convertToXMLNode(gm.getNode(node2type, node2name));
        XMLNode xmlNode1 = gm.convertToXMLNode(gm.getNode(node1type, node1name));
        XMLNode xmlNode3 = gm.convertToXMLNode(gm.getNode(node3type, node3name));

        List<XMLNode> nodes = gm.getNodes();

        boolean n1returned = false;
        boolean n2returned = false;
        boolean n3returned = false;

        for (XMLNode n : nodes) {
            if (GraphManager.areEqual(n, xmlNode1)) {
                n1returned = true;
            }
            else if (GraphManager.areEqual(n, xmlNode2)) {
                n2returned = true;
            }
            else if (GraphManager.areEqual(n, xmlNode3)) {
                n3returned = true;
            }
        }
        wasSuccessful = n1returned && n2returned && n3returned;

        return wasSuccessful;
    }

    @Test
    public void testGraphRetrieveNodesOfSpecifiedType() throws 
            DatatypeConfigurationException,
            NodeNotFoundException
    {
        assertTrue("testGraphRetrieveAllNodes", isSuccessfulRetrieveAllNodes());
    }

    public static boolean isSuccessfulRetrieveNodesOfSpecifiedType()
            throws DatatypeConfigurationException, NodeNotFoundException
    {
        boolean wasSuccessful = false;

        String directory = DIRECTORY;
        FileUtils.deleteDirectory(directory);

        GraphManager gm = new GraphManager(directory);

        String node1name = "RachelShadoan";
        String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String node2name = "HackystatSocNet";
        String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
        String relationshipType1to2 = BetweenNodesRelationshipType.IS_FOLLOWING.
                name();

        String node3name = "Kittens";
        String node3type = IsARelationshipType.IS_INTEREST.name();

        gm.addNode(node1type, node1name);
        gm.addNode(node2type, node2name);
        gm.addNode(node3type, node3name);

        gm.relateNodes(node1type, node1name, node2type, node2name,
                relationshipType1to2);

        XMLNode xmlNode2 = gm.convertToXMLNode(gm.getNode(node2type, node2name));
        XMLNode xmlNode1 = gm.convertToXMLNode(gm.getNode(node1type, node1name));

        List<XMLNode> nodes = gm.getNodes(node1type);

        boolean n1returned = false;
        boolean n2returned = false;
        boolean noMoreReturned = true;

        for (XMLNode n : nodes) {
            if (GraphManager.areEqual(n, xmlNode1)) {
                n1returned = true;
            }
            else if (GraphManager.areEqual(n, xmlNode2)) {
                n2returned = true;
            }
            else {
                noMoreReturned = false;
            }
        }
        wasSuccessful = n1returned && n2returned && noMoreReturned;

        return wasSuccessful;
    }
}
