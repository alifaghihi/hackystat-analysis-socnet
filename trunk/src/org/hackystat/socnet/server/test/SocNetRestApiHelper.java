package org.hackystat.socnet.server.test;

import java.io.IOException;
import java.util.logging.Level;
import org.hackystat.socnet.server.resource.users.UserManager;
import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.ServerProperties;
import org.hackystat.socnet.server.db.GraphDBImpl;
import org.hackystat.socnet.utils.FileUtils;
import static org.hackystat.socnet.server.ServerProperties.ADMIN_EMAIL_KEY;
import static org.hackystat.socnet.server.ServerProperties.ADMIN_PASSWORD_KEY;
import org.junit.BeforeClass;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;

/**
 * Provides helpful utility methods to SensorBase test classes, which will normally want to extend
 * this class.
 * 
 * @author Philip Johnson
 * 
 */
public class SocNetRestApiHelper {

  /** The SensorBase server used in these tests. */
  protected static Server server;
  /** Make a Manager available to this test class. */
  protected static UserManager userManager;
  /** The admin email. */
  protected static String adminEmail;
  /** The admin password. */
  protected static String adminPassword;

  public static final String TEST_EMAIL = "TEST_USER@TEST.COM";

  /**
   * Starts the server going for these tests.
   * 
   * @throws Exception If problems occur setting up the server.
   */
  @BeforeClass
  public static void setupServer() throws Exception {
    // Create a 'testing' version of ServerProperties.
    ServerProperties properties = new ServerProperties();
    properties.setTestProperties();
    // Now instantiate the server parameterized for testing purposes.
    SocNetRestApiHelper.server = Server.newInstance(properties);
    SocNetRestApiHelper.server.getLogger().setLevel(Level.SEVERE);
    SocNetRestApiHelper.userManager = (UserManager) server.getContext().getAttributes().get(
        "UserManager");

    SocNetRestApiHelper.adminEmail = server.getServerProperties().get(ADMIN_EMAIL_KEY);
    SocNetRestApiHelper.adminPassword = server.getServerProperties().get(ADMIN_PASSWORD_KEY);
  }

  /**
   * Returns the hostname associated with this test server.
   * 
   * @return The host name, including the context root.
   */
  protected static String getHostName() {
    return SocNetRestApiHelper.server.getHostName();
  }

  protected static void getCleanGraphDB() throws Exception {
    Server server = SocNetRestApiHelper.server;
    GraphDBImpl graphdb = (GraphDBImpl) server.getContext().getAttributes().get("GraphDB");

    if (!graphdb.isFreshlyCreated()) {
      graphdb.shutdown();
      ServerProperties props = server.getServerProperties();
      String dir = props.get(ServerProperties.TEST_GRAPH_DB_DIR_KEY);
      FileUtils.deleteDirectory(dir);
      graphdb.initialize();
      if (!graphdb.isFreshlyCreated()) {
        System.out.println("GRAPHDB NOT FRESH!");
        throw new RuntimeException("Could not create fresh GraphDB!");
      }
    }
  }

  public static Response makeRequest(Method method, String requestString, Representation entity) {
    Reference reference = new Reference(requestString);
    Request request = (entity == null) ? new Request(method, reference) : new Request(method,
        reference, entity);

    Client client = new Client(Protocol.HTTP);
    Response response = client.handle(request);

    return response;
  }

  public static Response makeRequestAuthenticated(Method method, String requestString,
      String username, String password, Representation entity) {
    Reference reference = new Reference(requestString);
    System.out.println("Reference: " + reference.toString());
    Request request = (entity == null) ? new Request(method, reference) : new Request(method,
        reference, entity);
    request.getClientInfo().getAcceptedMediaTypes().add(
        new Preference<MediaType>(MediaType.TEXT_XML));
    ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, username,
        password);
    request.setChallengeResponse(authentication);

    Client client = new Client(Protocol.HTTP);
    Response response = client.handle(request);
    return response;
  }

  public static Response PUT(String uri, String entity) {
    StringBuilder builder = new StringBuilder(500);

    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(entity);
    Representation representation = new StringRepresentation(builder, MediaType.TEXT_XML,
        Language.ALL, CharacterSet.UTF_8);

    return makeRequest(Method.PUT, uri, representation);
  }

  public static Response PUT(String uri, String entity, String username, String password) {
    StringBuilder builder = new StringBuilder(500);

    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(entity);
    Representation representation = new StringRepresentation(builder, MediaType.TEXT_XML,
        Language.ALL, CharacterSet.UTF_8);

    return makeRequestAuthenticated(Method.PUT, uri, username, password, representation);
  }

  public static String GET(String uri) throws IOException {
    Response r = makeRequest(Method.GET, uri, null);
    if (r.getEntity() == null) {
      return null;
    }
    else {
      return r.getEntity().getText();
    }

  }

  public static String GET(String uri, String username, String password) throws IOException {
    Response r = makeRequestAuthenticated(Method.GET, uri, username, password, null);
    if (r.getEntity() == null) {
      return null;
    }
    else {
      return r.getEntity().getText();
    }

  }

  public static Response POST(String uri, String entity) {
    StringBuilder builder = new StringBuilder(500);

    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(entity);
    Representation representation = new StringRepresentation(builder, MediaType.TEXT_XML,
        Language.ALL, CharacterSet.UTF_8);

    return makeRequest(Method.POST, uri, representation);
  }

  public static Response POST(String uri, String entity, String username, String password) {
    StringBuilder builder = new StringBuilder(500);

    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append(entity);
    Representation representation = new StringRepresentation(builder, MediaType.TEXT_XML,
        Language.ALL, CharacterSet.UTF_8);

    return makeRequestAuthenticated(Method.POST, uri, username, password, representation);
  }

  public static String getURI(String path) {
    return getURI(getHostName(), path);
  }

  public static String getURI(String host, String service) {
    String uri = host.endsWith("/") ? host : host + "/";
    uri += service;
    uri = uri.endsWith("/") ? uri : uri + "/";

    return uri;
  }
}
