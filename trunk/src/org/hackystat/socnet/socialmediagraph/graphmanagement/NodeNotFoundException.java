/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.graphmanagement;

/**
 * 
 * @author Rachel Shadoan
 */
public class NodeNotFoundException extends Exception {

  private static final long serialVersionUID = 1;

  public NodeNotFoundException() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public NodeNotFoundException(String nodeName, String nodeType) {
    super("\nThe node of name " + nodeName + " and type " + nodeType + " was "
        + "not found in the graph.\n");
  }

  public NodeNotFoundException(long nodeID) {
    super("\nThe node " + nodeID + " was not found in the graph.\n");
  }

  NodeNotFoundException(String nodeType) {
    super("\nNo nodes of type " + nodeType + " exist in the graph.\n");
  }

}
