/* Class name: InterrogateDataSource
 * File name:  InterrogateDataSource.java
 * Created:    12-May-2008 12:06:55
 * Modified:   19-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  19-May-2008 Added support for the FormatDocBuilder to handle the hashtable
          generated from this event. This then allows the user to edit the table info
 * 0.001  12-May-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.*;
import mars.mars.gui.*;
import mars.mars.jdbc.*;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class InterrogateDataSource implements ActionListener
{
  private DataSourceWindow dswParent;
  
  public InterrogateDataSource(DataSourceWindow parent)
  {
    dswParent = parent;
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    JDBCInterrogator jdbcI = new JDBCInterrogator();
    String dataSourceName = dswParent.getTextField();
    Hashtable htMetaData = jdbcI.interrogateDataSource(dataSourceName);
    try
    {
      dswParent.setClosed(true); // Close the DataSourceWindow
    }
    catch (Exception x)
    {
      System.err.println("Caught an Exception in mars.mars.events.InterrogateDataSource.actionPerformed(ActionEvent)");
      x.printStackTrace();
    }
    dswParent.setVisible(false);
    dswParent = null;
    /**************************************************************************/
    /* The hashtable returned from the above method will be in this format:   */
    /*   Hashtable                                                            */
    /*     |-> String (table name acting as identifier)                       */
    /*     |-> GenericTable                                                   */
    /**************************************************************************/
    FormatDocBuilder fdbChild = new FormatDocBuilder(htMetaData,dataSourceName);
  }
}