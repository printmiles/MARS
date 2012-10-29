/* Class name: Prefs_Logging
 * File name:  Prefs_Logging.java
 * Created:    16-Mar-2008 15:16:00
 * Modified:   02-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  02-Apr-2008 Fixed logging event and added JDoc and comments
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class Prefs_Logging implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Prefs_Logging";
  private String sLevel;
  private Logger log;
  
  public Prefs_Logging(String logLevel)
  {
    // Get an instance of logger for recording events
    log = LoggerFactory.getLogger(parentClassName);
    // Set the log level
    sLevel = logLevel;
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Logger level has been requested to be changed to a new level: " + sLevel);
    log.finest("Writing new log level to preferences");
    // Obtain the application preferences
    Preferences prefsLocale = MarsPreferences.getMarsPrefs();
    // Write the log level to the preferences
    prefsLocale.put("mars.logLevel", sLevel);
    MarsPreferences.updateMarsPrefs();
    log.finest("Updating all of the current loggers to work at the new level");
    // Get the LoggerFactory to update all of the instances of Logger used by the application to the new level
    LoggerFactory.setLogLevel(sLevel);
    log.finest("Log level changed in the preferences and on current loggers");
  }
}