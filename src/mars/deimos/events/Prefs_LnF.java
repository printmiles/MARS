/* Class name: Prefs_LnF
 * File name:  Prefs_LnF.java
 * Created:    19-Mar-2008 22:39:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.events;
import java.awt.event.*;
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
public class Prefs_LnF implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Prefs_LnF";
  private String lnfName;
  private DeimosClient deimosPane;
  private Logger log;
  private Locale locMyLocation;
  
  public Prefs_LnF(String name, DeimosClient mcParent, Locale locHere)
  {
    deimosPane = mcParent;
    lnfName = name;
    locMyLocation = locHere;
    log = LoggerFactory.getLogger(parentClassName, locMyLocation);
    log.finest("Created log for Prefs_LnF");
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      log.finest("Trying to change Look and Feel for Deimos to " + lnfName);
      Preferences prefLnF = DeimosPreferences.getDeimosPrefs();
      prefLnF.put("deimos.lnf", lnfName);
      DeimosPreferences.updateDeimosPrefs();
      UIManager.setLookAndFeel(lnfName);
      SwingUtilities.updateComponentTreeUI(deimosPane);
    }
    catch (Exception x)
    {
      log.throwing(parentClassName, "actionPerformed(ActionEvent)", x);
      DeimosResourceBundle drbLnF = new DeimosResourceBundle(locMyLocation);
      deimosPane.setStatus(drbLnF.getRBString("deimos.errors.LnFCouldNotBeApplied","Look and Feel could not be applied to the GUI."));
    }
    log.finest("Successfully change look and feel");
  }
}