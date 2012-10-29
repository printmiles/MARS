/* Class name: DataSourceTableListener
 * File name:  DataSourceTableListener.java
 * Created:    12-May-2008 11:50:11
 * Modified:   12-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  12-May-2008 Initial build
 */

package mars.mars.events;
import javax.swing.*;
import javax.swing.event.*;
import mars.mars.gui.DataSourceWindow;

/**
 * This class is used with the DataSource window to detect user events generated from the
 * displayed JTable. This class is used to detect selections of rows within the JTable and
 * update the JTextField on the form accordingly.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class DataSourceTableListener extends Object implements ListSelectionListener
{
  private DataSourceWindow parent;
  private JTable jtDSW;
  
  /**
   * Initialises the class. Requires the parent form and JTable to work correctly.
   * @param dswParent The parent DataSourceWindow instance
   * @param parentTable The parent JTable instance within the DataSourceWindow
   */
  public DataSourceTableListener(DataSourceWindow dswParent, JTable parentTable)
  {
    parent = dswParent;
    jtDSW = parentTable;
  }
  
  /**
   * The event fired by a ListSelectionEvent.
   * @param event The event triggered.
   */
  public void valueChanged(ListSelectionEvent event)
  {
    if (event.getValueIsAdjusting())
    {
      return;
    }
    String dsnName = (String) jtDSW.getModel().getValueAt(jtDSW.getSelectionModel().getLeadSelectionIndex(), 0);
    parent.setTextField(dsnName);
  }
}