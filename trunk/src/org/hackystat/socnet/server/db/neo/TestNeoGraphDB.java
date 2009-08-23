/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.db.neo;

import javax.xml.datatype.DatatypeConfigurationException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.socialmediagraph.graphmanagement.GraphManager;
import org.hackystat.socnet.socialmediagraph.graphmanagement.NodeNotFoundException;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.utils.FileUtils;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 
 * @author Rachel Shadoan
 */
public class TestNeoGraphDB {

  @Test
  public void testStoreAndGetNode() throws DatatypeConfigurationException, NodeNotFoundException {
    assertTrue("testStoreAndGet failed", isSuccessfulStoreAndGetNode());
  }

  public static boolean isSuccessfulStoreAndGetNode() throws DatatypeConfigurationException,
      NodeNotFoundException {
    FileUtils.deleteDirectory("~/test");
    NeoGraphDB neoGraph = new NeoGraphDB("~/test");

    String name = "Eliza DooLittle";
    String type = IsARelationshipType.IS_USER.name();
    XMLNode node = new XMLNode();
    node.setName(name);
    node.setType(type);
    neoGraph.storeNode(node);

    XMLNode returnedNode = neoGraph.getNode(type, name);

    if (GraphManager.areEqual(node, returnedNode)) {
      return true;
    }
    else {
      return false;
    }
  }
}
