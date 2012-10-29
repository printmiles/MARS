/* Class name: DeimosPreferences
 * File name:  DeimosPreferences.java
 * Created:    13-Mar-2008 14:01:04
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003a 09-Jun-2008 Copied from Mars version 0.003
 * 0.002a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.object.customisation;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class is used as a helper by others to access a single XML file for all application preferences.
 * This class is responsible for retrieving and outputting data via <code>Preferences</code> objects.
 * Classes retrieve a set of <code>Preferences</code> on calling the <code>getDeimosPrefs</code> method
 * on a new class instance. Values can then be retrieved and set through these <code>Preferences </code> instances
 * within other classes. Once another class is ready to save the values to the central preferences file then the
 * <code>updateDeimosPrefs</code> method is called and outputted to the central file for the application.
 * @see java.util.prefs.Preferences
 * @version 0.003a
 * @author Alex Harris (W4786241)
 */
public class DeimosPreferences
{
  private static final String loggerName = "mars.deimos.object.customisation.DeimosPreferences";
  private static final String prefsFileName = "deimos.prefs.xml";
  private static Preferences prefsDeimos;
  
  /**
   * Used to return all of the Preferences used across the application.
   * Preferences can then be edited locally within the calling class.
   * @return A new instance of Preferences to be used locally
   */
  public static Preferences getDeimosPrefs()
  {
    // Obtain a logger to write progress to
    Logger log = LoggerFactory.getLogger(loggerName);
    log.log(Level.FINEST, "Examining preferences to see if it's null or not.", prefsDeimos);
    if (prefsDeimos == null)
    {
      log.finest("Preferences object was null, creating a new one from the default file (deimos.prefs.xml).");
      try
      {
        // Obtain the FileInputStream for the central Preferences file
        FileInputStream fisXMLPrefs = new FileInputStream(prefsFileName);
        // Providing the file isn't null then import the preferences
        if (fisXMLPrefs != null)
        {
          prefsDeimos.importPreferences(fisXMLPrefs);
        }
      }
      catch (Exception x)
      {
        // If any errors are thrown then record them in the logger
        log.throwing("DeimosPreferences", "getDeimosPrefs()", x);
      }
    }
    // Obtain the Preferences for this class, this allows for everything to be centralised within the file
    prefsDeimos = Preferences.userNodeForPackage(DeimosPreferences.class);
    // Return the Preferences to the receiver.
    return prefsDeimos;
  }
  
  /**
   * This class provides exactly the same functions are the getDeimosPrefs method but doesn't record anything in the Log
   * <p>It should <b>only</b> be used by the LoggerFactory class to prevent a loop. All other classes should use the
   * getDeimosPrefs method to utilise the logger.
   * @return A new instance of Preferences to be used locally
   */
  public static Preferences getDeimosPrefsQuietly()
  {
    /* As mentioned in the JDoc comments, this is the same code but omitting the use of the log. It doesn't harm if
     * a class other than LoggerFactory uses it, we just lose the logs if something goes wrong.
     */
    if (prefsDeimos == null)
    {
      try
      {
        FileInputStream fisXMLPrefs = new FileInputStream(prefsFileName);
        if (fisXMLPrefs != null)
        {
          prefsDeimos.importPreferences(fisXMLPrefs);
        }
      }
      catch (Exception x)
      {
        // If any errors are thrown then put them in System.err rather than the Logger
        /** @todo Localise this string */
        System.err.println("Caught an error");
        x.printStackTrace();
      }
    }
    prefsDeimos = Preferences.userNodeForPackage(DeimosPreferences.class);
    return prefsDeimos;
  }
    
  /**
   * Used to update any preferences that may have been written to within the main XML file.
   */
  public static void updateDeimosPrefs()
  {
    // Obtain a logger to write progress to
    Logger log = LoggerFactory.getLogger(loggerName);
    try
    {
      log.finest("Exporting preferences to file: " + prefsFileName);
      // Record all of the preferences in the deimos.prefs.xml file
      prefsDeimos.exportNode(new FileOutputStream(prefsFileName));
    }
    catch (Exception x)
    {
      // If an error was encountered during the update then write the details to the log
      log.throwing("DeimosPreferences", "updateDeimosPrefs()", x);
    }
  }
}