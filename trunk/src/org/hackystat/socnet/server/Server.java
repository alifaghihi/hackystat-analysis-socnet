package org.hackystat.socnet.server;

import java.util.Map;

import org.hackystat.utilities.logger.HackystatLogger;
import org.hackystat.utilities.logger.RestletLoggerUtil;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;

import static org.hackystat.socnet.server.ServerProperties.HOSTNAME_KEY;
import static org.hackystat.socnet.server.ServerProperties.PORT_KEY;
import static org.hackystat.socnet.server.ServerProperties.CONTEXT_ROOT_KEY;
import static org.hackystat.socnet.server.ServerProperties.LOGGING_LEVEL_KEY;

import java.util.logging.Logger;
import org.hackystat.socnet.server.db.derby.DerbyUserDB;
import org.hackystat.socnet.server.db.neo.NeoGraphDB;
import org.hackystat.socnet.server.resource.helloping.HelloPingResource;
import org.hackystat.socnet.server.resource.socialmediagraph.SocialMediaGraphManager;
import org.hackystat.socnet.server.resource.socialmediagraph.NodeResource;
import org.hackystat.socnet.server.resource.socialmediagraph.RelationshipResource;
import org.hackystat.socnet.server.resource.users.UserManager;

/**
 * Sets up the HTTP Server process and dispatching to the associated resources. 
 * @author Philip Johnson
 */
public class Server extends Application { 

  /** Holds the Restlet Component associated with this Server. */
  private Component component; 
  
  /** Holds the host name associated with this Server. */
  private String hostName;
  
  /** Holds the HackystatLogger for the sensorbase. */
  private Logger logger; 
  
  /** Holds the ServerProperties instance associated with this sensorbase. */
  private ServerProperties serverProperties;
  
  /**
   * Creates a new instance of a SensorBase HTTP server, listening on the supplied port.
   * SensorBase properties are initialized from the User's sensorbase.properties file.  
   * @return The Server instance created. 
   * @throws Exception If problems occur starting up this server. 
   */
  public static Server newInstance() throws Exception {
    return newInstance(new ServerProperties());
  }
  
  /**
   * Creates a new instance of a SensorBase HTTP server suitable for unit testing. 
   * SensorBase properties are initialized from the User's sensorbase.properties file, 
   * then set to their "testing" versions.   
   * @return The Server instance created. 
   * @throws Exception If problems occur starting up this server. 
   */
  public static Server newTestInstance() throws Exception {
    ServerProperties properties = new ServerProperties();
    properties.setTestProperties();
    return newInstance(properties);
  }
  
  /**
   * Creates a new instance of a SensorBase HTTP server, listening on the supplied port.
   * @param  serverProperties The ServerProperties used to initialize this server.
   * @return The Server instance created. 
   * @throws Exception If problems occur starting up this server. 
   */
  public static Server newInstance(ServerProperties serverProperties) throws Exception {
    Server server = new Server();
    server.logger = HackystatLogger.getLogger("org.hackystat.socnet.server", "server");
    server.serverProperties = serverProperties;
    server.hostName = "http://" +
                      server.serverProperties.get(HOSTNAME_KEY) + 
                      ":" + 
                      server.serverProperties.get(PORT_KEY) + 
                      "/" +
                      server.serverProperties.get(CONTEXT_ROOT_KEY) +
                      "/";
    int port = Integer.valueOf(server.serverProperties.get(PORT_KEY));
    server.component = new Component();
    server.component.getServers().add(Protocol.HTTP, port);
    server.component.getDefaultHost()
      .attach("/" + server.serverProperties.get(CONTEXT_ROOT_KEY), server);

    // Set up logging.
    RestletLoggerUtil.useFileHandler("server");
    HackystatLogger.setLoggingLevel(server.logger, server.serverProperties.get(LOGGING_LEVEL_KEY));
    server.logger.warning("Starting SocNet Server.");
    server.logger.warning("Host: " + server.hostName);
    server.logger.info(server.serverProperties.echoProperties());

  

    // Now create all of the Resource Managers and store them in the Context.

    Map<String, Object> attributes = 
      server.getContext().getAttributes();
    attributes.put("SocNetServer", server);
    attributes.put("ServerProperties", server.serverProperties);
    DerbyUserDB userDB = new DerbyUserDB(server);
    userDB.initialize();
    attributes.put("UserDB", userDB);
    attributes.put("GraphDB", new NeoGraphDB(server));
    attributes.put("UserManager", new UserManager(server));
    attributes.put("SocialMediaGraphManager", new SocialMediaGraphManager(server));
    // Now let's open for business. 
    server.logger.info("Maximum Java heap size (MB): " + 
        (Runtime.getRuntime().maxMemory() / 1000000.0));
    server.component.start();
    server.logger.warning("SocNet Server (Version " + getVersion() + ") now running.");
    return server;
  }

  
 
  /**
   * Starts up the SensorBase web service using the properties specified in sensor.properties.  
   * Control-c to exit. 
   * @param args Ignored. 
   * @throws Exception if problems occur.
   */
  public static void main(final String[] args) throws Exception {
    Server.newInstance();
  }

  /**
   * Dispatch to the Projects, SensorData, SensorDataTypes, or Users Resource depending on the URL.
   * We will authenticate all requests except for registration (users?email={email}).
   * @return The router Restlet.
   */
  @Override
  public Restlet createRoot() {
    // First, create a Router that will have a Guard placed in front of it so that this Router's
    // requests will require authentication.
    Router authRouter = new Router(getContext());
   
    // Now create our "top-level" router which will allow the registration URI to proceed without
    // authentication, but all other URI patterns will go to the guarded Router. 
    Router router = new Router(getContext());
    router.attach("/ping", HelloPingResource.class);
    router.attach("/nodes/{nodetype}/{node}", NodeResource.class);
    router.attach("/relationships/{relationshiptype}", RelationshipResource.class);
    return router;
  }


  /**
   * Returns the version associated with this Package, if available from the jar file manifest.
   * If not being run from a jar file, then returns "Development". 
   * @return The version.
   */
  public static String getVersion() {
    String version = 
      Package.getPackage("org.hackystat.socnet.server").getImplementationVersion();
    return (version == null) ? "Development" : version; 
  }
  
  /**
   * Returns the host name associated with this server. 
   * Example: "http://localhost:9876/sensorbase/"
   * @return The host name. 
   */
  public String getHostName() {
    return this.hostName;
  }
  
 
  /**
   * Returns the ServerProperties instance associated with this server. 
   * @return The server properties.
   */
  public ServerProperties getServerProperties() {
    return this.serverProperties;
  }
  
  /**
   * Returns the logger for the SensorBase. 
   * @return The logger.
   */
  @Override
  public Logger getLogger() {
    return this.logger;
  }
}

