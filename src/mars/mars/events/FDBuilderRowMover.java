/* Class name: FDBuilderRowMover
 * File name:  FDBuilderRowMover.java
 * Created:    19-May-2008 12:39:28
 * Modified:   29-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  29-May-2008 Added support for move some and move all buttons
 * 0.001  19-May-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import mars.mars.object.table.*;

/**
 * This class is used to copy items from one side of the FDEditor window to another. The source and destination
 * are set by the order of the supplied JTable instances. The first being set as the source and the second the
 * destination.
 * <p>A boolean value is also used to indicate whether all entries from one JTable should be moved to the other
 * or just those lines that are selected by the user. This class is responsible for handling the events from
 * the four buttons on the FDEditor window which is in turn replicated for each and every table within a
 * given data source. This class is attached in the following way to each of the four buttons:
 * <ul>
 *   <li>FDBuilderRowMover(LH-side JTable, RH-side JTable, true)</li>
 *   <li>FDBuilderRowMover(LH-side JTable, RH-side JTable, false)</li>
 *   <li>FDBuilderRowMover(RH-side JTable, LH-side JTable, false)</li>
 *   <li>FDBuilderRowMover(RH-side JTable, LH-side JTable, true)</li>
 * </ul>
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class FDBuilderRowMover implements ActionListener
{
  private JTable tblFrom, tblTo; // These are used to store the tables we're moving data between.
  private boolean moveAll; // Whether the event should move all of the contents of one table to another.
  
  /**
   * The class constructor.
   * @param from The JTable the data item is moving from
   * @param to The JTable the data item is moving to
   * @param moveAllRows A boolean value indicating whether all of the rows within the JTable should be moved
   */
  public FDBuilderRowMover(JTable from, JTable to, boolean moveAllRows)
  {
    tblFrom = from;
    tblTo = to;
    moveAll = moveAllRows;
  }
  
  /**
   * The method inherited from the ActionListener interface which is used to perform the requested action on demand.
   * @param ae The associated ActionEvent
   * @see java.awt.event.ActionListener
   */
  public void actionPerformed(ActionEvent ae)
  {
    // First obtain the GenericTable (table models) from the source and destination tables.
    GenericTable gtFrom = (GenericTable) tblFrom.getModel();
    GenericTable gtTo = (GenericTable) tblTo.getModel();
    
    if (moveAll)
    {
      Vector rows = gtFrom.getRows();
      int offset = 0;
      int rowSize = rows.size();
      for (int i = 0; i < rowSize; i++)
      {
        int j = i + offset;
        Object[] objTemp = (Object[]) rows.get(0 + offset);
        if (((Boolean) objTemp[4]).booleanValue())
        {
          gtTo.addRow(objTemp);
          gtFrom.removeRow(0 + offset);
        }
        else
        {
          offset++;
        }
        
      }
    }
    else
    {
      // If moveAll is false then we need to find which rows to movoe from one table to the other.
      // Get the integer positions of the rows to be moved.
      int[] rowsToMove = tblFrom.getSelectedRows();
      for (int i = 0; i < rowsToMove.length; i++)
      {
        /*
         * Iterate through the array of selected row positions (rowsToMove[]) and
         * add the contents of the row position to the destination table and then
         * delete the contents from the source table.
         */
        int adjustedPosition = rowsToMove[i] - i;
        Object[] objTemp = gtFrom.getRow(adjustedPosition);
        // Check whether the data item is supported by mars or not.
        if (((Boolean) objTemp[4]).booleanValue())
        {
          // If it's true then we can move the value across.
          gtTo.addRow(objTemp);
          gtFrom.removeRow(adjustedPosition);
        }
        else
        {
          // If it's not then throw an error onto the screen.
          JOptionPane.showMessageDialog(mars.mars.gui.MarsClient.getDesktopPane(),
                  "Cannot add this attribute as it is an incompatible type:\nAttribute Name: \t".concat(((String) objTemp[1]))
                  .concat("\nSQL Type: \t").concat((String) objTemp[2]));
        }
      }
    }
  }
}