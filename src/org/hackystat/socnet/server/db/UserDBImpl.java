/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.db;

import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;

/**
 * 
 * @author cody
 */
public interface UserDBImpl extends DBImpl {

  /**
   * Returns the XML UserIndex for all Users in this server.
   * 
   * @return The XML String containing an index to all Users.
   */
  public abstract String getUserIndex();

  /**
   * Returns the XMLUser instance as XML string, or null if not found.
   * 
   * @param email The user's email.
   * @return The XMLUser XML string, or null.
   */
  public abstract String getUser(String email);

  /**
   * Persists a XMLUser instance. If a XMLUser with this name already exists in the storage system,
   * it will be overwritten.
   * 
   * @param user The user
   * @param xmlUser The XMLUser marshalled into an XML String.
   * @param xmlUserRef The corresponding UserRef marshalled into an XML String
   * @return True if the user was successfully inserted.
   */
  public abstract boolean storeUser(XMLUser user, String xmlUser, String xmlUserRef);

  /**
   * Ensures that the XMLUser with the given email is no longer present in this manager.
   * 
   * @param email The user's email address.
   */
  public abstract void deleteUser(String email);
}
