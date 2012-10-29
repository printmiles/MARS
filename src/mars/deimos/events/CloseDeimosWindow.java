/* Class name: CloseMARSWindow
 * File name:  CloseMARSWindow.java
 * Created:    02-Apr-2008 19:11:37
 * Modified:   28-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  28-Jul-2008 Added ability to close the DataConnectionManager and all threads stored within it
 * 0.001  02-Apr-2008 Initial build
 */

package mars.deimos.events;
import java.awt.event.*;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.reporting.DataConnectionManager;

/**
 * This class extends WindowAdapter to override the windowClosing method. It does this
 * in order to close all log files gracefully and perform garbage collection before
 * the system exits.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class CloseDeimosWindow extends WindowAdapter
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
    // Stop all of the threads first as we still need the logs running to catch any outputs required
    DataConnectionManager.removeAll();
    // Now close the loggers and their XML files
    LoggerFactory.closeLogs();
    // Run garbage collection quickly
    System.gc();
    // Exit the system with an OK closure code.
    System.exit(1);
  }
}