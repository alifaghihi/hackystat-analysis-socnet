/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.server.db.derby;

import static org.hackystat.socnet.server.ServerProperties.USER_DB_DIR_KEY;

import org.hackystat.socnet.server.db.UserDBImpl;
import org.hackystat.socnet.server.resource.users.jaxb.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import java.util.logging.Logger;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.socnet.server.Server;


/**
 *
 * @author Rachel Shadoan
 */
public class DerbyUserDB implements UserDBImpl{

  /** The JDBC driver. */
  private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
  
  /** The Database name. */
  private static final String dbName = "socnet";
  
  /**  The Derby connection URL. */ 
  private static final String connectionURL = "jdbc:derby:" + dbName + ";create=true";
  
  /** Indicates whether this database was initialized or was pre-existing. */
  private boolean isFreshlyCreated;
  
  /** The SQL state indicating that INSERT tried to add data to a table with a preexisting key. */
  private static final String DUPLICATE_KEY = "23505";
  
  /** The key for putting/retrieving the directory where Derby will create its databases. */
  private static final String derbySystemKey = "derby.system.home";
  
  /** The logger message for connection closing errors. */
  private static final String errorClosingMsg = "Derby: Error while closing. \n";
  
  /** The logger message when executing a query. */
  private static final String executeQueryMsg = "Derby: Executing query ";
  
  /** Required by PMD since this string occurs multiple times in this file. */
  private static final String ownerEquals = " owner = '";

  /** Required by PMD since this string occurs multiple times in this file. */
  private static final String sdtEquals = " sdt = '";
  private static final String toolEquals = " tool = '";
  
  /** Required by PMD as above. */
  private static final String quoteAndClause = "' AND ";
  private static final String andClause = " AND "; 
  private static final String orderByTstamp = " ORDER BY tstamp";
  private static final String orderByRuntime = " ORDER BY runtime DESC";
  private static final String derbyError = "Derby: Error ";
  private static final String indexSuffix = "Index>";
  private static final String xml = "Xml";

    /** Keeps a pointer to this Server for use in accessing the managers. */
  protected Server server;
  
  /** Keep a pointer to the Logger. */
  protected Logger logger;
  
  /**
   * Instantiates the Derby implementation.  Throws a Runtime exception if the Derby
   * jar file cannot be found on the classpath.
   * @param server The Socnet Server server instance. 
   */
  public DerbyUserDB(Server server) {
    this.server = server;
    this.logger = server.getLogger();
    // Set the directory where the DB will be created and/or accessed.
    // This must happen before loading the driver. 
    String dbDir = server.getServerProperties().get(USER_DB_DIR_KEY);
    System.getProperties().put(derbySystemKey, dbDir);
    // Try to load the derby driver. 
    try {
      Class.forName(driver); 
    } 
    catch (java.lang.ClassNotFoundException e) {
      String msg = "Derby: Exception during DbManager initialization: Derby not on CLASSPATH.";
      this.logger.warning(msg + "\n" + StackTrace.toString(e));
      throw new RuntimeException(msg, e);
    }
  }
  

  /** {@inheritDoc} */
  @Override
  public void initialize() {
    try {
      // Create a shutdown hook that shuts down derby.
      Runtime.getRuntime().addShutdownHook(new Thread() {
        /** Run the shutdown hook for shutting down Derby. */
        @Override 
        public void run() {
          Connection conn = null;
          try {
            conn = DriverManager.getConnection("jdbc:derby:;shutdown=true");
          }
          catch (Exception e) {
            System.out.println("Derby shutdown hook results: " + e.getMessage());
          }
          finally {
            try {
              conn.close();
            }
            catch (Exception e) { //NOPMD
              // we tried.
            }
          }
        }
      });
      // Initialize the database table structure if necessary.
      this.isFreshlyCreated = !isPreExisting();
      String dbStatusMsg = (this.isFreshlyCreated) ? 
          "Derby: uninitialized." : "Derby: previously initialized.";
      this.logger.info(dbStatusMsg);
      if (this.isFreshlyCreated) {
        this.logger.info("Derby: creating DB in: " + System.getProperty(derbySystemKey));
        createTables();
      }
    }
    catch (Exception e) {
      String msg = "Derby: Exception during DerbyImplementation initialization:";
      this.logger.warning(msg + "\n" + StackTrace.toString(e));
      throw new RuntimeException(msg, e);
    }

  }
  
