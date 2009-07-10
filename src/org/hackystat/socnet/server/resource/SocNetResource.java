package org.hackystat.socnet.server.resource;


import org.hackystat.socnet.server.resource.ResponseMessage;
import org.hackystat.socnet.server.Server;
import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * An abstract superclass for all SensorBase resources that supplies common 
 * initialization and validation processing. 
 * <p>
 * Initialization processing includes:
 * <ul>
 * <li> Extracting the authenticated user identifier (when authentication available)
 * <li> Extracting the user email from the URI (when available)
 * <li> Declares that the TEXT/XML representational variant is supported.
 * <li> Providing instance variables bound to the ProjectManager, SdtManager, UserManager, and 
 * SensorDataManager.
 * </ul>
 * <p>
 * Validation processing involves a set of "validated" methods. These check the values
 * of various parameters in the request, potentially initializing instance variables
 * as a result.  If the validation process fails, these methods set the Restlet 
 * Status value appropriately and return false. 
 * 
 * @author Philip Johnson
 *
 */
public abstract class SocNetResource extends Resource {
  

  
  /** The server. */
  protected Server server; 
  
  /** Everyone generally wants to create one of these, so declare it here. */
  protected String responseMsg;
  
  /**
   * Provides the following representational variants: TEXT_XML.
   * @param context The context.
   * @param request The request object.
   * @param response The response object.
   */
  public SocNetResource(Context context, Request request, Response response) {
    super(context, request, response);
  
    getVariants().clear(); // copied from BookmarksResource.java, not sure why needed.
    getVariants().add(new Variant(MediaType.TEXT_XML));
  }
  

  /**
   * The Restlet getRepresentation method which must be overridden by all concrete Resources.
   * @param variant The variant requested.
   * @return The Representation. 
   */
  @Override
  public abstract Representation represent(Variant variant);
  
  /**
   * Creates and returns a new Restlet StringRepresentation built from xmlData.
   * The xmlData will be prefixed with a processing instruction indicating UTF-8 and version 1.0.
   * @param xmlData The xml data as a string. 
   * @return A StringRepresentation of that xmldata. 
   */
  public static StringRepresentation getStringRepresentation(String xmlData) {
    StringBuilder builder = new StringBuilder(500);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(xmlData);
    return new StringRepresentation(builder, MediaType.TEXT_XML, Language.ALL, CharacterSet.UTF_8);
  }
  
  
  /**
   * Helper function that removes any newline characters from the supplied string and 
   * replaces them with a blank line. 
   * @param msg The msg whose newlines are to be removed. 
   * @return The string without newlines. 
   */
  private String removeNewLines(String msg) {
    return msg.replace(System.getProperty("line.separator"), " ");
  }
  
  /**
   * Called when an exception is caught while processing a request.
   * Just sets the response code.  
   * @param timestamp The timestamp that could not be parsed.
   */
  protected void setStatusBadTimestamp (String timestamp) { 
    this.responseMsg = ResponseMessage.badTimestamp(this, timestamp);
    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg)); 
  }
  
  
  /**
   * Called when an exception is caught while processing a request.
   * Just sets the response code.  
   * @param e The exception that was caught.
   */
  protected void setStatusInternalError (Exception e) { 
    this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
  }
  
  /**
   * Called when a miscellaneous "one off" error is caught during processing.
   * @param msg A description of the error.
   */
  protected void setStatusMiscError (String msg) { 
    this.responseMsg = ResponseMessage.miscError(this, msg);
    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
  }
}
