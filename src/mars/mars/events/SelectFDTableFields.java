/* Class name: SelectFDTableFields
 * File name:  SelectFDTableFields.java
 * Created:    21-May-2008 13:10:05
 * Modified:   21-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  21-May-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.*;
import mars.mars.gui.*;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class SelectFDTableFields implements ActionListener
{
  private FormatDocBuilder fdbParent;
  private Hashtable selectedFields;
  private String dsnName;
  
  public SelectFDTableFields(FormatDocBuilder parent, Hashtable htFields, String name)
  {
    fdbParent = parent;
    selectedFields = htFields;
    dsnName = name;
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      fdbParent.setClosed(true); // Close the DataSourceWindow
    }
    catch (Exception x)
    {
      System.err.println("Caught an Exception in mars.mars.events.SelectFDTableFields.actionPerformed(ActionEvent)");
      x.printStackTrace();
    }
    fdbParent.setVisible(false);
    fdbParent = null;
    
    FDEditor fde = new FDEditor(dsnName, selectedFields);
  }
}