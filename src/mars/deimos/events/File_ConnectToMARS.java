/* Class name: File_ConnectToMARS
 * File name:  File_ConnectToMARS.java
 * Created:    09-Jun-2008 16:16:23
 * Modified:   16-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  16-Jun-2008 Added code to initiate the RMI connection
 * 0.001  09-Jun-2008 Initial build
 */

package mars.deimos.events;
import java.awt.event.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.JOptionPane;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.remote.impl.ConnectionFactory;

/**
 * This class is used to connect to the MARS application
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class File_ConnectToMARS implements ActionListener
{
  private Logger log;
  
  public File_ConnectToMARS()
  {
    log = LoggerFactory.getLogger("mars.deimos.events.File_ConnectToMARS");
    log.finest("Initialised");
  }
  
  /**
   * The event fired from the File menu to close the application
   * @param ae The triggered ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    // Get the host name of the RMI server from the user
    String host = JOptionPane.showInputDialog("Enter host name or IP address of the RMI Server.");
    Preferences prefs = DeimosPreferences.getDeimosPrefs();
    prefs.put("deimos.config.preferredMARSHost", host);
    DeimosPreferences.updateDeimosPrefs();
    log.config("Creating connection to MARS RMI server.");
    ConnectionFactory conF = new ConnectionFactory();
    log.config("Created connection");
  }
}