/* Class name: ListDeimosClients
 * File name:  ListDeimosClients.java
 * Created:    30-May-2008 14:03:16
 * Modified:   30-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  11-Aug-2008 Added code to display active Deimos clients
 * 0.001  30-May-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.logging.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import mars.mars.gui.MarsClient;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.table.GenericTable;
import mars.mars.remote.impl.ConnectionFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class ListDeimosClients extends JInternalFrame implements ActionListener
{
  private Logger log;
  private MarsResourceBundle mrb;
  private static final String parentClassName = "mars.mars.events.ListDeimosClients";
  private Vector vecColumns;
  private Vector vecRows;
  private GenericTable gt;
  
  public ListDeimosClients()
  {
    super("Deimos Clients:", true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(parentClassName);
    // Obtain the localisation settings
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = MarsPreferences.getMarsPrefs();
    String strCountry = prefUser.get("aKey.aVal", System.getProperty("user.country"));
    String strLanguage = prefUser.get("aKey.aVal", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    log.config("Got a Locale for the current session " + locMe.getLanguage() + "_" + locMe.getCountry());
    // Obtain a resource bundle to provide localised strings
    mrb = new MarsResourceBundle(locMe);
    gt = new GenericTable();
    vecColumns = new Vector();
    vecColumns.add("Client");
    vecColumns.add("Source");
    vecRows = new Vector();
    JTable jt = new JTable(gt);
    JScrollPane jsp = new JScrollPane(jt);
    this.add(jsp);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(600, 300);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    Enumeration eDeimosClients = ConnectionFactory.getKeys();
    while (eDeimosClients.hasMoreElements())
    {
      Object[] oEntry = new Object[2];
      oEntry[0] = (String) eDeimosClients.nextElement();
      Hashtable htEntry = ConnectionFactory.getItem((String) oEntry[0]);
      oEntry[1] = (String) htEntry.get("name");
      log.config("Found a client at: " + ((String) oEntry[0]) + " with port " + ((String) oEntry[1]) + " available for monitoring");
      vecRows.add(oEntry);
    }
    gt.setData(vecColumns, vecRows);
    this.setVisible(true);
  }
}