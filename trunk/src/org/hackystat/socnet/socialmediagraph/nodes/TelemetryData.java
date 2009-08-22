/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.ArrayList;

/**
 *
 * @author Rachel Shadoan
 */
public class TelemetryData {
    
    ArrayList<TelemetryDataStream> streams;
    
    
   public TelemetryData()
   {
       streams = new ArrayList<TelemetryDataStream>();
   }

}
