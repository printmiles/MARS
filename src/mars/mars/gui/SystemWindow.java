/* Class name: SystemWindow
 * File name:  SystemWindow.java
 * Created:    16-Apr-2008 13:22:55
 * Modified:   21-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  21-Apr-2008 Adding support for Save > Save FormatDoc item to test the mars.mars.xml.FormatDocWriter class
 * 0.001  16-Apr-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import mars.mars.events.*;
import mars.mars.object.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class is used to provide the windows for each monitored system within MARS. It should provide
 * the user with a JMenu, JToolbar, JTree and JTable and also acts to control all of the other items
 * associated with communication to the specified system; threads, RMI comms, FormatDocs and the like.
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class SystemWindow extends JInternalFrame
{
  private static final String parentClassName = "mars.mars.gui.SystemWindow";
  private Logger log;
  private FormatDoc fdSystem;
  
  public SystemWindow(String aSystemName)
  {
    super("System Window - " + aSystemName, true, true);
    super.setLayer(2);
    log = LoggerFactory.getLogger(parentClassName);
    fdSystem = FormatDocStore.get(aSystemName);
    log.log(Level.CONFIG,"Obtained FormatDoc for system " + aSystemName, fdSystem);
    JMenuBar jMB = new JMenuBar();
    JMenu jmSave = new JMenu("Save");
    JMenuItem jmiSave = new JMenuItem("Save FormatDoc");
    jmiSave.addActionListener(new File_Save(aSystemName));
    jmSave.add(jmiSave);
    jMB.add(jmSave);
    JTextArea jtaEnvironment = new JTextArea();
    jtaEnvironment.append("FormatDoc Obtained for:");
    jtaEnvironment.append("\n~~~~~~~~~~~~~~~~~~~");
    jtaEnvironment.append("\n" + aSystemName);
    jtaEnvironment.setEditable(false);
    jtaEnvironment.setTabSize(4);
    this.setJMenuBar(jMB);
    this.add(jtaEnvironment, BorderLayout.CENTER);
    
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setSize(200,350);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
    this.setFrameIcon(new ImageIcon("images/Connect.png"));
    this.setVisible(true);
  }
}