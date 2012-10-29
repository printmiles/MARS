/* Class name: Prefs_LnF
 * File name:  Prefs_LnF.java
 * Created:    16-Mar-2008 15:14:00
 * Modified:   17-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  17-Mar-2008 Add code for changing the Look and Feel of the application
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.mars.gui.MarsClient;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.customisation.MarsResourceBundle;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Prefs_LnF implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Prefs_LnF";
  private String lnfName;
  private MarsClient marsPane;
  private Logger log;
  private Locale locMyLocation;
  
  public Prefs_LnF(String name, MarsClient mcParent, Locale locHere)
  {
    marsPane = mcParent;
    lnfName = name;
    locMyLocation = locHere;
    log = LoggerFactory.getLogger(parentClassName, locMyLocation);
    log.finest("Created log for Prefs_LnF");
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      log.finest("Trying to change Look and Feel for Mars to " + lnfName);
      Preferences prefLnF = MarsPreferences.getMarsPrefs();
      prefLnF.put("mars.lnf", lnfName);
      MarsPreferences.updateMarsPrefs();
      UIManager.setLookAndFeel(lnfName);
      SwingUtilities.updateComponentTreeUI(marsPane);
    }
    catch (Exception x)
    {
      log.throwing(parentClassName, "actionPerformed(ActionEvent)", x);
      MarsResourceBundle mrbLnF = new MarsResourceBundle(locMyLocation);
      marsPane.setStatus(mrbLnF.getRBString("mars.errors.LnFCouldNotBeApplied","Look and Feel could not be applied to the GUI."));
    }
    log.finest("Successfully change look and feel");
  }
}