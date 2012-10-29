/* Class name: FormatDocStore
 * File name:  FormatDocStore.java
 * Created:    08-Apr-2008 16:35:19
 * Modified:   08-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  08-Apr-2008 Initial build
 */

package mars.mars.object;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class is used to store and retrieve FormatDocs used within MARS. All FormatDocs
 * should be named uniquely and so this class uses a Hashtable to store <code>FormatDoc
 * </code> classes that have been created through XML parsing against their name
 * (obtained from the file name).
 * <p>Instances of <code>FormatDoc</code> can then be retrieved through this class
 * using methods similar to interacting with a Hashtable, however this class logs any
 * transactions along with results and any objects passing through.
 * @version 0.001
 * @author Alex Harris (W4786241)
 * @see java.util.Hashtable
 */
public class FormatDocStore
{
  private static final String parentClassName = "mars.mars.object.FormatDocStore";
  private static Logger log;
  private static Hashtable htFD;
  private static FormatDoc fdTemp;
  
  /**
   * Initialises the class and returns to the receiver
   */
  public FormatDocStore()
  {
    if (log == null)
    {
      log = LoggerFactory.getLogger(parentClassName);
      log.finest("Initialising FormatDocStore");
    }
    if (htFD == null)
    {
      htFD = new Hashtable();
    }
  }
  
  /**
   * This method allows a XML DocumentHandler to work with a FormatDoc and save it here once completed parsing
   * @param tempFD
   */
  public static void putTempFD(FormatDoc newTempFD)
  {
    fdTemp = newTempFD;
  }
  
  /**
   * This allows a parser to obtain the completed FormatDoc once the DocumentHandler has finished parsing the raw XML
   * @return The FormatDoc built through XML parsing
   */
  public static FormatDoc getTempFD()
  {
    return fdTemp;
  }
  
  /**
   * Moves the temporary FormatDoc (the one just parsed from a XML file) to the main hashtable using the supplied
   * string as the key.
   * @param system The system name to be used as the key for the parsed FormatDoc
   */
  public static void tempToHashTable(String system)
  {
    put(system, fdTemp);
  }
  
  /**
   * Searches the hashtable for the supplied system name and returns a boolean value
   * based on if the string can be found or not.
   * @param system The requested system name as a String
   * @return Whether or not the string cn be found in the hashtable keys
   */
  public static boolean containsKey(String system)
  {
    log.finest("Checking for key: " + system);
    if (htFD.containsKey(system))
    {
      log.finest("Found entry in the hashtable");
      return true;
    }
    else
    {
      log.finest("Could not find entry in the hastable");
      return false;
    }
  }
  
  /**
   * Returns the instance of <code>FormatDoc</code> stored within the hashtable under
   * the supplied system name.
   * @param system The system name to find the corresponding FormatDoc
   * @return The requested FormatDoc
   */
  public static FormatDoc get(String system)
  {
    log.finest("Retrieving FormatDoc for system: " + system);
    FormatDoc fdSystem = ((FormatDoc) htFD.get(system));
    log.log(Level.FINEST,"Found FormatDoc", fdSystem);
    return fdSystem;
  }
  
  /**
   * Returns all of the keys within the hashtable as an Enumeration
   * @return All of the keys within the hashtable
   * @see java.util.Enumeration
   */
  public static Enumeration keys()
  {
    log.finest("Returning the keys of the hashtable");
    return htFD.keys();
  }
  
  /**
   * Adds a new <code>FormatDoc</code> to the hashtable with the supplied system name
   * @param system
   * @param fd
   */
  public static void put(String system, FormatDoc fd)
  {
    log.log(Level.FINEST, "Adding FormatDoc to the hashtable for system: " + system, fd);
    htFD.put(system, fd);
  }
  
  /**
   * Returns the size of the hashtable
   * @return The hashtable size
   */
  public static int size()
  {
    log.finest("Returning the size of the hashtable, which is :" + htFD.size());
    return htFD.size();
  }
}