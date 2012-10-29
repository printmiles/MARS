/* Class name: File_Save
 * File name:  File_Save.java
 * Created:    16-Mar-2008 15:11:00
 * Modified:   21-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  21-Apr-2008 Added content to write FormatDocs from a system window using the
 *        mars.mars.xml.FormatDocWriter class.
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mars.mars.gui.MarsClient;
import mars.mars.object.customisation.MarsResourceBundle;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.xml.FormatDocWriter;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 * @todo Add code for pressing the button in the application window, to allow the user to select which FormatDoc to save
 */
public class File_Save implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.File_Save";
  private Logger log;
  private String system;
  
  public File_Save()
  {
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Class created for the main application window");
    system = "## No system set ##";
  }
  
  public File_Save(String systemName)
  {
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Class created for the following system window " + systemName);
    system = systemName;
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    MarsClient mcParent = MarsClient.getMarsWindow();
    if (system.equals("## No system set ##"))
    {
      /** @todo Add code for a FormatDoc chooser */
      JOptionPane.showMessageDialog(mcParent,"Code not added for generic FormatDoc save. Use the system window button instead");
      return;
    }
    FormatDocWriter fdWriter = new FormatDocWriter(system);
    MarsResourceBundle mrbStatus = new MarsResourceBundle(mcParent.getLocale());
    
    if (fdWriter.writeFormatDoc())
    {
      mcParent.setStatus(mrbStatus.getRBString("mars.gui.client.status.FDExported","Formatting Document was successfully exported as: ") + system);
    }
    else
    {
      mcParent.setStatus(mrbStatus.getRBString("mars.gui.client.status.FDNotExported","Formatting Document was not created for ") + system);
    }
  }
}