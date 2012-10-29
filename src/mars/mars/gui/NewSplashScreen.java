/* Class name: NewSplashScreen
 * File name:  NewSplashScreen.java
 * Created:    14-Mar-2008 16:42:53
 * Modified:   08-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  08-Apr-2008 Adjusted static variables to alter position of text and progress bar
 * 0.002  03-Apr-2008 Added JDoc and comments. Changed renderSplashFrame() method
 *        to private visibility as it doesn't need to be used outside of this class
 * 0.001  14-Mar-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.util.Locale;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.customisation.MarsResourceBundle;

/**
 * This class is used initially by the <code>RunMars</code> class. It assumes that
 * the <code>SplashScreen-Image: </code> statement has been included in the JAR
 * file running the code. This then tries to obtain the Graphics2D class created by
 * running that class in J2SE 1.6. If the user is running this code on a lower version, then
 * the Graphics2D object returns null. The code returns execution to the receiver which
 * can then load the <code>OldSplashScreen</code> class to present an alternative
 * splash screen to the user.
 * <p>NOTE: When running this code through the NetBeans IDE the way that NetBeans creates
 * and uses the JAR file leads to this class not detecting the Graphics2D instance and so
 * showing the "compatible" splash screen instead of this one.
 * <p>This code was adapted from:
 * <i>New Splash-Screen Functionality in Java SE 6</i>, Sun Microsystems, Available online at: 
 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/splashscreen/
 * , [Accessed 3rd April 2008]
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @see java.awt.Graphics2D
 */
public class NewSplashScreen
{
  private static final int WIDTH = 750;                        // Width of the image used for the splash screen
  private static final int HEIGHT = 375;                       // Height of the image used
  private static final int X = 10;                             // X position of the top corner of the square to be refreshed (for updates)
  private static final int Y = 10;                             // Y position of the refresh square
  private static final int ProgressBar_WIDTH = 340;            // Width of the rectangle to be used as a progress bar
  private static final int ProgressBar_HEIGHT = 20;            // Height of the progress bar rectangle
  private static final int ProgressBar_Y = 270;                // How far down should the progress bar be displayed
  private static final Color ProgressBar_COLOUR = Color.WHITE; // The colour of the progress bar
  private static final int Text_X = 30;                        // The X position text position
  private static final int Text_Y = 250;                       // The Y text position
  private static final Color Text_COLOUR = Color.WHITE;        // The text colour
  private static int maxVal = 0;                               // The default maximum value for the progress bar
  private static int curVal = 0;                               // The default current value for the progress bar
  private static String textVal;                               // The text to be displayed on the splash screen
  private static SplashScreen splash;                          // The SplashScreen itself
  private static Graphics2D g;                                 // The Graphics2D object from the SplashScreen
  private static boolean configured = true;                    // Whether the SplashScreen has been correctly configured (that the runtime is 1.6 or higher)
  
  /**
   * Initialises the class and sets the maximum value of the progress bar displayed
   * and obtains resources that are suitable for the supplied Locale.
   * @param maximumValue The maximum value for the progress bar on the splash screen
   * @param locMe The locale that should be used to provide localised strings for display to the user
   * @see java.util.Locale
   */
  public NewSplashScreen(int maximumValue, Locale locMe)
  {
    maxVal = maximumValue;
    // Create a new logger and get a resource bundle
    Logger log = LoggerFactory.getLogger("mars.mars.gui.NewSplashScreen");
    MarsResourceBundle mrbSplash = new MarsResourceBundle(locMe);
    textVal = mrbSplash.getRBString("mars.gui.splash.load", "Loading...");
    log.finest("Splash Screen created for JSE 1.6 or higher, attempting to obtain the SplashScreen class.");
    // Try to obtain the SplashScreen class from the environment
    splash = SplashScreen.getSplashScreen();
    if (splash == null)
    {
      // If the splashscreen is null then we're running on J2SE 1.5 or earlier.
      log.finest("SplashScreen couldn't be obtained, must be running on pre 1.6.");
      System.err.println(mrbSplash.getRBString("mars.errors.SplashScreenNotCompatible","Splash screen is null. May be running on JSE 1.5 or earlier."));
      configured = false;
      return;
    }
    g = (Graphics2D) splash.createGraphics();
    if (g == null)
    {
      // If there was some kind of problem getting the graphics class from the splash screen then quit
      log.finest("Graphics2D class could not be obtained, currently null.");
      System.err.println(mrbSplash.getRBString("mars.errors.Graphics2DNotFound", "Cannot obtain the Splash screen graphics2D class. It is currently null."));
      configured = false;
      return;
    }
  }
  
  /**
   * Returns whether the class has been executed and configured correctly or not. Issues obtaining the
   * SplashScreen class or its instance of Graphics2D would return false, otherwise it should return
   * true.
   * @return Whether the class is configured correctly
   */
  public boolean isConfigured()
  {
    return configured;
  }
  
  /**
   * Increases the value of the progress bar on the splash screen without changing the
   * text displayed. If it has exceeded the maximum value then it closes the splash screen.
   * This means that there is no <code>.close()</code> method in this class. Instead the
   * maximum value of the progress bar is set in the constructor and then this method is 
   * repeatedly called until the maximum value is exceeded and the screen closed automatically.
   */
  public void increaseProgress()
  {
    curVal++;
    if (curVal <= maxVal)
    {
      renderSplashFrame();
    }
    else
    {
      splash.close();
    }
  }
  
  /**
   * Overloads the <code>increaseProgress()</code> method and allows for the text on
   * the splash screen to be changed to the new string. The same principle around
   * increasing the progress bar value and then closing the splash screen is applied
   * here from the <code>increaseProgress()</code> method.
   * @param newText The new text to be displayed on the splash screen
   * @see #increaseProgress()
   */
  public void increaseProgress(String newText)
  {
    textVal = newText;
    increaseProgress();
  }
  
  /**
   * This method is used to draw an invisible box over the splash screen image, write
   * text and add the progress bar (as a solid filled rectangle) to this box and
   * display it. Subsequent calls to this method will wipe the contents of the previous
   * screen and then display a new box otherwise the text garbles and progress bar
   * becomes unclear.
   */
  private void renderSplashFrame()
  {
    // Sets the colour of the box to transparent
    g.setComposite(AlphaComposite.Clear);
    // Sets the bounds of the box to match those of the splash screen image
    g.fillRect(X, Y, WIDTH, HEIGHT);
    g.setPaintMode();
    // Sets the text colour
    g.setColor(Text_COLOUR);
    // Turns on anti-aliasing on for the text
    g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON));
    // Sets the text properties
    g.setFont(new Font("Sans Serif",Font.BOLD,12));
    // Sets the text string and position to be displayed
    g.drawString(textVal, Text_X, Text_Y);
    // Sets the progress bar colour
    g.setColor(ProgressBar_COLOUR);
    // Sets the progress bar position and size
    g.fillRect(Text_X, ProgressBar_Y,(ProgressBar_WIDTH / maxVal)*curVal, ProgressBar_HEIGHT);
    // Updates the splashscreen with the revised Graphics2D class.
    splash.update();
  }
}