  /**
   * Determine if the database has already been initialized with correct table definitions. 
   * Table schemas are checked by seeing if a dummy insert on the table will work OK.
   * @return True if the database exists and tables are set up correctly.
   * @throws SQLException If problems occur accessing the database or the tables are set right. 
   */
  private boolean isPreExisting() throws SQLException {
    Connection conn = null;
    Statement s = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.createStatement();
      s.execute(testUserTableStatement);
    }  
    catch (SQLException e) {
      String theError = (e).getSQLState();
      if ("42X05".equals(theError)) {
        // Database doesn't exist.
        return false;
      }  
      else if ("42X14".equals(theError) || "42821".equals(theError))  {
        // Incorrect table definition. 
        throw e;   
      } 
      else { 
        // Unknown SQLException
        throw e; 
      }
    }
    finally {
      if (s != null) {
        s.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    // If table exists will get -  WARNING 02000: No row was found 
    return true;
  }
  
  /**
   * Initialize the database by creating tables for each resource type.
   * @throws SQLException If table creation fails.
   */
  private void createTables() throws SQLException {
    Connection conn = null;
    Statement s = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.createStatement();
      s.execute(createUserTableStatement);
      s.close();
    }
    finally {
      s.close();
      conn.close();
    }
  }
  



  /** {@inheritDoc} */
  @Override
  public boolean isFreshlyCreated() {
    return this.isFreshlyCreated;
  }
  
  /**
   * Constructs a set of LIKE clauses corresponding to the passed set of UriPatterns.
   * <p>
   * Each UriPattern is translated in the following way:
   * <ul>
   * <li> If there is an occurrence of a "\" or a "/" in the UriPattern, then 
   * two translated UriPatterns are generated, one with all "\" replaced with "/", and one with 
   * all "/" replaced with "\".
   * <li> The escape character is "\", unless we are generating a LIKE clause containing a 
   * "\", in which case the escape character will be "/".
   * <li> All occurrences of "%" in the UriPattern are escaped.
   * <li> All occurrences of "_" in the UriPattern are escaped.
   * <li> All occurrences of "*" are changed to "%".
   * </ul>
   * The new set of 'translated' UriPatterns are now used to generate a set of LIKE clauses
   * with the following form:
   * <pre>
   * (RESOURCE like 'translatedUriPattern1' escape 'escapeChar1') OR
   * (RESOURCE like 'translatedUriPattern2' escape 'escapeChar2') ..
   * </pre>
   * 
   * <p>
   * There is one special case.  If the List(UriPattern) is null, empty, or consists of exactly one 
   * UriPattern which is "**" or "*", then the empty string is returned. This is an optimization for
   * the common case where all resources should be matched and so we don't need any LIKE clauses.
   * <p>
   * We return either the empty string (""), or else a string of the form:
   * " AND ([like clause] AND [like clause] ... )"
   * This enables the return value to be appended to the SELECT statement.
   * <p>
   * This method is static and package private to support testing. See the class 
   * TestConstructUriPattern for example invocations and expected return values. 
   *  
   * @param uriPatterns The list of uriPatterns.
   * @return The String to be used in the where clause to check for resource correctness.
   */
  static String constructLikeClauses(List<String> uriPatterns) {
    // Deal with special case. UriPatterns is null, or empty, or "**", or "*"
    if (((uriPatterns == null) || uriPatterns.isEmpty()) ||
        ((uriPatterns.size() == 1) && uriPatterns.get(0).equals("**")) ||
        ((uriPatterns.size() == 1) && uriPatterns.get(0).equals("*"))) {
      return "";
    }
    // Deal with the potential presence of path separator character in UriPattern.
    List<String> translatedPatterns = new ArrayList<String>();
    for (String pattern : uriPatterns) {
      if (pattern.contains("\\") || pattern.contains("/")) {
        translatedPatterns.add(pattern.replace('\\', '/'));
        translatedPatterns.add(pattern.replace('/', '\\'));
      }
      else {
        translatedPatterns.add(pattern);
      }        
    }
    // Now escape the SQL wildcards, and make our UriPattern wildcard into the SQL wildcard.
    for (int i = 0; i < translatedPatterns.size(); i++) {
      String pattern = translatedPatterns.get(i);
      pattern = pattern.replace("%", "`%"); // used to be /
      pattern = pattern.replace("_", "`_"); // used to be /
      pattern = pattern.replace('*', '%');
      translatedPatterns.set(i, pattern);
    }

    // Now generate the return string: " AND (<like clause> OR <like clause> ... )".
    StringBuffer buff = new StringBuffer();
    buff.append(" AND (");
    if (!translatedPatterns.isEmpty()) {
      buff.append(makeLikeClause(translatedPatterns, "`")); // used to be /
    }

    buff.append(')');
    
    return buff.toString();
  }
  
