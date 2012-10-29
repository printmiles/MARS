/* Class name: Column
 * File name:  Column.java
 * Created:    01-Apr-2008 21:29:20
 * Modified:   03-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  03-Apr-2008 Added JDoc and comments
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.object;

/**
 * This class is used to represent one or more columns within a FormatDoc.
 * It is used in preference to the table (and attribute) class if the datasource
 * that is being used is a text file or a COM port.
 * As this class only contains accessory pairs and no complex methods, there is
 * no logging provided for instances of this class.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class Column
{
  private int iFrom;
  private int iTo;
  private String sName;
  private String sType;
  
  /**
   * Instantiates the class with default values and returns to the receiver.
   */
  public Column()
  {
    // Set default values
    setFrom(1);
    setTo(2);
    setName("aFieldName");
    setType("String");
  }
  
  /**
   * Returns the position a column starts from
   * @return The starting character position of a column
   */
  public int getFrom()
  {
    return iFrom;
  }
  
  /**
   * Returns the position the column ends
   * @return The ending character position of a column
   */
  public int getTo()
  {
    return iTo;
  }
  
  /**
   * Returns the name of the column
   * @return The column name
   */
  public String getName()
  {
    return sName;
  }
  
  /**
   * Returns the columns data type
   * @return The data type of the column
   */
  public String getType()
  {
    return sType;
  }
  
  /**
   * Sets the starting position of the column
   * @param newFrom The column starting position
   */
  public void setFrom(int newFrom)
  {
    iFrom = newFrom;
  }
  
  /**
   * Sets the ending position of the column
   * @param newTo The column ending position
   */
  public void setTo(int newTo)
  {
    iTo = newTo;
  }
  
  /**
   * Sets the column's name
   * @param newName The column name
   */
  public void setName(String newName)
  {
    sName = newName;
  }
  
  /**
   * Sets the column's type
   * @param newType The column type
   */
  public void setType(String newType)
  {
    sType = newType;
  }
}