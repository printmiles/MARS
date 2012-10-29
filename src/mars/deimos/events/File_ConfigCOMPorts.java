/* Class name: File_ConnectToMARS
 * File name:  File_ConnectToMARS.java
 * Created:    14-Jul-2008 12:18:05
 * Modified:   14-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  14-Jul-2008 Initial build, copied from File_ConfigCOMPorts class
 */

package mars.deimos.events;
import java.awt.event.*;
import java.util.logging.*;
import mars.deimos.gui.COMPortConfig;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class is used to display the Configure COM ports window
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class File_ConfigCOMPorts implements ActionListener
{
  private Logger log;
  
  public File_ConfigCOMPorts()
  {
    log = LoggerFactory.getLogger("mars.deimos.events.File_ConfigCOMPorts");
    log.finest("Initialised");
  }
  
  /**
   * The event fired from the File menu to close the application
   * @param ae The triggered ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    log.config("Displaying COM port configuration window");
    COMPortConfig cpcWin = new COMPortConfig();
  }
}