/* Class name: Prefs_SetDir
 * File name:  Prefs_SetDir.java
 * Created:    31-Mar-2008 14:35:55
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 09-Jun-2008 Moved from Mars to Deimos and renamed
 * 0.001  31-Mar-2008 Initial build
 */

package mars.deimos.events;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.deimos.gui.DeimosClient;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.customisation.DeimosResourceBundle;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002a
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Prefs_SetDir implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Prefs_SetDir";
  private DeimosClient deimosPane;
  private Logger log;
  private String type;
  
  public Prefs_SetDir(String dirType, DeimosClient dcParent)
  {
    deimosPane = dcParent;
    type = dirType;
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Create log for Prefs_SetDir");
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Setting directory for " + type);
    JFileChooser jFC = new JFileChooser();
    // Get the preferred user Locale and create a new instance
    Preferences prefDir = DeimosPreferences.getDeimosPrefs();
    String strCountry = prefDir.get("deimos.locale.country", System.getProperty("user.country"));
    String strLanguage = prefDir.get("deimos.locale.lang", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    // Obtain a resource bundle to provide localised strings for the file chooser
    DeimosResourceBundle mrbDirs = new DeimosResourceBundle(locMe);
    jFC.setDialogTitle(mrbDirs.getRBString("deimos.gui.filechooser.dir." + type, "Select a Directory:"));
    if (type.equals("Log"))
    {
      jFC.setFileSelectionMode(jFC.DIRECTORIES_ONLY);
    }
    File fDir = new File(prefDir.get("deimos.dir." + type, System.getProperty("user.dir")));
    jFC.setSelectedFile(fDir);
    log.finest("Displaying FileChooser");
    int returnVal = jFC.showOpenDialog(deimosPane);
    if (returnVal == 1)
    {
      // User cancelled the FileChooser dialog
      log.finest("User cancelled the file chooser");
      return;
    }
    fDir = jFC.getSelectedFile();
    if (type.equals("Log"))
    {
      prefDir.put("deimos.dir." + type, fDir.getPath() + System.getProperty("file.separator"));
    }
    else
    {
      prefDir.put("deimos.dir." + type, fDir.getPath());
    }
    DeimosPreferences.updateDeimosPrefs();
    log.config("Directory has been set to " + fDir.getAbsolutePath() + " for deimos.dir." + type);
  }
}