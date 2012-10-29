/* Class name: File_Open
 * File name:  File_Open.java
 * Created:    16-Mar-2008 15:05:00
 * Modified:   31-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  31-Mar-2008 Add event code and constructor
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.mars.gui.MarsClient;
import mars.mars.object.FormatDocFileFilter;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.xml.FormatDocReader;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class File_Open implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.File_Connect";
  private static MarsClient mcParent;
  private static Logger log;
  
  public File_Open (MarsClient parent)
  {
    mcParent = parent;
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Created instance of File_Connect event");
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("File_Connect clicked");
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = MarsPreferences.getMarsPrefs();
    String strCountry = prefUser.get("mars.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("mars.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    // Obtain a resource bundle to provide localised strings for the file chooser
    MarsResourceBundle mrbConnect = new MarsResourceBundle(locMe);
    JFileChooser jFC = new JFileChooser();
    jFC.setAcceptAllFileFilterUsed(false); // Only allow those file permitted by the FileFilter
    jFC.setDialogTitle(mrbConnect.getRBString("mars.gui.filechooser.formatDoc", "Select a FormatDoc:"));
    // Set the FileFilter of the JFileChooser to select only FormatDocs (ending with .fd.xml)
    log.finest("Setting FileFilter to .fd.xml");
    jFC.setFileFilter(new FormatDocFileFilter());
    log.finest("Displaying FileChooser");
    int returnVal = jFC.showOpenDialog(mcParent);
    if (returnVal == 1)
    {
      // User cancelled the FileChooser dialog
      log.finest("User cancelled the file chooser");
      return;
    }
    File fFD = jFC.getSelectedFile();
    log.config("Loading selected FormatDoc " + fFD.getAbsolutePath());
    FormatDocReader fdrOpen = new FormatDocReader();
    /**@todo Fix the code below before going into production */
    mcParent.setStatus("Parsing FormatDoc");
    boolean result = fdrOpen.parseFormatDoc(fFD.getAbsolutePath());
    if (result)
    {
      mcParent.setStatus("Parsed document without any problems");
    }
    else
    {
      mcParent.setStatus("Errors detected in the document, see the log for more information");
    }
  }
}