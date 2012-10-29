/* Class name: Query
 * File name:  Query.java
 * Created:    01-Apr-2008 21:29:35
 * Modified:   02-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  02-Apr-2008 Added class content and JDoc
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.object;

/**
 * This class is used to represent one or more queries within a FormatDoc.
 * As this class only contains accessory pairs and no complex methods, there is
 * no logging provided for instances of this class.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class Query
{
  private String sID;
  private String sName;
  private String sStatement;
  
  /**
   * Instantiates the class with default values and returns to the receiver.
   */
  public Query()
  {
    // Sets new instances of the class to default values
    setID("1");
    setName("A New Query");
    setStatement("SELECT * FROM aTable");
  }
  
  /**
   * Provides the ID of the query
   * @return Returns the query ID
   */
  public String getID()
  {
    return sID;
  }
  
  /**
   * Sets the query ID to the supplied value
   * @param newID The new ID for the query
   */
  public void setID(String newID)
  {
    sID = newID;
  }
  
  /**
   * Returns the name of the query to be used within the GUI
   * @return Returns the query name
   */
  public String getName()
  {
    return sName;
  }
  
  /**
   * Sets the name of the query to the new value
   * @param newName The new name to assign to this query
   */
  public void setName(String newName)
  {
    sName = newName;
  }
  
  /**
   * Returns the stored SQL statement
   * @return The SQL statement associated with this query
   */
  public String getStatement()
  {
    return sStatement;
  }
  
  /**
   * Sets the SQL statement of the query
   * @param newStatement The new SQL statement to be used with this query
   */
  public void setStatement(String newStatement)
  {
    sStatement = newStatement;
  }
}