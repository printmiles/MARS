/* Class name: File_New
 * File name:  File_New.java
 * Created:    16-Mar-2008 15:08:00
 * Modified:   09-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  09-May-2008 Added code content
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.logging.Logger;
import mars.mars.gui.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class File_New implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.File_New";
  private Logger log;
  
  public File_New()
  {
    log = LoggerFactory.getLogger(parentClassName);
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Displaying DataSourceWindow");
    DataSourceWindow dswData = new DataSourceWindow();
  }
}