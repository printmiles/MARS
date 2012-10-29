/* Class name: CloseMARSWindow
 * File name:  CloseMARSWindow.java
 * Created:    02-Apr-2008 19:11:37
 * Modified:   02-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  02-Apr-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class extends WindowAdapter to override the windowClosing method. It does this
 * in order to close all log files gracefully and perform garbage collection before
 * the system exits.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class CloseMARSWindow extends WindowAdapter
{ 
  /**
   * This method is called when the MARS application window is requested to shut
   * The method ensures that all of the Logger instances created by the application
   * during its run-time are closed gracefully otherwise they can't be opened
   * correctly at the closing tag is usually missing.
   * @param e The triggered WindowEvent
   */
  public void windowClosing(WindowEvent e)
  {
    LoggerFactory.closeLogs();
    System.gc();
    System.exit(1);
  }
}