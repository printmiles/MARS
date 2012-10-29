/* Class name: FDEditor
 * File name:  FDEditor.java
 * Created:    20-May-2008 20:43:47
 * Modified:   21-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  21-May-2008 Added main code
 * 0.001  20-May-2008 Initial build
 */

package mars.temp;
import javax.swing.*;
import java.util.logging.*;
import java.util.*;
import java.util.prefs.*;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
/*
public class BasicInternalFrame extends JInternalFrame
{
  private Logger log;
  private aResourceBundle arb;
  private static final String parentClassName = "mars.something";
  
  public BasicInternalFrame()
  {
super("Configure COM Ports:", true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(parentClassName);
    // Obtain the localisation settings
    // Get the preferred user Locale and create a new instance
    Preferences prefUser = aPreferences.getPrefs();
    String strCountry = prefUser.get("aKey.aVal", System.getProperty("user.country"));
    String strLanguage = prefUser.get("aKey.aVal", System.getProperty("user.language"));
    Locale locMe = new Locale(strLanguage, strCountry);
    log.config("Got a Locale for the current session " + locMe.getLanguage() + "_" + locMe.getCountry());
    // Obtain a resource bundle to provide localised strings
    arb = new aResourceBundle(locMe);
    */
    /**************************************************************************/
    /*   New Code to be entered after this point                              */
    /**************************************************************************/
    
    /**************************************************************************/
    /*  And above this one                                                    */
    /**************************************************************************/
/*
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(600, 300);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = aClient.getDesktopPane();
    parent.add(this);
    this.setVisible(true);
  }
}*/