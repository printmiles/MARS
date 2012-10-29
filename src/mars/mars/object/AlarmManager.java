/* Class name: AlarmManager
 * File name:  AlarmManager.java
 * Created:    30-May-2008 22:02:57
 * Modified:   01-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  01-Jun-2008 Added code and methods
 * 0.001  30-May-2008 Initial build
 */

package mars.mars.object;
import java.util.*;

/**
 * This class is used to store Alarms added to an attribute through the FDEditor screens.
 * The alarms are built using a dedicated GUI and stored in here before they are added to an instance of FormatDoc.
 * As this class utilises a single (static) hashtable then suitably unique identifiers should be used
 * such as <code>DSNName:DomainName:AttributeName</code> using a consistent naming and formatting
 * scheme for the identifier.
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @see java.util.Hashtable
 */
public class AlarmManager
{
  private static Hashtable htAlarms;
  
  /**
   * Adds a new key-value pair to the hashtable for later retrieval.
   * @param key A string of suitable uniqueness to identify the supplied value.
   * @param value An Alarm containing all of the required information.
   */
  public static void addKey(String key, Alarm value)
  {
    htAlarms.put(key, value);
  }
  
  /**
   * Removes the value from the hashtable specified by the supplied key.
   * @param key The value to remove from the hashtable
   */
  public static void removeKey(String key)
  {
    htAlarms.remove(key);
  }
  
  /**
   * Returns the Alarm contained within the class' hashtable at the named key.
   * @param key The key of the hashtable to be returned.
   * @return The named Alarm
   */
  public static Alarm getValue(String key)
  {
    return (Alarm) htAlarms.get(key);
  }
  
  /**
   * Returns an <code>Enumeration</code> containing all of the keys stored within
   * the master hashtable
   * @return All of the contained keys
   */
  public static Enumeration returnKeys()
  {
    return htAlarms.keys();
  }
  
  /**
   * Resets the contents of the master class' hashtable
   */
  public static void resetConversions()
  {
    htAlarms = new Hashtable();
  }
}