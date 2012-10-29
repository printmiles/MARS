/* Class name: COMPortConfig
 * File name:  COMPortConfig.java
 * Created:    14-Jul-2008 11:29:11
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  03-Aug-2008 Added extra row for testing the incoming feed.
 * 0.002  21-Jul-2008 Added support for reading previously saved values
 * 0.001  14-Jul-2008 Initial build
 */

package mars.deimos.gui;
import gnu.io.CommPortIdentifier; // This is used to work with the COM ports using RxTx
import java.awt.BorderLayout;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import mars.deimos.object.customisation.*;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.object.table.GenericTable;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class COMPortConfig extends JInternalFrame
{
  private Logger log;
  private DeimosResourceBundle drbCPC;
  private static final String parentClassName = "mars.deimos.gui.COMPortConfig";
  
  public COMPortConfig()
  {
    super("Configure COM Ports:", true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(parentClassName);
    // Obtain the localisation settings
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = DeimosPreferences.getDeimosPrefs();
    String strCountry = prefUser.get("deimos.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("deimos.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    log.config("Got a Locale for the current session " + locMe.getLanguage() + "_" + locMe.getCountry());
    // Obtain a resource bundle to provide localised strings
    drbCPC = new DeimosResourceBundle(locMe);
    // Build the table headers
    Vector vecColumns = new Vector();
    vecColumns.add("Monitor");
    vecColumns.add("Name");
    vecColumns.add("Type");
    vecColumns.add("In Use?");
    vecColumns.add("Baud (bps)");
    vecColumns.add("Data Bits");
    vecColumns.add("Parity");
    vecColumns.add("Stop Bits");
    Vector vecRows = new Vector();
    // Get the COM ports of the local machine
    Enumeration enumPorts = CommPortIdentifier.getPortIdentifiers();
    while (enumPorts.hasMoreElements())
    {
      // For each COM port we create a new Object[] array
      CommPortIdentifier comPort = (CommPortIdentifier) enumPorts.nextElement();
      Object[] objItems = new Object[8];
      // Set monitor to false
      objItems[0] = new Boolean(false);
      // Obtain the name of the COM port
      objItems[1] = comPort.getName();
      // Identify the type of port
      switch (comPort.getPortType())
      {
        case CommPortIdentifier.PORT_PARALLEL:
        {
          objItems[2] = "Parallel";
          break;
        }
        case CommPortIdentifier.PORT_SERIAL:
        {
          objItems[2] = "Serial";
          break;
        }
        default:
        {
          objItems[2] = "Other";
          break;
        }
      }
      objItems[3] = new Boolean(comPort.isCurrentlyOwned()); // Is the port used elsewhere?
      objItems[4] = new Integer(9600);  // Set the baud rate
      objItems[5] = new Integer(8); // Set the data bits
      objItems[6] = "None"; // Set the parity
      objItems[7] = new Float(1.0); // Set the stop bits
      // Iterate through the current preferences file to see if there are already some settings saved.
      int currentlyStored = (new Integer(prefUser.get("deimos.comPort.numberToMonitor", "0"))).intValue();
      for (int x = 0; x < currentlyStored; x++)
      {
        String name = prefUser.get("deimos.comPort." + x + ".name", "Unknown");
        if (name.equals(comPort.getName()))
        {
          objItems[0] = new Boolean(true); // Overwrite the Monitor value
          objItems[4] = new Integer(prefUser.getInt("deimos.comPort." + x + ".baud", 9600)); // Overwrite the baud value
          objItems[5] = new Integer(prefUser.getInt("deimos.comPort." + x + ".data", 8)); // Overwrite the data value
          objItems[6] = prefUser.get("deimos.comPort." + x + ".parity", "None"); // Overwrite the parity setting
          objItems[7] = new Float(prefUser.getFloat("deimos.comPort." + x + ".stop", 1)); // Overwrite the stop bit setting
        }
      }
      vecRows.add(objItems);
    }
    // Add a row for testing (COM99)
    Object[] objItems = new Object[8];
    objItems[0] = new Boolean(false); // Monitor?
    objItems[1] = "COM99"; // Port name
    objItems[2] = "Test"; // Type
    objItems[3] = new Boolean(false); // Used elsewhere?
    objItems[4] = new Integer(9600); // Baud rate
    objItems[5] = new Integer(8); // Data bits
    objItems[6] = "None"; // Parity
    objItems[7] = new Float(1.0); // Stop bits
    vecRows.add(objItems);
    // Create a GenericTable for displaying the vector contents
    GenericTable gtCOM = new GenericTable();
    gtCOM.setData(vecColumns, vecRows);
    JTable jtCOMConfig = new JTable(gtCOM);
    TableColumn tcBaud = jtCOMConfig.getColumnModel().getColumn(4);
    tcBaud.setCellEditor(new DefaultCellEditor(buildBaudBox()));
    TableColumn tcData = jtCOMConfig.getColumnModel().getColumn(5);
    tcData.setCellEditor(new DefaultCellEditor(buildDataBox()));
    TableColumn tcParity = jtCOMConfig.getColumnModel().getColumn(6);
    tcParity.setCellEditor(new DefaultCellEditor(buildParityBox()));
    TableColumn tcStop = jtCOMConfig.getColumnModel().getColumn(7);
    tcStop.setCellEditor(new DefaultCellEditor(buildStopBox()));
    gtCOM.setEditableColumns(new int[] {0,4,5,6,7});
    JScrollPane jsp = new JScrollPane(jtCOMConfig);
    this.add(jsp, BorderLayout.CENTER);
    JButton jbSave = new JButton("Save");
    jbSave.addActionListener(new mars.deimos.events.comPort_Save(gtCOM, this));
    this.add(jbSave, BorderLayout.SOUTH);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(600, 200);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = DeimosClient.getDesktopPane();
    parent.add(this);
    this.setVisible(true);
    log.finest("Displaying COMPortConfig window");
    parent = null;
  }
  
  /**
   * Method copied from JavaNews, one of my other projects
   * @return A combobox with the available baud rates within
   */
  private JComboBox buildBaudBox()
  {
    log.finer("Entered method");
    JComboBox baudBox = new JComboBox();
    baudBox.addItem("110");
    baudBox.addItem("300");
    baudBox.addItem("600");
    baudBox.addItem("1200");
    baudBox.addItem("2400");
    baudBox.addItem("4800");
    baudBox.addItem("9600");
    baudBox.addItem("14400");
    baudBox.addItem("19200");
    baudBox.addItem("38400");
    baudBox.addItem("57600");
    baudBox.addItem("115200");
    baudBox.setSelectedItem("9600");
    return baudBox;
  }

  /**
   * Method copied from JavaNews, one of my other projects
   * @return A combobox with the available data bit values within
   */
  private JComboBox buildDataBox()
  {
    log.finer("Entered method");
    JComboBox dataBox = new JComboBox();        
    dataBox.addItem("5");
    dataBox.addItem("6");
    dataBox.addItem("7");
    dataBox.addItem("8");
    dataBox.setSelectedItem("8");
    return dataBox;
  }

  /**
   * Method copied from JavaNews, one of my other projects
   * @return A combobox with the available parity values within
   */
  private JComboBox buildParityBox()
  {
    log.finer("Entered method");
    JComboBox parityBox = new JComboBox();
    parityBox.addItem("Even");
    parityBox.addItem("Odd");
    parityBox.addItem("Mark");
    parityBox.addItem("None");
    parityBox.setSelectedItem("None");
    return parityBox;
  }

  /**
   * Method copied from JavaNews, one of my other projects
   * @return A combobox with the available stop bit values within
   */
  private JComboBox buildStopBox()
  {
    log.finer("Entered method");
    JComboBox stopBox = new JComboBox();
    stopBox.addItem("1");
    stopBox.addItem("1.5");
    stopBox.addItem("2");
    stopBox.setSelectedItem("1");
    return stopBox;
  }
}