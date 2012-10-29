/* Class name: FormatDocBuilder
 * File name:  FormatDocBuilder.java
 * Created:    17-May-2008 23:21:11
 * Modified:   21-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  21-May-2008 Added code to support the FDEditor window.
 * 0.002  19-May-2008 Added code to display JTabbedPane with the tables
 * 0.001  17-May-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.table.*;
import mars.mars.events.*;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.table.GenericTable;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Haris (W4786241)
 */
public class FormatDocBuilder extends JInternalFrame
{
  private static final String parentClassName = "mars.mars.gui.FormatDocBuilder";
  private MarsResourceBundle mrbFDB;
  private Logger log;
  private Hashtable htTables; // Used to retrieve the available tables
  private Hashtable htPanels;  // Used to store the JPanels in the gui
  private Hashtable htDestTbls; // Used to store the destination tables
  
  public FormatDocBuilder(Hashtable htDataSourceTables, String name)
  {
    // Set the JInternalFrame properties inherited from the superclass
    super("Edit Datasource:", true, true);
    super.setLayer(1);
    htTables = htDataSourceTables;
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
    mrbFDB = new MarsResourceBundle(locMe);
    // Use the buildTabbedPaneData() method to change the contents of htTables to contain
    // JPanels with all of the components and events so that we can add them straight into a tab
    log.entering(parentClassName, "buildTabbedPaneData()", htTables);
    buildTabbedPaneData();
    log.exiting(parentClassName, "buildTabbedPaneData()", htTables);
    // Add the JPanels to the GUI
    JTabbedPane jtp = new JTabbedPane();
    Enumeration enumPanels = htPanels.keys();
    while (enumPanels.hasMoreElements())
    {
      String strPanelName = (String) enumPanels.nextElement();
      JPanel jpTemp = (JPanel) htPanels.get(strPanelName);
      jtp.add(strPanelName, jpTemp);
    }
    JButton jbBuild = new JButton(mrbFDB.getRBString("mars.gui.FDB.next", "Next >"));
    jbBuild.addActionListener(new SelectFDTableFields(this, htDestTbls, name));
    this.add(jtp, BorderLayout.CENTER);
    this.add(jbBuild, BorderLayout.SOUTH);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(600, 300);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
    this.setVisible(true);
  }
  
  private void buildTabbedPaneData()
  {
    htPanels = new Hashtable();
    htDestTbls = new Hashtable();
    Enumeration enumTables = htTables.keys();
    while (enumTables.hasMoreElements())
    {
      String tableName = (String) enumTables.nextElement();
      // Create the JTable and scroll pane for the source table structure
      GenericTable gtSource = (GenericTable) htTables.get(tableName);
      JScrollPane jspSource, jspDest; // These will contain the two tables
      JTable jtSource = new JTable(gtSource);
      jtSource.setRowSelectionAllowed(true);
      jtSource.setColumnSelectionAllowed(false);
      jspSource = new JScrollPane(jtSource);
      // Now create the destination one.
      GenericTable gtDest = new GenericTable();
      gtDest.setData(gtSource.getColumns(), new Vector());
      JTable jtDest = new JTable(gtDest);
      jtDest.setRowSelectionAllowed(true);
      jtDest.setColumnSelectionAllowed(false);
      jspDest = new JScrollPane(jtDest);
      JButton jbAllS2D = new JButton(">>"); // Used to move all items from the source to the destination table
      jbAllS2D.addActionListener(new FDBuilderRowMover(jtSource, jtDest,true));
      JButton jbSomeS2D = new JButton(">"); // Used to move only the selected rows from source to destination
      jbSomeS2D.addActionListener(new FDBuilderRowMover(jtSource, jtDest,false));
      JButton jbSomeD2S = new JButton("<"); // Used to move only the selected rows from destination to source
      jbSomeD2S.addActionListener(new FDBuilderRowMover(jtDest, jtSource, false));
      JButton jbAllD2S = new JButton("<<"); // Used to move all items from the destination to the source table
      jbAllD2S.addActionListener(new FDBuilderRowMover(jtDest, jtSource,true));
      JPanel jpSub = new JPanel(); // this is a sub panel used to contain the two buttons
      jpSub.setLayout(new GridLayout(4,1,10,10));
      jpSub.add(jbAllS2D);
      jpSub.add(jbSomeS2D);
      jpSub.add(jbSomeD2S);
      jpSub.add(jbAllD2S);
      JPanel jpTemp = new JPanel(); // This is the panel used to contain all of the components
      jpTemp.setLayout(new GridLayout(1,3,1,1));
      jpTemp.add(jspSource);
      jpTemp.add(jpSub);
      jpTemp.add(jspDest);
      htPanels.put(tableName, jpTemp);
      htDestTbls.put(tableName, gtDest);
    }
  }
}