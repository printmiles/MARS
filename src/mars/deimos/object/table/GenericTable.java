/* Class name: GenericTable
 * File name:  GenericTable.java
 * Created:    06-May-2008 15:31:40
 * Modified:   14-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003a 14-Jul-2008 Copied from Mars version and adapted
 * 0.003  20-May-2008 Added additional methods to support editable cells.
 * 0.002  19-May-2008 Added convinience methods for duplicating a table:
 *        getColumns     - Used to return the column headers for the table
 *        getRows        - Used to return all rows of the table in their vector
 *        getRow(int)    - Used to return a whole row as an instance of Object[]
 *        removeRow(int) - Used to remove the designated row from the table
 * 0.001  06-May-2008 Initial build - This was taken from code I've written before
 */

package mars.deimos.object.table;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class is used as the data model for the many JTables used within MARS. This class extends the AbstractTableModel
 * class and so can be used to create an instance of JTable.
 * @version 0.003a
 * @author Alex Harris (W4786241)
 * @see javax.swing.table.AbstractTableModel
 */
public class GenericTable extends AbstractTableModel
{
  private Vector columnNames = new Vector();
  private Vector data = new Vector();
  private static Logger log;
  private static final String parentClassName = "mars.deimos.object.table.GenericTable";
  private int[] editableCells;
  
  /**
   * Used to initialise the class with a few data items. This will be overwritten by the items inputted
   * when the class is first used by an implementing class.
   */
  public GenericTable()
  {
    log = LoggerFactory.getLogger(parentClassName);
    columnNames.add("Default Table");
    data.add(new Object[] {"New Data To Be Displayed Here #1"});
    data.add(new Object[] {"New Data To Be Displayed Here #2"});
    data.add(new Object[] {"New Data To Be Displayed Here #3"});
    data.add(new Object[] {"New Data To Be Displayed Here #4"});
    data.add(new Object[] {"New Data To Be Displayed Here #5"});
  }

  /**
   * Returns the number of columns stored within the table.
   * @return The column count of the table
   */
  public int getColumnCount()
  {
    return columnNames.size();
  }

  /**
   * Returns the number of rows within the table
   * @return The row count of the table
   */
  public int getRowCount()
  {
    return data.size();
  }
  
  /**
   * Returns the name of the column given its index
   * @param col The index number of the column in question. This is zero based
   * @return The name of the column as a string
   */
  public String getColumnName(int col)
  {
    return (String) columnNames.get(col);
  }

  /**
   * Returns the items stored at a particular reference within a table using the
   * Vector and Object references to access the relevant item
   * @param row The row within the table
   * @param col The column within the row
   * @return The object stored at the requested location
   */
  public Object getValueAt(int row, int col)
  {
    Object[] temp = (Object[]) data.get(row);
    if (col >= temp.length)
    {
      return null;
    }
    else
    {
      return temp[col];
    }
  }

  /**
   * Returns the name of the class of the object stored at the given location
   * @param c The index of the requested column. This is zero based
   * @return The representative class of the object type stored in this column
   */
  public Class getColumnClass(int c)
  {
    return getValueAt(0, c).getClass();
  }

  /**
   * Used to change the value on an item at a particular location specified by the intersection
   * of the row and column coordinates.
   * @param value The object to be placed at the requested cell location in place of the currently stored item
   * @param row The row to be used. This is zero based
   * @param col The column to be used. This is zero based
   */
  public void setValueAt(Object value, int row, int col)
  {
    Object[] temp = (Object[]) data.get(row);
    temp[col] = value;
    fireTableCellUpdated(row, col);
  }

  /**
   * Used to change the entire dataset of the table to that specified in the provided vectors.
   * The columns Vector should contain instances of Strings simply added to the vector. The rows
   * are represented by Object[] arrays placed within the vector. The number of items within the
   * columns vector should be the same as the number of items placed within each and every Object
   * array in the rows vector.
   * <p>This method also fires and triggers that may be associated with the table.
   * @param columns The vector containing the column names as String instances.
   * @param rows The rows for the table. This is a vector with an instance of Object[]
   * at each entry within the Vector with a corresponding number of entries and types to other rows
   * within the vector. The number of columns of each row should also correspond with the number of
   * columns within the columns vector.
   * @see java.util.vector
   */
  public void setData(Vector columns, Vector rows)
  {
    log.finer("Entered method");
    columnNames = columns;
    data = rows;
    fireTableStructureChanged();
    if (rows == null)
    {
      fireTableRowsInserted(0, 0);
    }
    else
    {
      fireTableRowsInserted(0, rows.size()-1);
    }
  }

  /**
   * Used to change the rows of a dataset where the columns already defined can be reused.
   * See the setData(Vector, Vector) for more information.
   * @param rows The rows for the table. This is a vector with an instance of Object[] at each
   * entry within the vector. The number of columns of each row should also correspond with the
   * number of columns within the columns vector already defined.
   * @see GenericTable.setData(Vector, Vector)
   */
  public void setData(Vector rows)
  {
    log.finer("Entered method");
    data = rows;
    fireTableRowsInserted(0, data.size());
  }

  /**
   * Adds a single row to the rows vector. Takes the supplied Object[] array and adds it
   * directly to the vector. Assumes that the supplied object is compatible (of similar size
   * and types) to those Object[] instances already stored in the vector.
   * <p>It utilises the Vector.add(Object) method to insert the data within the Vector so
   * any limitations on data placements are carried over from this method.
   * @param aNewRow The Object[] array to be inserted into the Vector.
   * @see java.util.Vector
   */
  public void addRow(Object[] aNewRow)
  {
    log.finer("Entered method");
    data.add(aNewRow);
    fireTableRowsInserted(0, data.size()-1);
  }
  
  /**
   * Used to return the headers of a table
   * @return The vector containing the headers for the table
   */
  public Vector getColumns()
  {
    return columnNames;
  }
  
  /**
   * Used to return the vector containing all of the row data
   * @return The vector containing the Object[] arrays making up the table data
   */
  public Vector getRows()
  {
    return data;
  }
  
  /**
   * Used to return a particular row within the table
   * @param x The row at position x to be returned
   * @return The row contents as an instance of Object[]
   */
  public Object[] getRow(int x)
  {
    return (Object[]) data.get(x);
  }
  
  /**
   * Used to remove a particular row from the table model.
   * @param x The row number to be removed.
   */
  public void removeRow(int x)
  {
    data.remove(x);
    fireTableDataChanged();
  }
  
  /**
   * Defines the columns within the table which are editable to those contained within the array.
   * If the table is completely uneditable then this method shouldn't be used or the argument
   * should be set to null or an empty array. The columns whose index is included in the array
   * is then set to editable. This means that if a whole table is set to editable then all indices
   * should be contained within the array from 0 to size-1. The entries may be non-sequential and unordered.
   * @param editableColumns
   */
  public void setEditableColumns(int[] editableColumns)
  {
    editableCells = editableColumns;
  }
  
  /**
   * Returns a boolean value representing whether the column can be edited or not.
   * @param row This is used internally by Java and not of importance to the functioning of the method.
   * @param col This is the (zero-based) column index that is being queried.
   * @return Whether or not the column contents can be edited.
   */
  public boolean isCellEditable(int row, int col)
  {
    boolean result = false;
    
    if (editableCells == null)
    {
      return false;
    }
    
    for (int i = 0; i < editableCells.length; i++)
    {
      if (col == editableCells[i])
      {
        result = true;
      }
    }
    
    return result;
  }
}