/* Class name: File_Exit
 * File name:  File_Exit.java
 * Created:    16-Mar-2008 15:07:00
 * Modified:   03-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  03-Apr-2008 Added code for the class and JDoc
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class is used to close the system from the File menu
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class File_Exit implements ActionListener
{ 
  /**
   * The event fired from the File menu to close the application
   * @param ae The triggered ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    LoggerFactory.closeLogs();
    System.gc();
    System.exit(1);
  }
}