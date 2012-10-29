/* Class name: FDBuilder_AddConversionsandAlarms
 * File name:  FDBuilder_AddConversionsandAlarms.java
 * Created:    23-May-2008 17:46:01
 * Modified:   23-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  23-May-2008 Initial build
 */

package mars.mars.events;
import javax.swing.*;
import javax.swing.event.*;
import mars.mars.gui.*;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class FDBuilder_AddConversionsandAlarms extends Object implements ListSelectionListener
{
  JTable parent;
  
  public FDBuilder_AddConversionsandAlarms(JTable source)
  {
    parent = source;
  }
  
  public void valueChanged(ListSelectionEvent lse)
  {
    int col = parent.getSelectedColumn();
    int row = parent.getSelectedRow();
    /*
     * Column 6 is the SQL queries and column 7 the alarms. Use this to add alarms for each one.
     */
    if (col > 5)
    {
      JOptionPane.showMessageDialog(parent, "You clicked on the cell at row " + row + " column " + col);
    }
  }
}