  /**
   * Creates a set of LIKE clauses with the specified escape character.
   * @param patterns The patterns. 
   * @param escape The escape character.
   * @return The StringBuffer with the LIKE clauses. 
   */
  private static StringBuffer makeLikeClause(List<String> patterns, String escape) {
    StringBuffer buff = new StringBuffer(); //NOPMD generates false warning about buff size.
    if (patterns.isEmpty()) {
      return buff;
    }
    for (Iterator<String> i = patterns.iterator(); i.hasNext(); ) {
      String pattern = i.next();
      buff.append("(RESOURCE LIKE '");
      buff.append(pattern);
      buff.append("' ESCAPE '");
      buff.append(escape);
      buff.append("')");
      if (i.hasNext()) {
        buff.append(" OR ");
      }
    }
    buff.append(' ');
    return buff;
  }
  
  /**
   * Constructs a clause of form ( OWNER = 'user1' [ OR OWNER = 'user2']* ). 
   * @param users The list of users whose ownership is being searched for.
   * @return The String to be used in the where clause to check for ownership.
   */
  private String constructOwnerClause(List<User> users) {
    StringBuffer buff = new StringBuffer();
    buff.append('(');
    // Use old school iterator so we can do a hasNext() inside the loop.
    for (Iterator<User> i = users.iterator(); i.hasNext(); ) {
      User user = i.next();
      buff.append(ownerEquals);
      buff.append(user.getEmail());
      buff.append('\'');
      if (i.hasNext()) {
        buff.append(" OR");
      }
    }
    buff.append(") ");
    return buff.toString();
  }
  
  // ********************   Start  User specific stuff here *****************  //
  /** The SQL string for creating the SocnetUser table. So named because 'User' is reserved. */
  private static final String createUserTableStatement = 
    "create table SocnetUser  "
    + "("
    + " Email VARCHAR(128) NOT NULL, "
    + " Password VARCHAR(128) NOT NULL, "
    + " Role CHAR(16), "
    + " XmlUser VARCHAR(32000) NOT NULL, "
    + " XmlUserRef VARCHAR(1000) NOT NULL, "
    + " LastMod TIMESTAMP NOT NULL, "
    + " PRIMARY KEY (Email) "
    + ")" ;
  
  /** An SQL string to test whether the User table exists and has the correct schema. */
  private static final String testUserTableStatement = 
    " UPDATE SocnetUser SET "
    + " Email = 'TestEmail@foo.com', " 
    + " Password = 'changeme', " 
    + " Role = 'basic', " 
    + " XmlUser = 'testXmlResource', "
    + " XmlUserRef = 'testXmlRef', "
    + " LastMod = '" + new Timestamp(new Date().getTime()).toString() + "' "
    + " WHERE 1=3";

  /** {@inheritDoc} */
  @Override
  public void deleteUser(String email) {
    String statement = "DELETE FROM SocnetUser WHERE Email='" + email + "'";
    deleteResource(statement);
  }

  /** {@inheritDoc} */
  @Override
  public String getUser(String email) {
    String statement = "SELECT XmlUser FROM SocnetUser WHERE Email = '" + email + "'";
    return getResource("User", statement);
  }


