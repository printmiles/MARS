/* Class name: Attribute
 * File name:  Attribute.java
 * Created:    01-Apr-2008 21:28:32
 * Modified:   02-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  02-Apr-2008 Added numberOfConversions method
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.object;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class is used by the table class to store information about attributes (fields)
 * within a table (domain) contained by a given datasource.
 * As the class performs no complex operations and contains mainly accessor pairs
 * there is no logging provided.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class Attribute
{
  private String sID;
  private String sName;
  private String sType;
  private boolean bReport;
  private Hashtable htConversions;
  
  /**
   * Instantiates the class with default values and returns to the receiver.
   */
  public Attribute()
  {
    // Set default values
    setID("1");
    setName("aFieldName");
    setReported(true);
    htConversions = new Hashtable();
  }
  
  /**
   * Provides the attribute's ID
   * @return The attribute ID
   */
  public String getID()
  {
    return sID;
  }
  
  /**
   * Provides the attribute's name
   * @return The attribute name
   */
  public String getName()
  {
    return sName;
  }
  
  /**
   * Provides the attribute's type
   * @return The attribute type
   */
  public String getType()
  {
    return sType;
  }
  
  /**
   * Whether the attribute should be reported on
   * @return Whether the attribute should be reported on
   */
  public boolean isReported()
  {
    return bReport;
  }
  
  /**
   * An <code>Enumeration</code> of the keys for conversions to be performed on
   * this attribute. These are provided from the raw attribute of the convert element
   * within the FormatDoc
   * @return The conversion keys of values to be replaced
   * @see java.util.Enumeration
   */
  public Enumeration getConversionKeys()
  {
    return htConversions.keys();
  }
  
  /**
   * Returns a value of a conversion to occur within this attribute. This is provided
   * through the contents of the convert element and replaces the value of the
   * corresponding hashtable key.
   * @param key The unique conversion key to be found within the hastable
   * @return The value to be substituted for the supplied key 
   */
  public Object getConversionValue(String key)
  {
    return htConversions.get(key);
  }
  
  /**
   * The size of the conversions hashtable
   * @return The size of the conversions hashtable
   */
  public int numberOfConversions()
  {
    return htConversions.size();
  }
  
  /**
   * Sets the ID to the supplied String
   * @param newID The new ID
   */
  public void setID(String newID)
  {
    sID = newID;
  }
  
  /**
   * Sets the name to the supplied String
   * @param newName The new name
   */
  public void setName(String newName)
  {
    sName = newName;
  }
  
  /**
   * Sets the type to the supplied String
   * @param newType The new type
   */
  public void setType(String newType)
  {
    sType = newType;
  }
  
  /**
   * Sets whether the attribute should be reported on (visible as a column within MARS)
   * @param reported Whether the attribute should be reported on
   */
  public void setReported(boolean reported)
  {
    bReport = reported;
  }
  
  /**
   * Places a new conversion within the hashtable. The raw attribute is used as the key
   * and represents the value to be found within the attribute. The value of the convert
   * element is added as the value of the hashtable and is replaced once a corresponding
   * key has been found.
   * @param key The value to be found
   * @param value The substitute value
   */
  public void putConversion(String key, String value)
  {
    htConversions.put(key, value);
  }
}