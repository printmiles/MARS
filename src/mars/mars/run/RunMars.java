/* Class name: RunMars
 * File name:  RunMars.java
 * Created:    12-Mar-2008 17:49:41
 * Modified:   19-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.007  19-Jun-2008 Added support for SecurityManager on the application startup
 * 0.006  17-Mar-2008 Added JDoc for the main method
 * 0.005  16-Mar-2008 Amended the starting parameters for MarsClient and the SplashScreens
 * 0.004  15-Mar-2008 Added preference support for Locales and OldSplashScreen.
 * 0.003  14-Mar-2008 Added support for Locales and NewSplashScreen. Included the class JavaDoc
 * 0.002  13-Mar-2008 Added logger and OldSplashScreen
 * 0.001  12-Mar-2008 Initial build
 */

package mars.mars.run;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import mars.mars.gui.MarsClient;
import mars.mars.gui.NewSplashScreen;
import mars.mars.gui.OldSplashScreen;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.customisation.MarsResourceBundle;

/**
 * This class is reponsible for the loading of the application. It handles the initialisation
 * of Loggers and Locales used to present localised Splash Screens to the user.
 * <p>The application defaults to using the NewSplashScreen class which is initialised through the
 * inclusion of the <code>SplashScreen-Image: </code> statement in the JAR file manifest.
 * The class then attempts to retrieve the Graphics Area for painting.
 * If it cannot obtain this then the class stores a <code>isConfigured() = false</code> value.
 * This can be examined and an <code>OldSplashScreen</code> class loaded instead. The <code>OldSplashScreen</code>
 * class uses a slightly different image projected onto an instance of <code>JWindow</code> to provide a similar effect.
 * <p>Once the splash screens are loaded, they are passed to the <code>MarsClient</code> class for building
 * the main GUI and further updates.
 * @version 0.007
 * @author Alex Harris (W4786241)
 */
public class RunMars
{
  /**
   * Runs the Mars application after initialising the logger, associated classes and splash screen.
   * For more information, refer to the main class description.
   * @param args Any user supplied arguments. These are ignored during execution
   */
  public static void main(String[] args)
  {
    Logger log = LoggerFactory.getLogger("mars.mars.run.RunMars");
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
    // Get the preferences file for MARS
    Preferences prefUser = MarsPreferences.getMarsPrefs();
    // Set the country and language to those value stored in the preferences.
    // If they cannot be retrieved then default to the System properties.
    String strCountry = prefUser.get("mars.locale.country", System.getProperty("user.country"));
    String strLanguage = prefUser.get("mars.locale.lang", System.getProperty("user.language"));
    // Create a Locale based on the country and language retrieved from the system
    Locale locMe = new Locale(strLanguage, strCountry);
    // Create a new instance of NewSplashScreen
    NewSplashScreen newSplash = new NewSplashScreen(6, locMe);
    if (newSplash.isConfigured())
    {
      MarsResourceBundle mrbSplash = new MarsResourceBundle(locMe);
      newSplash.increaseProgress(mrbSplash.getRBString("mars.gui.splash.gotPreferences","Loading Preferences..."));
      MarsClient mcMainClient = new MarsClient(newSplash, mrbSplash);
    }
    else
    {
      OldSplashScreen oldSplash = new OldSplashScreen(6, locMe);
      MarsResourceBundle mrbSplash = new MarsResourceBundle(locMe);
      oldSplash.increaseProgress(mrbSplash.getRBString("mars.gui.splash.gotPreferences","Loading Preferences..."));
      MarsClient mcMainClient = new MarsClient(oldSplash, mrbSplash);
    }
  }
}