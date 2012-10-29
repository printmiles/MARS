/* Class name: Table
 * File name:  Table.java
 * Created:    01-Apr-2008 21:28:49
 * Modified:   22-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  22-Apr-2008 Added getAttribute(String) method
 * 0.003  14-Apr-2008 Added support for rentention tag.
 * 0.002  02-Apr-2008 Added class content
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.object;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class is used by the FormatDoc class to hold information from a parsed XML FormatDoc.
 * As this class doesn't contain any complex operations and mainly accessor pairs there is no
 * logging provided for instances of this class.
 * @version 0.004
 * @author Alex Harris (W4786241)
 */
public class Table
{
  private String sName;
  private String sRetentionUnits;
  private int iRetentionValue;
  private Hashtable attributes;
  
  /**
   * Instantiates the class with default values and returns to the receiver.
   */
  public Table()
  {
    // Give the table name a default value and initialise the Hashtable for attributes
    setName("aTableName");
    
    attributes = new Hashtable();
  }
  
  /**
   * Provides the table's name
   * @return The table name
   */
  public String getName()
  {
    return sName;
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
   * Returns the units used for the table's retention value, days, months etc
   * @return The units used for data retention from this table
   */
  public String getRetentionUnits()
  {
    return sRetentionUnits;
  }
  
  /**
   * Sets the units for data retention period. Valid values are:
   * <ul>
   *   <li>hour</li>
   *   <li>day</li>
   *   <li>month</li>
   *   <li>year</li>
   * </ul>
   * @param newUnits The type of units for the retention value
   */
  public void setRetentionUnits(String newUnits)
  {
    newUnits = newUnits.toLowerCase(); // Convert to lowercase just to be safe
    if ((newUnits.equals("hour")) ||
        (newUnits.equals("day")) ||
        (newUnits.equals("month")) ||
        (newUnits.equals("year")))
    {
      sRetentionUnits = newUnits;
    }
  }
  
  /**
   * Returns the value for data retention for this table. Should be used in conjunction
   * with <code>getRetentionUnits()</code>
   * @return The value for data retention from this table
   */
  public int getRetentionValue()
  {
    return iRetentionValue;
  }
  
  /**
   * Sets the retention value. This is used to dictate how long the data for this table
   * is held onto within the MARS database.
   * @param newValue The new value to be stored for data retention
   */
  public void setRetentionValue(int newValue)
  {
    iRetentionValue = newValue;
  }      
  
  /**
   * The size of the attribute hashtable
   * @return The size of the attribute hashtable
   */
  public int numberOfAttributes()
  {
    return attributes.size();
  }
  
  /**
   * An <code>Enumeration</code> of the keys for attributes within the table.
   * These are provided from the attribute id within the FormatDoc
   * @return The keys of attributes witin the table
   * @see java.util.Enumeration
   */
  public Enumeration getAttributeKeys()
  {
    return attributes.keys();
  }
  
  /**
   * Places a new attribute within the hashtable. The attribute class is built
   * from the attribute element within the FormatDoc and contains data from sub-elements.
   * @param key The attribute ID
   * @param value The corresponding attribute class
   */
  public void putAttribute(String key, Attribute value)
  {
    attributes.put(key, value);
  }
  
  public Attribute getAttribute(String key)
  {
    return (Attribute) attributes.get(key);
  }
}