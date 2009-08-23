/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationships;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;
import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.hackystat.socnet.utils.JAXBHelper;
import org.hackystat.utilities.tstamp.Tstamp;
import org.restlet.data.Response;

/**
 * 
 * @author Rachel Shadoan
 */
public class SocNetClient implements SocNetClientInterface {

  final String email;
  final String password;
  JAXBContext jaxbSocialMediaGraphContext;
  JAXBContext jaxbUserContext;
  String serveruri;

  public SocNetClient(String serverPath, String clientEmail, String clientPassword)
      throws JAXBException, Exception {
    email = clientEmail;
    password = clientPassword;
    this.jaxbSocialMediaGraphContext = JAXBContext
        .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    this.jaxbUserContext = JAXBContext
        .newInstance(org.hackystat.socnet.server.resource.users.jaxb.ObjectFactory.class);
    this.serveruri = serverPath;

    registerClient();
  }

  public void addNode(String nodeName, String nodeType) throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException {
    XMLNode node = new XMLNode();
    node.setName(nodeName);
    node.setType(nodeType);

    String xml = JAXBHelper.marshall(node, jaxbSocialMediaGraphContext);
    String serviceuri = SocNetRestApiHelper.getURI(serveruri, "nodes/" + node.getType() + "/"
        + node.getName());
    Response response = SocNetRestApiHelper.PUT(serviceuri, xml, email, password);

    if (!response.getStatus().isSuccess()) {
      throw new RuntimeException("Add node failed for " + nodeType + ":" + nodeName
          + "!  Message was: " + response.getStatus().getDescription());
    }
  }

  public void addRelationship(XMLRelationship rel) throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException {
    List<XMLNode> nodes = rel.getXMLNode();

    XMLNode startNode = nodes.get(0);
    XMLNode endNode = nodes.get(1);

    String servicePath = "relationships/" + rel.getType() + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName();

    String serviceuri = SocNetRestApiHelper.getURI(serveruri, servicePath);
    String xml = JAXBHelper.marshall(rel, jaxbSocialMediaGraphContext);

    Response response = SocNetRestApiHelper.PUT(serviceuri, xml, email, password);

    if (!response.getStatus().isSuccess()) {
      throw new RuntimeException("Add relationship failed!  Message was: "
          + response.getStatus().getDescription());
    }
  }

  public void addRelationshipTo(String startNodeName, String startNodeType, String endNodeName,
      String endNodeType, String relationshipType) throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException {
    addNode(startNodeName, startNodeType);
    addNode(endNodeName, endNodeType);

    XMLNode startNode = new XMLNode();
    startNode.setType(startNodeType);
    startNode.setName(startNodeName);

    XMLNode endNode = new XMLNode();
    endNode.setType(endNodeType);
    endNode.setName(endNodeName);

    XMLRelationship rel = new XMLRelationship();
    rel.setType(relationshipType);

    ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) rel.getXMLNode();

    nodes.add(startNode);
    nodes.add(endNode);
    String servicePath = "relationships/" + rel.getType() + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName();

    String serviceuri = SocNetRestApiHelper.getURI(serveruri, servicePath);
    String xml = JAXBHelper.marshall(rel, jaxbSocialMediaGraphContext);

    Response response = SocNetRestApiHelper.PUT(serviceuri, xml, email, password);

    if (!response.getStatus().isSuccess()) {
      throw new RuntimeException("Add relationship failed!  Message was: "
          + response.getStatus().getDescription());
    }
  }

  public XMLGregorianCalendar getDateLastUpdated(String startNodeName, String startNodeType,
      String endNodeName, String endNodeType, String relationshipType, String dateLastSent)
      throws Exception {

    String servicePath = "relationships/" + relationshipType + "/" + startNodeType + "/"
        + startNodeName + "/" + endNodeType + "/" + endNodeName + "/" + dateLastSent;

    System.out.println("getDateLastUpdated " + servicePath);
    String serviceuri = SocNetRestApiHelper.getURI(serveruri, servicePath);

    String response = SocNetRestApiHelper.GET(serviceuri, email, password);

    return Tstamp.makeTimestamp(response);
  }

  public XMLNodes getNodes(String nodeType) throws IOException, JAXBException {
    String serviceuri = SocNetRestApiHelper.getURI(serveruri, "nodes/" + nodeType);

    String xml = SocNetRestApiHelper.GET(serviceuri, email, password);
    XMLNodes nodes = (XMLNodes) JAXBHelper.unmarshall(xml, jaxbSocialMediaGraphContext);
    return nodes;
  }

  public XMLRelationships getNodes(String nodeName, String nodeType, String relationshipType)
      throws IOException, JAXBException {
    String path = "nodes/" + nodeName + "/" + nodeType + "/" + relationshipType + "/" + "INCOMING";
    String serviceuri = SocNetRestApiHelper.getURI(serveruri, path);

    String xml = SocNetRestApiHelper.GET(path, email, password);

    XMLRelationships rels = (XMLRelationships) JAXBHelper.unmarshall(xml,
        jaxbSocialMediaGraphContext);

    return rels;
  }

  public boolean endRelationship(String startNodeName, String startNodeType, String endNodeName,
      String endNodeType, String relationshipBetweenNodes) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void registerClient() throws Exception {

    String uri = SocNetRestApiHelper.getURI(serveruri, "users/" + email);

    String getResult = SocNetRestApiHelper.GET(uri, email, password);

    try {
      XMLUser user1 = (XMLUser) JAXBHelper.unmarshall(getResult, jaxbUserContext);
    }
    catch (Exception e) {
      // e.printStackTrace();
      uri = SocNetRestApiHelper.getURI(serveruri, "register/");
      XMLUser user = new XMLUser();
      user.setEmail(email);
      user.setPassword(password);
      String xml = JAXBHelper.marshall(user, jaxbUserContext);
      Response response = SocNetRestApiHelper.POST(uri, xml);

      if (!response.getStatus().isSuccess()) {
        System.out.println(response.getStatus().getDescription());
        System.out.println("POST was not successful");
        throw new Exception(response.getStatus().getDescription());

      }
      else {
        System.out.println("POST was successful");

      }
    }

    return;

  }

  public void updateRelationship(XMLRelationship rel) throws JAXBException,
      ParserConfigurationException, TransformerConfigurationException, TransformerException {
    List<XMLNode> nodes = rel.getXMLNode();

    XMLNode startNode = nodes.get(0);
    XMLNode endNode = nodes.get(1);

    String servicePath = "relationships/" + rel.getType() + "/" + startNode.getType() + "/"
        + startNode.getName() + "/" + endNode.getType() + "/" + endNode.getName();

    String serviceuri = SocNetRestApiHelper.getURI(serveruri, servicePath);
    String xml = JAXBHelper.marshall(rel, jaxbSocialMediaGraphContext);

    Response response = SocNetRestApiHelper.PUT(serviceuri, xml, email, password);

    if (!response.getStatus().isSuccess()) {
      throw new RuntimeException("Add relationship failed!  Message was: "
          + response.getStatus().getDescription());
    }
  }
}
