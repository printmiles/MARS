/* Class name: Prefs_SetDir
 * File name:  Prefs_SetDir.java
 * Created:    31-Mar-2008 14:35:55
 * Modified:   31-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  31-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.mars.gui.MarsClient;
import mars.mars.object.SchemaFileFilter;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.customisation.MarsResourceBundle;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Prefs_SetDir implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Prefs_SetDir";
  private MarsClient marsPane;
  private Logger log;
  private String type;
  
  public Prefs_SetDir(String dirType, MarsClient mcParent)
  {
    marsPane = mcParent;
    type = dirType;
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Create log for Prefs_SetDir");
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Setting directory for " + type);
    JFileChooser jFC = new JFileChooser();
    // Get the preferred user Locale and create a new instance
    Preferences prefDir = MarsPreferences.getMarsPrefs();
    String strCountry = prefDir.get("mars.locale.country", System.getProperty("user.country"));
    String strLanguage = prefDir.get("mars.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    // Obtain a resource bundle to provide localised strings for the file chooser
    MarsResourceBundle mrbDirs = new MarsResourceBundle(locMe);
    jFC.setDialogTitle(mrbDirs.getRBString("mars.gui.filechooser.dir." + type, "Select a Directory:"));
    if (type.equals("Log"))
    {
      jFC.setFileSelectionMode(jFC.DIRECTORIES_ONLY);
    }
    if (type.equals("XSD"))
    {
      jFC.setFileFilter(new SchemaFileFilter());
      jFC.setAcceptAllFileFilterUsed(false);
    }
    File fDir = new File(prefDir.get("mars.dir." + type, System.getProperty("user.dir")));
    jFC.setSelectedFile(fDir);
    log.finest("Displaying FileChooser");
    int returnVal = jFC.showOpenDialog(marsPane);
    if (returnVal == 1)
    {
      // User cancelled the FileChooser dialog
      log.finest("User cancelled the file chooser");
      return;
    }
    fDir = jFC.getSelectedFile();
    if (type.equals("Log"))
    {
      prefDir.put("mars.dir." + type, fDir.getPath() + System.getProperty("file.separator"));
    }
    else
    {
      prefDir.put("mars.dir." + type, fDir.getPath());
    }
    MarsPreferences.updateMarsPrefs();
    log.config("Directory has been set to " + fDir.getAbsolutePath() + " for mars.dir." + type);
  }
}