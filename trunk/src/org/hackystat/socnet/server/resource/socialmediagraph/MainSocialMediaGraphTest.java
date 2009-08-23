/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.socialmediagraph;

import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.hackystat.socnet.utils.JAXBHelper;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;

/**
 * 
 * @author Rachel Shadoan
 */
public class MainSocialMediaGraphTest {

  public static void main(String[] args) throws JAXBException, ParserConfigurationException,
      TransformerConfigurationException, TransformerException {
    String host = "http://localhost:9876/socnet/";

    JAXBContext jaxbContext = null;

    try {
      jaxbContext = JAXBContext
          .newInstance(org.hackystat.socnet.server.resource.socialmediagraph.jaxb.ObjectFactory.class);
    }
    catch (Exception e) {
      String msg = "Exception during SocialMediaGraphManager initialization processing";
      throw new RuntimeException(msg, e);
    }

    XMLNode n = new XMLNode();
    XMLNode n2 = new XMLNode();
    n.setName("StartNode!");
    n.setType("TEST");
    n2.setName("EndNode!");
    n2.setType("TEST");

    XMLRelationship r = new XMLRelationship();
    ArrayList<XMLNode> nodes = (ArrayList<XMLNode>) r.getXMLNode();
    nodes.add(n);
    nodes.add(n2);
    // r.setStartNode(n);
    // r.setEndNode(n2);
    r.setType("TEST");

    String xml = JAXBHelper.marshall(r, jaxbContext);

    StringBuilder builder = new StringBuilder(500);

    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(xml);
    Representation representation = new StringRepresentation(builder, MediaType.TEXT_XML,
        Language.ALL, CharacterSet.UTF_8);
    String uri = host.endsWith("/") ? host : host + "/";
    uri += "relationships/" + r.getType() + "/";

    Response response = SocNetRestApiHelper.makeRequest(Method.PUT, uri, representation);

    if (response.getStatus().isSuccess()) {
      System.out.println(response);
      System.out.println("We succeeded!");
    }
    else {
      System.out.println("You have failed.");
    }
  }
}
