/* Class name: FDEditor
 * File name:  FDEditor.java
 * Created:    20-May-2008 20:43:47
 * Modified:   02-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  02-Jun-2008 Added Build button to build the FormatDoc class
 * 0.002  21-May-2008 Added main code
 * 0.001  20-May-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import mars.mars.events.*;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.table.*;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class FDEditor extends JInternalFrame
{
  private Logger log;
  private MarsResourceBundle mrbFDE;
  private static final String parentClassName = "mars.mars.gui.FDEditor";
  
  public FDEditor(String dsnName, Hashtable htFields)
  {
    super("Edit Datasource:", true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(parentClassName);
    // Obtain the localisation settings
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = MarsPreferences.getMarsPrefs();
    String strCountry = prefUser.get("mars.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("mars.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    log.config("Got a Locale for the current session " + locMe.getLanguage() + "_" + locMe.getCountry());
    // Obtain a resource bundle to provide localised strings for the file chooser
    mrbFDE = new MarsResourceBundle(locMe);
    
    GenericTable gtSelectedFields = new GenericTable();
    JTable jtFields = new JTable(gtSelectedFields);
    JScrollPane jspTable = new JScrollPane(jtFields);
    Vector vecColumns = new Vector();
    vecColumns.add("Source");
    vecColumns.add("Table");
    vecColumns.add("Attribute Name");
    vecColumns.add("Type");
    vecColumns.add("Alias");
    vecColumns.add("Report");
    vecColumns.add("Conversions");
    vecColumns.add("Alarms");
    Vector vecRows = new Vector();
    Enumeration enumFields = htFields.keys();
    while (enumFields.hasMoreElements())
    {
      String tableName = (String) enumFields.nextElement();
      GenericTable gtTemp = (GenericTable) htFields.get(tableName);
      Vector vecTmpRows = gtTemp.getRows();
      for (int i = 0; i < vecTmpRows.size(); i++)
      {
        Object[] objSourceTbl = (Object[]) vecTmpRows.get(i);
        Object[] objNewRow = new Object[8];
        objNewRow[0] = dsnName;            // Source DSN Name
        objNewRow[1] = tableName;          // Source Table
        objNewRow[2] = objSourceTbl[1];    // Original Attribute Name
        objNewRow[3] = objSourceTbl[3];    // Mars Attribute Type
        objNewRow[4] = objSourceTbl[1];    // Alias name allocated by the user
        objNewRow[5] = new Boolean(false); // Whether the field should be reported or not
        objNewRow[6] = "Not set";          // Conversions to be performed on the attribute
        objNewRow[7] = "Not set";          // Alarms to be added to the attribute
        vecRows.add(objNewRow);
      }
    }
    gtSelectedFields.setEditableColumns(new int[] {4,5});
    gtSelectedFields.setData(vecColumns, vecRows);
    jtFields.getSelectionModel().addListSelectionListener(new FDBuilder_AddConversionsandAlarms(jtFields));
    
    this.add(jspTable, BorderLayout.CENTER);
    JButton jbBuildFD = new JButton("Build FormatDoc");
    jbBuildFD.addActionListener(new FDEditor_BuildFormatDoc(gtSelectedFields,"DSN"));
    this.add(jbBuildFD, BorderLayout.SOUTH);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(600, 300);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
    this.setVisible(true);
  }
}