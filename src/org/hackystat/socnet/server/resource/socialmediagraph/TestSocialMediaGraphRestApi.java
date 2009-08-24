/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.socialmediagraph;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.junit.Test;
import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.hackystat.socnet.socialmediagraph.graphmanagement.GraphManager;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.BetweenNodesRelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.utils.JAXBHelper;
import org.restlet.data.Response;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @author Rachel Shadoan
 */
public class TestSocialMediaGraphRestApi extends SocNetRestApiHelper {
  public static final String EXCEPTION_MSG = "Exception during " +
          "SocialMediaGraphManager initialization processing";
  
  
  @Test
  public void testPutAndGetNode() throws Exception {
    assertTrue("testPutNode failed", putAndGetNodeSuccessful());
  }

  public static boolean putAndGetNodeSuccessful() throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException,
      IOException, Exception {
    boolean result = false;

    JAXBContext jaxbContext = null;

    getCleanGraphDB();

    try {
      jaxbContext = JAXBContext
          .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    }
    catch (Exception e) {
      String msg = EXCEPTION_MSG;
      throw new RuntimeException(msg, e);
    }

    XMLNode n1 = new XMLNode();
    n1.setType(IsARelationshipType.IS_USER.name());
    n1.setName("ElizaDoolittle");
    String xml = JAXBHelper.marshall(n1, jaxbContext);
    String uri = getURI("nodes/" + n1.getType() + "/" + n1.getName());
    Response response = PUT(uri, xml, TEST_EMAIL, TEST_EMAIL);

    if (!response.getStatus().isSuccess()) {
      System.out.println(response.getStatus().getDescription());
      System.out.println("Put was not successful");
      // return false;
    }
    else {
    }
    String getResult = GET(uri, TEST_EMAIL, TEST_EMAIL);

    XMLNode n2 = (XMLNode) JAXBHelper.unmarshall(getResult, jaxbContext);
    return n1.getType().equals(n2.getType()) && n1.getName().equals(n2.getName());

  }

  @Test
  public void testPutAndGetRelationship() throws Exception {
    assertTrue("testPutAndGetRelationship failed", wasSuccessfulPutAndGetNodeRelationship());
  }

  public static boolean wasSuccessfulPutAndGetNodeRelationship() throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException,
      IOException, Exception {
    boolean result = false;

    JAXBContext jaxbContext = null;

    getCleanGraphDB();

    try {
      jaxbContext = JAXBContext
          .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    }
    catch (Exception e) {
      String msg = EXCEPTION_MSG;
      throw new RuntimeException(msg, e);
    }

    String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node1name = "HackstatSocNet";
    String node2name = "RachelShadoan";
    String relationshipType = BetweenNodesRelationshipType.IS_FOLLOWING.name();

    XMLNode endNode = new XMLNode();
    endNode.setType(node1type);
    endNode.setName(node1name);

    String xml = JAXBHelper.marshall(endNode, jaxbContext);
    String uri = getURI("nodes/" + endNode.getType() + "/" + endNode.getName());
    Response response = PUT(uri, xml, TEST_EMAIL, TEST_EMAIL);

    XMLNode startNode = new XMLNode();
    startNode.setName(node2name);
    startNode.setType(node2type);

    xml = JAXBHelper.marshall(endNode, jaxbContext);
    uri = getURI("nodes/" + startNode.getType() + "/" + startNode.getName());
    response = PUT(uri, xml);

    XMLRelationship rel = new XMLRelationship();
    rel.setType(relationshipType);

    ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) rel.getXMLNode();

    nodes.add(startNode);
    nodes.add(endNode);

    xml = JAXBHelper.marshall(rel, jaxbContext);
    uri = getURI("relationships/" + relationshipType + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName());

    response = PUT(uri, xml);

    if (!response.getStatus().isSuccess()) {
      System.out.println(response.getStatus().getDescription());
      System.out.println("Put was not successful");
      return false;
    }

    String getResult = GET(uri);

    XMLRelationship r2 = (XMLRelationship) JAXBHelper.unmarshall(getResult, jaxbContext);

