/* Class name: FormatDoc
 * File name:  FormatDoc.java
 * Created:    31-Mar-2008 12:06:07
 * Modified:   14-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.005  14-Apr-2008 Added getTableSize, getColumnSize, getQuerySize, getTableKeys,
 *        getColumnKeys, getQueryKeys, getTable(String), getColumn(String), getQuery(String)
 *        and getAllAlarms methods
 * 0.004  04-Apr-2008 Added addQuery, removeQuery, addAlarm and removeAlarm methods
 * 0.003  03-Apr-2008 Added JDoc, comments and dependant classes
 * 0.002  01-Apr-2008 Added class content
 * 0.001  31-Mar-2008 Initial build
 */

package mars.mars.object;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class is used to represent a parsed XML FormatDoc containing information about
 * a data source, its structure and user preferences. These preferences can relate to
 * conversions that should be performed within the table, alarms that should be raised
 * should certain conditions evaluate to true and queries that should be made available
 * for the user to select from.
 * @version 0.005
 * @author Alex Harris (W4786241)
 */
public class FormatDoc
{
  private String sDestination;
  private String sProtocol;
  private String sDataSource;
  private Hashtable htTables;
  private Hashtable htColumns;
  private Hashtable htQueries;
  private Vector vecAlarms;
  
  /**
   * Creates a new instance of the class by initialising all internal variables 
   * and returns it to the receiver.
   */
  public FormatDoc()
  {
    // Set default values
    setDestination("127.0.0.1");
    setProtocol("JDBC");
    setDataSourceName("#");
    htTables = new Hashtable();
    htColumns = new Hashtable();
    htQueries = new Hashtable();
    vecAlarms = new Vector();
  }
  
  /**
   * Returns the destination MARS should connect to this can be either in the form
   * of an IP address or DNS name.
   * @return The destination a connection should be established to.
   */
  public String getDestination()
  {
    return sDestination;
  }
  
  /**
   * Sets the destination of a MARS connection. This would be a remote host which has
   * a data source that cannot be accessed directly from the MARS server.
   * @param newDestination The IP address or DNS name of the destination
   */
  public void setDestination(String newDestination)
  {
    sDestination = newDestination;
  }
  
  /**
   * Returns the protocol that should be used to connect from the destination to the
   * required data source.
   * @return The protocol to be used to connect to the data source
   */
  public String getProtocol()
  {
    return sProtocol;
  }
  
  /**
   * Set the protocol to connect to the data source from the specified destination.
   * Supported protocols are JDBC, Serial or Deimos.
   * @param newProtocol The new protocol to be set
   */
  public void setProtocol(String newProtocol)
  {
    sProtocol = newProtocol;
  }
  
  /**
   * Returns the data source name that MARS should connect to in conjunction with
   * the protocol and destination.
   * @return The data source name
   */
  public String getDataSourceName()
  {
    return sDataSource;
  }
  
  /**
   * Set the data source name that should be connected to.
   * @param newDataSourceName The new data source
   */
  public void setDataSourceName(String newDataSourceName)
  {
    sDataSource = newDataSourceName;
  }
  
  /**
   * Places a new table within the hashtable
   * @param tableID The id of the table
   * @param newTable The table instance
   */
  public void addTable(String tableID, Table newTable)
  {
    htTables.put(tableID, newTable);
  }
  
  /**
   * Removes the table instance for a table with the supplied ID
   * @param tableID The table ID of the table to remove
   */
  public void removeTable(String tableID)
  {
    htTables.remove(tableID);
  }
  
  /**
   * Places a new column within the hashtable. It requires an ID to add and retrieve
   * the relevant column which must be used consistently for add and remove functions.
   * @param sID The columnID 
   * @param newColumn The column instance
   */
  public void addColumn(String sID, Column newColumn)
  {
    htColumns.put(sID, newColumn);
  }
  
  /**
   * Removes the column instance for a column with the supplied ID
   * @param columnID The ID of the column to remove
   */
  public void removeColumn(String columnID)
  {
    htColumns.remove(columnID);
  }
  
  /**
   * Places a new query within the hashtable
   * @param queryID The id of the query
   * @param newQuery The query instance
   */
  public void addQuery(String queryID, Query newQuery)
  {
    htQueries.put(queryID, newQuery);
  }
  
  /**
   * Removes the query instance for a query with the supplied ID
   * @param queryID The ID of the query to be removed from the hashtable
   */
  public void removeQuery(String queryID)
  {
    htQueries.remove(queryID);
  }
  
  /** 
   * Adds a new alarm to the Alarm vector
   * @param newAlarm The new alarm to be added
   */
  public void addAlarm(Alarm newAlarm)
  {
    vecAlarms.add(newAlarm);
  }
  
  /**
   * Removes the supplied alarm from the array
   * @param alarmToBeRemoved The alarm instance to be removed
   */
  public void removeAlarm(Alarm alarmToBeRemoved)
  {
    int iPosOfWantedAlarm = -1;
    for (int i = 0; i < vecAlarms.size(); i++)
    {
      if (((Alarm) vecAlarms.get(i)).equals(alarmToBeRemoved))
      {
        // If this is true then record the position where it occurred.
        iPosOfWantedAlarm = i;
      }
    }
    
    if (iPosOfWantedAlarm > -1)
    {
      // If we found something then this will evaluate to true as the lowest value we can have here is 0.
      vecAlarms.remove(iPosOfWantedAlarm);
    }
  }
  
  /**
   * Returns all of the alarms currently configured within the FormatDoc. The alarms are stored within a Vector
   * and are not in any particular order.
   * @return The vector containing configured alarms
   */
  public Vector getAllAlarms()
  {
    return vecAlarms;
  }
  
  /**
   * Returns the size of the Tables hashtable
   * @return The number of tables within the FormatDoc
   */
  public int getTableSize()
  {
    return htTables.size();
  }
  
  /**
   * Returns the size of the Columns hashtable
   * @return The number of columns within the FormatDoc
   */
  public int getColumnSize()
  {
    return htColumns.size();
  }
  
  /** 
   * Returns the size of the Query hashtable
   * @return The number of queries within the FormatDoc
   */
  public int getQuerySize()
  {
    return htQueries.size();
  }
  
  /**
   * Returns an <code>Enumeration</code> of the keys used within the Tables hashtable
   * @return The list of keys within the tables hashtable.
   */
  public Enumeration getTableKeys()
  {
    return htTables.keys();
  }
  
  /**
   * Returns an <code>Enumeration</code> of the keys used within the Columns hashtable
   * @return The list of keys within the columns hashtable.
   */
  public Enumeration getColumnKeys()
  {
    return htColumns.keys();
  }
  
  /**
   * Returns an <code>Enumeration</code> of the keys used within the Queries hashtable
   * @return The list of keys within the queries hashtable.
   */
  public Enumeration getQueryKeys()
  {
    return htQueries.keys();
  }
  
  /**
   * Returns a Table given an ID from the hashtable
   * @param aKey The identifier of the required table
   * @return  The requested table stored within the hashtable
   */
  public Table getTable(String aKey)
  {
    return (Table) htTables.get(aKey);
  }
  
  /**
   * Returns a Column given an ID from the hashtable
   * @param aKey The identifier of the required column
   * @return  The requested column stored within the hashtable
   */
  public Column getColumn(String aKey)
  {
    return (Column) htColumns.get(aKey);
  }
  
  /**
   * Returns a Query given an ID from the hashtable
   * @param aKey The identifier of the required query
   * @return  The requested query stored within the hashtable
   */
  public Query getQuery(String aKey)
  {
    return (Query) htQueries.get(aKey);
  }
}