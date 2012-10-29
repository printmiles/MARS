/* Class name: DataConnectionManager
 * File name:  DataConnectionManager.java
 * Created:    22-Jul-2008 12:36:36
 * Modified:   28-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  28-Jul-2008 Added method exists(String) to check for a name within the hashtable.
 * 0.001  22-Jul-2008 Initial build
 */

package mars.deimos.reporting;
import java.util.*;
import java.util.logging.*;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.object.thread.*;

/**
 * This class is used to monitor data collection threads within Deimos. This class uses a pair of hashtables which are accessed
 * simultaneously where needed to provide the thread and/or status. Threads stored in this class should implement the <code>
 * mars.deimos.object.thread.DataThread</code> interface, this ensures that the threads are stopped before removing them from
 * the hashtable.
 * <p>This class is intended to be used with GUI windows allowing the user to start, pause and stop thread execution and their
 * associated data collection as well as allowing the COM port configuration window to dynamically add or remove COM port
 * listeners as requested by the user. This class should be loaded with the application startup and remain in memory for the
 * whole time Deimos is executing.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class DataConnectionManager
{
  private static final String parentClassName = "mars.deimos.reporting.DataConnectionManager";
  private static Logger log;
  private static Hashtable htThreads;
  private static Hashtable htStatus;
  
  /** The default constructor for the DataConnectionManager class */
  public DataConnectionManager()
  {
    log = LoggerFactory.getLogger(parentClassName);
    htThreads = new Hashtable();
    htStatus = new Hashtable();
    log.finest("Variables initialised");
  }
  
  /**
   * Provides a means for new threads implementing <code>mars.deimos.object.thread.DataThread</code> to be added to
   * the central hashtable for later retrieval.
   * @param name The name or purpose of the thread (this is used as a unique identifier)
   * @param newThread The thread to be added which implements the required interface
   * @param status The status of the thread that is being added
   */
  public static void add(String name, DataThread newThread, String status)
  {
    log.finest("Adding thread for " + name + " with a status of: " + status);
    htThreads.put(name, newThread);
    htStatus.put(name, status);
  }
  
  /**
   * Remove the thread from the hashtables. The thread will call the <code>stop()</code> method of the
   * <code>DataThread</code> interface to ensure that it has been shut down before removing it from the hashtable.
   * @param name The thread to be removed
   */
  public static void remove(String name)
  {
    log.fine("Stopping Thread: " + name);
    DataThread dt = (DataThread) htThreads.get(name);
    dt.stop();
    log.finest("Removing thread: " + name);
    htThreads.remove(name);
    htStatus.remove(name);
  }
  
  /**
   * Remove all of the threads stored within the class by individually calling the stop() for each key
   */
  public static void removeAll()
  {
    log.fine("Closing all threads contained in the hashtable");
    Enumeration enumKeys = getKeys();
    while (enumKeys.hasMoreElements())
    {
      String aKey = (String) enumKeys.nextElement();
      log.fine("Closing thread for " + aKey);
      remove(aKey);
    }
  }
  
  /**
   * Returns an enumeration of the keys contained within the class
   * @return All of the keys contained within the class
   */
  public static Enumeration getKeys()
  {
    log.finest("Returning keys");
    return htThreads.keys();
  }
  
  /**
   * Returns the thread stored under the given key
   * @param name The thread identifier
   * @return The thread itself
   */
  public static DataThread getThread(String name)
  {
    log.finest("Returning the object: " + name);
    return (DataThread) htThreads.get(name);
  }
  
  /**
   * Returns the status of the specified thread
   * @param name The thread identifier
   * @return The thread status
   */
  public static String getStatus(String name)
  {
    log.finest("Obtaining status for: " + name);
    return (String) htStatus.get(name);
  }
  
  /**
   * Updates the central hashtable with a new thread instance
   * @param name The requested thread identifier
   * @param thread The new thread
   */
  public static void putThread(String name, DataThread thread)
  {
    log.fine("Updating thread: " + name);
    htThreads.put(name, thread);
  }
  
  /**
   * Updates the central hashtable with the status for a specified thread
   * @param name The requested thread
   * @param status The new status
   */
  public static void putStatus(String name, String status)
  {
    log.fine("Changing status of thread (" + name + ") to: " + status);
    htStatus.put(name, status);
  }
  
  /**
   * Used to check whether the given name argument exists within the hashtable and returns a boolean value based on the outcome.
   * @param name The thread name in the hashtable
   * @return Whether or not it is present
   */
  public static boolean exists(String name)
  {
    return htThreads.containsKey(name);
  }
}