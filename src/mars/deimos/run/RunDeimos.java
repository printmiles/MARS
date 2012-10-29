/* Class name: RunDeimos
 * File name:  RunDeimos.java
 * Created:    19-Mar-2008 20:45:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.006a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.run;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import mars.deimos.gui.DeimosClient;
import mars.deimos.gui.NewSplashScreen;
import mars.deimos.gui.OldSplashScreen;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.customisation.DeimosResourceBundle;

/**
 * This class is reponsible for the loading of the application. It handles the initialisation
 * of Loggers and Locales used to present localised Splash Screens to the user.
 * <p>The application defaults to using the NewSplashScreen class which is initialised through the
 * inclusion of the <code>SplashScreen-Image: </code> statement in the JAR file manifest.
 * The class then attempts to retrieve the Graphics Area for painting.
 * If it cannot obtain this then the class stores a <code>isConfigured() = false</code> value.
 * This can be examined and an <code>OldSplashScreen</code> class loaded instead. The <code>OldSplashScreen</code>
 * class uses a slightly different image projected onto an instance of <code>JWindow</code> to provide a similar effect.
 * <p>Once the splash screens are loaded, they are passed to the <code>DeimosClient</code> class for building
 * the main GUI and further updates.
 * @version 0.006a
 * @author Alex Harris (W4786241)
 */
public class RunDeimos
{
  /**
   * Runs the Deimos application after initialising the logger, associated classes and splash screen.
   * For more information, refer to the main class description.
   * @param args Any user supplied arguments. These are ignored during execution
   */
  public static void main(String[] args)
  {
    Logger log = LoggerFactory.getLogger("mars.deimos.run.RunDeimos");
    // Write the current system config to the log
    log.config("Runtime: " + System.getProperty("java.runtime.name"));
    log.config("Vendor: " + System.getProperty("java.vendor"));
    log.config("Version: " + System.getProperty("java.version"));
    log.config("OS: " + System.getProperty("os.name"));
    log.config("OS Version: " + System.getProperty("os.version"));
    log.config("CPU Type: " + System.getProperty("os.arch"));
    log.config("User Name: " + System.getProperty("user.name"));
    log.config("User Directory: " + System.getProperty("user.dir"));
    log.config("Class Directory: " + System.getProperty("java.class.path"));
    log.config("Language: " + System.getProperty("user.language"));
    log.config("Country: " + System.getProperty("user.country"));
    log.finest("Starting application, obtaining preferences");
    // Get the preferences file for Deimos
    Preferences prefUser = DeimosPreferences.getDeimosPrefs();
    // Set the country and language to those value stored in the preferences.
    // If they cannot be retrieved then default to the System properties.
    String strCountry = prefUser.get("deimos.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("deimos.locale.lang", System.getProperty("user.language"));
    // Create a Locale based on the country and language retrieved from the system
    Locale locMe = new Locale(strLanguage, strCountry);
    // Create a new instance of NewSplashScreen
    NewSplashScreen newSplash = new NewSplashScreen(5, locMe);
    if (newSplash.isConfigured())
    {
      DeimosResourceBundle drbSplash = new DeimosResourceBundle(locMe);
      newSplash.increaseProgress(drbSplash.getRBString("deimos.gui.splash.gotPreferences","Loading Preferences..."));
      DeimosClient dcMainClient = new DeimosClient(newSplash, drbSplash);
    }
    else
    {
      OldSplashScreen oldSplash = new OldSplashScreen(5, locMe);
      DeimosResourceBundle drbSplash = new DeimosResourceBundle(locMe);
      oldSplash.increaseProgress(drbSplash.getRBString("deimos.gui.splash.gotPreferences","Loading Preferences..."));
      DeimosClient dcMainClient = new DeimosClient(oldSplash, drbSplash);
    }
  }
}