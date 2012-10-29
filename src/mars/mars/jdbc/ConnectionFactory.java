/* Class name: ConnectionFactory
 * File name:  ConnectionFactory.java
 * Created:    28-Apr-2008 11:41:08
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  09-Jun-2008 Added an additional consturctor to allow for the creation of user-specific connections
 *        accepting a user name and password.
 * 0.003  03-May-2008 Added additional methods for retrieving the hashtable keys and size
 * 0.002  02-May-2008 Added static methods for adding and retrieving from the hashtable
 * 0.001  28-Apr-2008 Initial build
 */

package mars.mars.jdbc;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class is used to configure instances of <code>Connection</code> used to obtain
 * data from an ODBC compliant data source. Due to the limitations of ODBC, instances are
 * stored within a hashtable to allow for retrieval of an already configured data source.
 * @version 0.004
 * @author Alex Harris (W4786241)
 * @see java.sql.Connection
 */
public class ConnectionFactory
{
  private static Hashtable htConnections;
  private static Logger log;
  private static final String parentClassName = "mars.mars.jdbc.ConnectionFactory";
  private static final String odbcDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  private static final String odbcDSNPrefix = "jdbc:odbc:";
  
  /**
   * Adds a new entry to the hashtable of connections used by the application.
   * @param id The system name used as a unique identifier
   * @param con The connection used to access ODBC data for the given system
   */
  public static void putConnection(String id, Connection con)
  {
    log.log(Level.CONFIG,"Adding connection to the hashtable for connection " + id,con);
    htConnections.put(id, con);
  }
  
  /**
   * Returns the connection from the hastable given the system name provided as the identifier.
   * <p>WARNING: This method will return null if an error is encountered internally.
   * @param id The unique system identifier
   * @return The connection relating to the system
   */
  public static Connection getConnection(String id)
  {
    Connection con;
    if (htConnections == null)
    {
      htConnections = new Hashtable();
      log = LoggerFactory.getLogger(parentClassName);
      log.finest("Logger created for ConnectionFactory class");
    }
    if (htConnections.contains(id))
    {
      return (Connection) htConnections.get(id);
    }
    else
    {
      try
      {
        Class.forName(odbcDriver);
        con = DriverManager.getConnection(odbcDSNPrefix + id);
        log.finest("Returning connection for system " + id);
        putConnection(id, con);
        return con;
      }
      catch (ClassNotFoundException cnfX)
      {
        log.finer("ClassNotFoundException: Unable to load database driver class");
        log.throwing("ODBCInterface", "getConnection", cnfX);
        return null;
      }
      catch (SQLException sqlX)
      {
        log.finer("SQL exception detected");
        log.throwing("ODBCInterface", "getConnection", sqlX);
        return null;
      }
      catch (Exception eX)
      {
        log.finer("General exception detected");
        log.throwing("ODBCInterface", "getConnection", eX);
        return null;
      }
    }
  }
  
  /**
   * Returns the connection from the hastable given the system name provided as the identifier
   * and uses the supplied login information if a user is required to login.
   * <p>WARNING: This method will return null if an error is encountered internally.
   * @param id The unique system identifier
   * @param name The database user name
   * @param pw The user password to access the system
   * @return The connection relating to the system
   */
  public static Connection getConnection(String id, String name, String pw)
  {
    Connection con;
    if (htConnections == null)
    {
      htConnections = new Hashtable();
      log = LoggerFactory.getLogger(parentClassName);
      log.finest("Logger created for ConnectionFactory class");
    }
    if (htConnections.contains(id))
    {
      return (Connection) htConnections.get(id);
    }
    else
    {
      try
      {
        Class.forName(odbcDriver);
        con = DriverManager.getConnection(odbcDSNPrefix + id,name,pw);
        pw = null;
        log.finest("Returning connection for system " + id);
        log.finest("Accessed the database using the user name of: " + name);
        putConnection(id, con);
        return con;
      }
      catch (ClassNotFoundException cnfX)
      {
        log.finer("ClassNotFoundException: Unable to load database driver class");
        log.throwing("ODBCInterface", "getConnection", cnfX);
        return null;
      }
      catch (SQLException sqlX)
      {
        log.finer("SQL exception detected");
        log.throwing("ODBCInterface", "getConnection", sqlX);
        return null;
      }
      catch (Exception eX)
      {
        log.finer("General exception detected");
        log.throwing("ODBCInterface", "getConnection", eX);
        return null;
      }
    }
  }
  
  /**
   * Returns the size of the connections hashtable
   * @return The integer value of the hashtable size
   */
  public static int getConnectionsAmount()
  {
    return htConnections.size();
  }
  
  /**
   * Returns an enumeration of the Connections hashtable keys.
   * @return An instance of Enumeration that contains the keys from the connections hashtable
   * @see java.util.Enumeration
   */
  public static Enumeration getConnectionsKeys()
  {
    return htConnections.keys();
  }
}