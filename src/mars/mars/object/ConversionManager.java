/* Class name: ConversionManager
 * File name:  ConversionManager.java
 * Created:    30-May-2008 22:02:39
 * Modified:   01-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  01-Jun-2008 Added code and methods
 * 0.001  30-May-2008 Initial build
 */

package mars.mars.object;
import java.util.*;

/**
 * This class is used to store Conversions added to an attribute through the FDEditor screens.
 * As the conversions maybe provided for multiple attributes in multiple domains, the user-supplied
 * data is mapped directly into a hashtable (as the conversions should use a unique identifier and
 * only have one conversion to perform) which is in turn mapped into a hashtable within this class
 * using a suitable identifier to retrieve the required set of conversions.
 * As this class utilises a single (static) hashtable then suitably unique identifiers should be used
 * such as <code>DSNName:DomainName:AttributeName</code> using a consistent naming and formatting
 * scheme for the identifier.
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @see java.util.Hashtable
 */
public class ConversionManager
{
  private static Hashtable htConversions;
  
  /**
   * Adds a new key-value pair to the hashtable for later retrieval.
   * @param key A string of suitable uniqueness to identify the supplied value.
   * @param value A Hashtable containing the original and converted values for an attribute.
   */
  public static void addKey(String key, Hashtable value)
  {
    htConversions.put(key, value);
  }
  
  /**
   * Removes the value from the hashtable specified by the supplied key.
   * @param key The value to remove from the hashtable
   */
  public static void removeKey(String key)
  {
    htConversions.remove(key);
  }
  
  /**
   * Returns the hashtable contained within the class' hashtable at the named key.
   * @param key The key of the hashtable to be returned.
   * @return The named hashtable
   */
  public static Hashtable getValue(String key)
  {
    return (Hashtable) htConversions.get(key);
  }
  
  /**
   * Returns an <code>Enumeration</code> containing all of the keys stored within
   * the master hashtable
   * @return All of the contained keys
   */
  public static Enumeration returnKeys()
  {
    return htConversions.keys();
  }
  
  /**
   * Resets the contents of the master class' hashtable
   */
  public static void resetConversions()
  {
    htConversions = new Hashtable();
  }
}