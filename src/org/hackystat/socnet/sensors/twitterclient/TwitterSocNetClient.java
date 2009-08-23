/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.sensors.twitterclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.client.SocNetClient;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationships;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.neo4j.api.core.RelationshipType;

/**
 * 
 * @author Rachel Shadoan
 */
public class TwitterSocNetClient {

  String serveruri = "http://localhost:9876/socnet/";
  SocNetClient client;

  public TwitterSocNetClient(String host, String clientEmail, String clientPassword)
      throws JAXBException, Exception {
    this.serveruri = host;
    client = new SocNetClient(this.serveruri, clientEmail, clientPassword);
  }

  public void addNode(String nodeName, IsARelationshipType nodeType) throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException {
    client.addNode(nodeName, nodeType.name());
    System.out.println("Store node " + nodeType.name() + ":" + nodeName
        + " at socnet server was successful!");
  }

  public void addRelationshipTo(String startNodeName, IsARelationshipType startNodeType,
      String endNodeName, IsARelationshipType endNodeType, RelationshipType relationshipBetweenNodes)
      throws JAXBException, ParserConfigurationException, TransformerConfigurationException,
      TransformerException {
    client.addRelationshipTo(startNodeName, startNodeType.name(), endNodeName, endNodeType.name(),
        relationshipBetweenNodes.name());
  }

  public ArrayList<String> getNodes(IsARelationshipType nodeType) throws IOException, JAXBException {
    XMLNodes nodes;
    try {
      nodes = client.getNodes(nodeType.name());
    }
    catch (Exception ex) {
      nodes = null;
      System.out.println("NodesNotFound!");
    }

    ArrayList<String> result = new ArrayList<String>();

    if (nodes == null) {
      return result;
    }
    for (XMLNode node : nodes.getXMLNode()) {
      System.out.println("Got node " + node.getType() + ":" + node.getName()
          + " from socnet server!");
      result.add(node.getName());
    }

    return result;
  }

  public boolean endRelationship(String startNodeName, IsARelationshipType startNodeType,
      String endNodeName, IsARelationshipType endNodeType, RelationshipType relationshipBetweenNodes) {
    return false;
  }

  public ArrayList<Integer> getRelationships(String nodeName, IsARelationshipType nodeType) {
    return new ArrayList<Integer>();
  }

  public boolean endRelationship(int relationshipID) {
    return false;
  }

  public ArrayList<String> getNodes(String nodeName, IsARelationshipType nodeType,
      RelationshipType relationshipType) throws IOException, JAXBException {
    XMLRelationships rels = client.getNodes(nodeName, nodeType.name(), relationshipType.name());

    ArrayList<String> result = new ArrayList<String>();

    for (XMLRelationship rel : rels.getXMLRelationship()) {
      List<XMLNode> nodes = rel.getXMLNode();

      if (!nodes.get(0).getName().equals(nodeName)) {
        result.add(nodes.get(0).getName());
      }
      else {
        result.add(nodes.get(1).getName());
      }
    }
    return result;
  }
}
