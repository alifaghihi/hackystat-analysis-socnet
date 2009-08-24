/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.utils;

import java.io.File;

/**
 * 
 * @author Rachel Shadoan
 */
public class FileUtils {

  public static boolean deleteDirectory(String dirName) {
    return deleteDirectory(new File(dirName));
  }

  public static boolean deleteDirectory(File dir) {
    if (!dir.exists()) {
      return false;
    }
    for (File f : dir.listFiles()) {
      if (f.isDirectory()) {
        deleteDirectory(f);
      }
      else {
        if(!f.delete())
            return false;
      }
    }

    return dir.delete();
  }
}
