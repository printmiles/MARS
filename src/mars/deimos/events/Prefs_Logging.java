/* Class name: Prefs_Logging
 * File name:  Prefs_Logging.java
 * Created:    19-Mar-2008 22:35:00
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 09-Jun-2008 Copied from Mars original and changed to Deimos naming
 * 0.001a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.events;
import java.awt.event.*;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002a
 * @author Alex Harris (W4786241)
 */
public class Prefs_Logging implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Prefs_Logging";
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
    Preferences prefsLocale = DeimosPreferences.getDeimosPrefs();
    // Write the log level to the preferences
    prefsLocale.put("deimos.logLevel", sLevel);
    DeimosPreferences.updateDeimosPrefs();
    log.finest("Updating all of the current loggers to work at the new level");
    // Get the LoggerFactory to update all of the instances of Logger used by the application to the new level
    LoggerFactory.setLogLevel(sLevel);
    log.finest("Log level changed in the preferences and on current loggers");
  }
}