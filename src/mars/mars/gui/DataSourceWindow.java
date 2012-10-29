/* Class name: DataSourceWindow
 * File name:  DataSourceWindow.java
 * Created:    09-May-2008 16:54:52
 * Modified:   30-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  30-May-2008 Added code to include a button to connect to Deimos clients
 * 0.002  12-May-2008 Added code details to main method, also added setTextField method
 * 0.001  09-May-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import mars.mars.events.*;
import mars.mars.jdbc.ODBCReader;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.table.GenericTable;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class DataSourceWindow extends JInternalFrame
{
  private static final String parentClassName = "mars.mars.gui.DataSourceWindow";
  private Logger log;
  private JTextField jtfDSNName;
  
  public DataSourceWindow()
  {
    // Set the JInternalFrame properties inherited from the superclass
    super("Select Datasource:", true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(parentClassName);
    // Obtain the localisation settings
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = MarsPreferences.getMarsPrefs();
    String strCountry = prefUser.get("mars.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("mars.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    // Obtain a resource bundle to provide localised strings for the file chooser
    MarsResourceBundle mrbDSW = new MarsResourceBundle(locMe);
    // Create a vector for the column names and populate it
    Vector vecColumns = new Vector();
    vecColumns.add(mrbDSW.getRBString("mars.gui.datasource.table.name", "Name"));
    vecColumns.add(mrbDSW.getRBString("mars.gui.datasource.table.type", "Type"));
    vecColumns.add(mrbDSW.getRBString("mars.gui.datasource.table.driver", "Driver"));
    // Create a new JTable for the GUI
    GenericTable gtDSN = new GenericTable();
    JTable jtDSN = new JTable(gtDSN);
    jtDSN.setRowSorter(new TableRowSorter(gtDSN));
    jtDSN.setRowSelectionAllowed(true);
    jtDSN.setColumnSelectionAllowed(false);
    // Initialise a Vector to store the Database names
    Vector vecJDBCDb = new Vector();
    // Check the system OS to see if we can use the correct method
    if ((System.getProperty("os.name")).startsWith("Windows"))
    {
      // The app is running on Windows of some form, we should be able to use the
      // Initialise the ODBCReader class so that we can found out the databases available to us.
      ODBCReader odbcR = new ODBCReader();
      // Get the list of Database Driver instances on the machine
      log.entering("ODBCReader", "getAllDSNs");
      vecJDBCDb = odbcR.getAllDSNs();
      log.exiting("ODBCReader", "getAllDSNs", vecJDBCDb);
    }
    else
    {
      // The app is running on some other form of OS so we can't call the ODBCReader's methods
      String[] aRow = new String[3];
      aRow[0] = mrbDSW.getRBString("mars.gui.datasource.table.unknown.name","No Data Found");
      aRow[1] = mrbDSW.getRBString("mars.gui.datasource.table.unknown.type","Incompatible OS");
      aRow[2] = mrbDSW.getRBString("mars.gui.datasource.table.unknown.driver","Unknown Database");
      vecJDBCDb.add(aRow);
      jtDSN.setEnabled(false);
    }
    
    gtDSN.setData(vecColumns, vecJDBCDb);
    // Put the GUI together
    log.finest("Added database names to JTable. Creating GUI");
    // Create the table
    Dimension dimTbl = new Dimension(300, 200); // Dimension for the table
    JScrollPane scrlA = new JScrollPane(jtDSN);
    scrlA.setPreferredSize(dimTbl);
    jtDSN.getSelectionModel().addListSelectionListener(new DataSourceTableListener(this, jtDSN));
    // Add the DSN components to a JPanel
    JPanel jpDSN = new JPanel();
    jpDSN.setLayout(new BorderLayout());
    JLabel jlblName = new JLabel(mrbDSW.getRBString("mars.gui.datasource.label", "Database to Connect To:"));
    jtfDSNName = new JTextField("");
    JButton jbClick = new JButton("OK");
    jlblName.setMaximumSize(new Dimension(100,25));
    jtfDSNName.setMaximumSize(new Dimension(100,25));
    jbClick.setMaximumSize(new Dimension(100,25));
    jbClick.addActionListener(new InterrogateDataSource(this));
    jpDSN.add(jlblName, BorderLayout.NORTH);
    jpDSN.add(jtfDSNName, BorderLayout.CENTER);
    jpDSN.add(jbClick, BorderLayout.EAST);
    TitledBorder brdrDSN = BorderFactory.createTitledBorder(mrbDSW.getRBString("mars.gui.datasource.border.panel","Datasource:"));
    jpDSN.setBorder(brdrDSN);
    // Add a button for connecting to Deimos clients
    JPanel jpDeimos = new JPanel();
    JLabel jlDeimos = new JLabel(mrbDSW.getRBString("mars.gui.datasource.deimos.connect", "View Available Deimos Clients:"));
    JButton jbDeimos = new JButton("Deimos");
    jbDeimos.addActionListener(new ListDeimosClients());
    jpDeimos.setLayout(new GridLayout(2,1));
    jpDeimos.add(jlDeimos);
    jpDeimos.add(jbDeimos);
    TitledBorder brdrDeimos = BorderFactory.createTitledBorder("Deimos:");
    jpDeimos.setBorder(brdrDeimos);
    // Add the components to the bottom of the window
    JPanel jpBottom = new JPanel();
    jpBottom.setLayout(new GridLayout(1,2));
    jpBottom.add(jpDSN);
    jpBottom.add(jpDeimos);
    
    this.add(scrlA, BorderLayout.CENTER);
    this.add(jpBottom, BorderLayout.SOUTH);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(400, 300);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
    this.setVisible(true);
  }
  
  public void setTextField(String newContents)
  {
    jtfDSNName.setText(newContents);
  }
  
  public String getTextField()
  {
    return jtfDSNName.getText();
  }
}