  /** {@inheritDoc} */
  @Override
  public String getUserIndex() {
    return getIndex("User", "SELECT XmlUserRef FROM SocnetUser");
  }

  /** {@inheritDoc} */
  @Override
  public boolean storeUser(User user, String xmlUser, String xmlUserRef) {
    Connection conn = null;
    PreparedStatement s = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.prepareStatement("INSERT INTO SocnetUser VALUES (?, ?, ?, ?, ?, ?)");
      // Order: Email Password Role XmlUser XmlUserRef LastMod
      s.setString(1, user.getEmail());
      s.setString(2, user.getPassword());
      s.setString(3, user.getRole());
      s.setString(4, xmlUser);
      s.setString(5, xmlUserRef);
      s.setTimestamp(6, new Timestamp(new Date().getTime()));
      s.executeUpdate();
      this.logger.fine("Derby: Inserted User" + user.getEmail());
    }
    catch (SQLException e) {
      if (DUPLICATE_KEY.equals(e.getSQLState())) {
        try {
          // Do an update, not an insert.
          s = conn.prepareStatement(
              "UPDATE SocnetUser SET "
              + " Password=?, " 
              + " Role=?, " 
              + " XmlUser=?, " 
              + " XmlUserRef=?, "
              + " LastMod=?"
              + " WHERE Email=?");
          s.setString(1, user.getPassword());
          s.setString(2, user.getRole());
          s.setString(3, xmlUser);
          s.setString(4, xmlUserRef);
          s.setTimestamp(5, new Timestamp(new Date().getTime()));
          s.setString(6, user.getEmail());
          s.executeUpdate();
          this.logger.fine("Derby: Updated User " + user.getEmail());
        }
        catch (SQLException f) {
          this.logger.info(derbyError + StackTrace.toString(f));
        }
      }
      else {
        this.logger.info(derbyError + StackTrace.toString(e));
      }
    }
    finally {
      try {
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
    return true;
  }

  // **************************** Internal helper functions *****************************
  
  /**
   * Returns a string containing the Index for the given resource indicated by resourceName.
   * @param resourceName The resource name, such as "Project". 
   * @param statement The SQL Statement to be used to retrieve the resource references.
   * @return The aggregate Index XML string. 
   */
  private String getIndex(String resourceName, String statement) {
    StringBuilder builder = new StringBuilder(512);
    builder.append("<").append(resourceName).append(indexSuffix);
    // Retrieve all the SensorData
    Connection conn = null;
    PreparedStatement s = null;
    ResultSet rs = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.prepareStatement(statement);
      rs = s.executeQuery();
      String resourceRefColumnName = xml + resourceName + "Ref";
      while (rs.next()) {
        builder.append(rs.getString(resourceRefColumnName));
      }
    }
    catch (SQLException e) {
      this.logger.info("Derby: Error in getIndex()" + StackTrace.toString(e));
    }
    finally {
      try {
        rs.close();
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
    builder.append("</").append(resourceName).append(indexSuffix);
    //System.out.println(builder.toString());
    return builder.toString();
  }
  
  /**
   * Returns a string containing the Index of all of the SensorData whose runtime field matches
   * the first runtime in the result set.  Since the passed statement will retrieve sensor
   * data in the given time period ordered in descending order by runtime, this should result
   * in an index containing only  
   * @param statement The SQL Statement to be used to retrieve the resource references.
   * @return The aggregate Index XML string. 
   */
  private String getSnapshotIndex(String statement) {
    String resourceName = "SensorData";
    StringBuilder builder = new StringBuilder(512);
    builder.append("<").append(resourceName).append(indexSuffix);
    // Retrieve all the SensorData
    Connection conn = null;
    PreparedStatement s = null;
    ResultSet rs = null;
    String firstRunTime = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.prepareStatement(statement);
      rs = s.executeQuery();
      String resourceRefColumnName = xml + resourceName + "Ref";
      boolean finished = false;
      // Add all entries with the first retrieved nruntime value to the index.
      while (rs.next() && !finished) {
        String runtime = rs.getString("Runtime");
        // Should never be null, but just in case. 
        if (runtime != null) {
          // Initial firstRunTime to the first retrieved non-null runtime value.
          if (firstRunTime == null) {
            firstRunTime = runtime;
          }
          // Now add every entry whose runtime equals the first retrieved run time.
          if (runtime.equals(firstRunTime)) {
            builder.append(rs.getString(resourceRefColumnName));
          }
          else {
            // As soon as we find a runtime not equal to firstRunTime, we can stop.
            finished = true;
          }
        }
      }
    }
    catch (SQLException e) {
      this.logger.info("Derby: Error in getIndex()" + StackTrace.toString(e));
    }
    finally {
      try {
        rs.close();
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
    builder.append("</").append(resourceName).append(indexSuffix);
    //System.out.println(builder.toString());
    return builder.toString();
  }
  
  /**
   * Returns a string containing the Index for the given resource indicated by resourceName, 
   * returning only the instances starting at startIndex, and with the maximum number of
   * returned instances indicated by maxInstances.   
   * @param resourceName The resource name, such as "Project".
   * @param startIndex The (zero-based) starting index for instances to be returned.
   * @param maxInstances The maximum number of instances to return.  
   * @param statement The SQL Statement to be used to retrieve the resource references.
   * @return The aggregate Index XML string. 
   */
  private String getIndex(String resourceName, String statement, int startIndex, int maxInstances) {
    StringBuilder builder = new StringBuilder(512);
    builder.append("<").append(resourceName).append(indexSuffix);
    // Retrieve all the SensorData to start.
    Connection conn = null;
    PreparedStatement s = null;
    ResultSet rs = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      s = conn.prepareStatement(statement);
      rs = s.executeQuery();
      int currIndex = 0;
      int totalInstances = 0;
      String resourceRefColumnName = xml + resourceName + "Ref";
      while (rs.next()) {
        if ((currIndex >= startIndex) && (totalInstances < maxInstances)) {
          builder.append(rs.getString(resourceRefColumnName));
          totalInstances++;
        }
        currIndex++;
      }
    }
    catch (SQLException e) {
      this.logger.info("Derby: Error in getIndex()" + StackTrace.toString(e));
    }
    finally {
      try {
        rs.close();
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
    builder.append("</").append(resourceName).append(indexSuffix);
    //System.out.println(builder.toString());
    return builder.toString();
  }
  
  /**
   * Returns a string containing the Resource as XML, or null if not found.
   * @param resourceName The name of the resource, such as "User".
   * @param statement The select statement used to retrieve the resultset containing a single
   * row with that resource.
   * @return The string containing the resource as an XML string.
   */
  private String getResource(String resourceName, String statement) {
    StringBuilder builder = new StringBuilder(512);
    Connection conn = null;
    PreparedStatement s = null;
    ResultSet rs = null;
    boolean hasData = false;
    try {
      conn = DriverManager.getConnection(connectionURL);
      server.getLogger().fine(executeQueryMsg + statement);
      s = conn.prepareStatement(statement);
      rs = s.executeQuery();
      String resourceXmlColumnName = xml + resourceName;
      while (rs.next()) { // the select statement must guarantee only one row is returned.
        hasData = true;
        builder.append(rs.getString(resourceXmlColumnName));
      }
    }
    catch (SQLException e) {
      this.logger.info("DB: Error in getResource()" + StackTrace.toString(e));
    }
    finally {
      try {
        rs.close();
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
    return (hasData) ? builder.toString() : null;
  }
  
  /**
   * Deletes the resource, given the SQL statement to perform the delete.
   * @param statement The SQL delete statement. 
   */
  private void deleteResource(String statement) {
    Connection conn = null;
    PreparedStatement s = null;
    try {
      conn = DriverManager.getConnection(connectionURL);
      server.getLogger().fine("Derby: " + statement);
      s = conn.prepareStatement(statement);
      s.executeUpdate();
    }
    catch (SQLException e) {
      this.logger.info("Derby: Error in deleteResource()" + StackTrace.toString(e));
    }
    finally {
      try {
        s.close();
        conn.close();
      }
      catch (SQLException e) {
        this.logger.warning(errorClosingMsg + StackTrace.toString(e));
      }
    }
  }

    public void shutdown()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
