/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.db;

/**
 *
 * @author cody
 */
public interface DBImpl
{

    /**
     * To be called as part of the startup process for a storage system. This method should:
     * <ul>
     * <li> Check to see if this storage system has already been created during a previous session.
     * <li> If no storage system exists, it should create one and initialize it appropriately.
     * </ul>
     */
    public abstract void initialize() throws Exception;

    /**
     * Returns true if the initialize() method did indeed create a fresh storage system.
     * This is used by the ResourceManagers to determine if they should read in default data or not.
     * @return True if the storage system is freshly created.
     */
    public abstract boolean isFreshlyCreated();
    
    public abstract void shutdown();
}