    return r2.getType().equals(relationshipType)
        && GraphManager.areEqual(r2.getXMLNode().get(0), startNode)
        && GraphManager.areEqual(r2.getXMLNode().get(1), endNode);

  }

  @Test
  public void testPutAndGetNodes() throws JAXBException, ParserConfigurationException,
      TransformerConfigurationException, TransformerException, IOException, Exception {
    assertTrue("testPutAndGetNodes failed", isSuccessfulPutAndGetNodes());
  }

  public static boolean isSuccessfulPutAndGetNodes() throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException,
      IOException, Exception {
    boolean result = false;

    JAXBContext jaxbContext = null;

    getCleanGraphDB();

    try {
      jaxbContext = JAXBContext
          .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    }
    catch (Exception e) {
      String msg = EXCEPTION_MSG;
      throw new RuntimeException(msg, e);
    }

    String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node3type = IsARelationshipType.IS_COUNTRY.name();
    String node4type = IsARelationshipType.IS_MOVIE.name();
    String node1name = "HackstatSocNet";
    String node2name = "RachelShadoan";
    String node3name = "Ghana";
    String node4name = "Brick";

    String relationshipType = BetweenNodesRelationshipType.IS_FOLLOWING.name();

    XMLNode endNode = new XMLNode();
    endNode.setType(node1type);
    endNode.setName(node1name);

    XMLNode startNode = new XMLNode();
    startNode.setName(node2name);
    startNode.setType(node2type);

    XMLNode node3 = new XMLNode();
    node3.setName(node3name);
    node3.setType(node3type);

    String xml = JAXBHelper.marshall(endNode, jaxbContext);
    String uri = getURI("nodes/" + node3.getType() + "/" + node3.getName());
    Response response = PUT(uri, xml);

    XMLNode node4 = new XMLNode();
    node4.setName(node4name);
    node4.setType(node4type);

    xml = JAXBHelper.marshall(endNode, jaxbContext);
    uri = getURI("nodes/" + node4.getType() + "/" + node4.getName());
    response = PUT(uri, xml);

    XMLRelationship rel = new XMLRelationship();
    rel.setType(relationshipType);

    ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) rel.getXMLNode();

    nodes.add(startNode);
    nodes.add(endNode);

    xml = JAXBHelper.marshall(rel, jaxbContext);
    uri = getURI("relationships/" + relationshipType + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName());

    response = PUT(uri, xml);

    if (!response.getStatus().isSuccess()) {
      System.out.println(response.getStatus().getDescription());
      System.out.println("Put was not successful");
      return false;
    }

    uri = getURI("nodes/" + node1type);

    String getResult = GET(uri);

    XMLNodes fetchedNodes = (XMLNodes) JAXBHelper.unmarshall(getResult, jaxbContext);

    ArrayList<XMLNode> retrievedNodes = (ArrayList<XMLNode>) fetchedNodes.getXMLNode();

    boolean containsStartNode = false;
    boolean containsEndNode = false;
    boolean containsNoOtherStuff = true;

    for (XMLNode xn : retrievedNodes) {

      if (GraphManager.areEqual(xn, startNode)) {
        containsStartNode = true;
      }
      else if (GraphManager.areEqual(xn, endNode)) {
        containsEndNode = true;
      }
      else {
        containsNoOtherStuff = false;
      }
    }

    return containsStartNode && containsEndNode && containsNoOtherStuff;

  }

  @Test
  public void testPutAndGetNodesOfType() throws JAXBException, ParserConfigurationException,
      TransformerConfigurationException, TransformerException, IOException, Exception {
    assertTrue("testPutAndGetNodes failed", isSuccessfulPutAndGetNodesOfType());
  }

  public static boolean isSuccessfulPutAndGetNodesOfType() throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException,
      IOException, Exception {
    boolean result = false;

    JAXBContext jaxbContext = null;

    getCleanGraphDB();

    try {
      jaxbContext = JAXBContext
          .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    }
    catch (Exception e) {
      String msg = EXCEPTION_MSG;
      throw new RuntimeException(msg, e);
    }

    String node1type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node2type = IsARelationshipType.IS_TWITTER_ACCOUNT.name();
    String node3type = IsARelationshipType.IS_COUNTRY.name();
    String node4type = IsARelationshipType.IS_MOVIE.name();
    String node1name = "HackstatSocNet";
    String node2name = "RachelShadoan";
    String node3name = "Ghana";
    String node4name = "Brick";

    String relationshipType = BetweenNodesRelationshipType.IS_FOLLOWING.name();

    XMLNode endNode = new XMLNode();
    endNode.setType(node1type);
    endNode.setName(node1name);

    XMLNode startNode = new XMLNode();
    startNode.setName(node2name);
    startNode.setType(node2type);

    XMLNode node3 = new XMLNode();
    node3.setName(node3name);
    node3.setType(node3type);

    String xml = JAXBHelper.marshall(endNode, jaxbContext);
    String uri = getURI("nodes/" + node3.getType() + "/" + node3.getName());
    Response response = PUT(uri, xml);

    XMLNode node4 = new XMLNode();
    node4.setName(node4name);
    node4.setType(node4type);

    xml = JAXBHelper.marshall(endNode, jaxbContext);
    uri = getURI("nodes/" + node4.getType() + "/" + node4.getName());
    response = PUT(uri, xml);

    XMLRelationship rel = new XMLRelationship();
    rel.setType(relationshipType);

    ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) rel.getXMLNode();

    nodes.add(startNode);
    nodes.add(endNode);

    xml = JAXBHelper.marshall(rel, jaxbContext);
    uri = getURI("relationships/" + relationshipType + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName());

    response = PUT(uri, xml);

    if (!response.getStatus().isSuccess()) {
      System.out.println(response.getStatus().getDescription());
      System.out.println("Put was not successful");
      return false;
    }

    uri = getURI("nodes/" + node1type + "/" + node1name + "/" + relationshipType + "/" + "INCOMING");

    String getResult = GET(uri);

    XMLNodes fetchedNodes = (XMLNodes) JAXBHelper.unmarshall(getResult, jaxbContext);

    ArrayList<XMLNode> retrievedNodes = (ArrayList<XMLNode>) fetchedNodes.getXMLNode();

    boolean containsStartNode = false;
    boolean containsNoOtherStuff = true;

    for (XMLNode xn : retrievedNodes) {
      System.out.println(xn.getName());
      if (GraphManager.areEqual(xn, startNode)) {
        containsStartNode = true;
      }
      else {
        containsNoOtherStuff = false;
      }
    }

    return containsStartNode && containsNoOtherStuff;

  }

  public static void main(String[] args) throws Exception {
    SocNetRestApiHelper.setupServer();
    putAndGetNodeSuccessful();
  }
}
