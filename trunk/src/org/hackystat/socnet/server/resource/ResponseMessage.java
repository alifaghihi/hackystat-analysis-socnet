package org.hackystat.socnet.server.resource;

import java.util.logging.Logger;

import org.hackystat.utilities.stacktrace.StackTrace;

/**
 * Provides standardized strings and formatting for response codes.  
 * This class is intended to make error reporting more uniform and informative. 
 * A good error message will always include an explanation for why the operation failed,
 * and what the requested operation was. 
 * @author Philip Johnson
 */
public class ResponseMessage {
  
 
  /**
   * The error message for requests that generate an unspecified internal error. 
   * @param resource The resource associated with this request. 
   * @param logger The logger. 
   * @param e The exception. 
   * @return A string describing the problem.
   */
  public static String internalError(SocNetResource resource, Logger logger, Exception e) {
    String message =  String.format("Internal error %s:%n  Request: %s %s",
        e.getMessage(),
        resource.getRequest().getMethod().getName(),
        resource.getRequest().getResourceRef().toString());
    logger.info(String.format("%s\n%s", message, StackTrace.toString(e)));
    return message;
  }

  /**
   * The error message for miscellaneous "one off" error messages. 
   * @param resource The resource associated with this request. 
   * @param message A short string describing the problem.
   * @return A string describing the problem.
   */
   public static String miscError(SocNetResource resource, String message) {
    return String.format("Request generated error: %s:%n  Request: %s %s", 
        message,  
        resource.getRequest().getMethod().getName(),
        resource.getRequest().getResourceRef().toString());
  }
  
  /**
   * The error message for unknown users.
   * @param resource The resource associated with this request. 
   * @param user A short string describing the problem.
   * @return A string describing the problem.
   */
  public static String undefinedUser(SocNetResource resource, String user) {
    return String.format("Undefined user %s:%n  Request: %s %s", 
        user,
        resource.getRequest().getMethod().getName(),
        resource.getRequest().getResourceRef().toString());
  }
  
  
  
  /**
   * The error message for requests where a timestamp is not supplied or is not parsable.
   * @param resource The resource associated with this request.
   * @param timestamp The bogus timestamp. 
   * @return A string describing the problem.
   */
  public static String badTimestamp(SocNetResource resource, String timestamp) {
    return String.format("Bad timestamp %s:%n  Request: %s %s", 
        timestamp,
        resource.getRequest().getMethod().getName(),
        resource.getRequest().getResourceRef().toString());
  }
  
    /**
   * The error message for requests that only the admin can handle. 
   * @param resource The resource associated with this request. 
   * @return A string describing the problem.
   */
   static String adminOnly(SocNetResource resource) {
    return String.format("Request requires administrator privileges:%n  Request: %s %s",
        resource.getRequest().getMethod().getName(),
        resource.getRequest().getResourceRef().toString());
  }

}
