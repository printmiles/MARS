/* Class name: Prefs_Locale
 * File name:  Prefs_Locale.java
 * Created:    16-Mar-2008 15:15:00
 * Modified:   17-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  17-Mar-2008 Added method code
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.event.*;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.customisation.MarsResourceBundle;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Prefs_Locale implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Prefs_Locale";
  private String strLocale;
  private Logger log;
  
  public Prefs_Locale (String locale)
  {
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Setting locale to a new value: " + locale);
    strLocale = locale;
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    StringTokenizer stLocale = new StringTokenizer(strLocale,"_");
    String[] strTokens = new String[stLocale.countTokens()];
    for (int i = 0; i <= stLocale.countTokens(); i++)
    {
      strTokens[i] = stLocale.nextToken();
    }
    Locale newLocale = new Locale("en"); // Just to give it a default value
    
    switch (strTokens.length)
    {
      case 1:
      {
        newLocale = new Locale(strTokens[0]);
        break;
      }
      case 2:
      {
        newLocale = new Locale(strTokens[0], strTokens[1]);
        break;
      }
      case 3:
      {
        newLocale = new Locale(strTokens[0], strTokens[1], strTokens[2]);
        break;
      }
      default:
      {
        log.warning("Could not set new locale correctly, there weren't the right amount of tokens within the string." + strTokens);
        break;
      }
    }
    log = LoggerFactory.getLogger(parentClassName, newLocale);
    log.finest("New locale created as: " + newLocale.getDisplayLanguage(new Locale("en")) + newLocale.getDisplayCountry(new Locale("en")));
    MarsResourceBundle mrbLocale = new MarsResourceBundle(newLocale);
    Preferences prefsLocale = MarsPreferences.getMarsPrefs();
    prefsLocale.put("mars.locale.lang", newLocale.getLanguage());
    prefsLocale.put("mars.locale.country", newLocale.getCountry());
    MarsPreferences.updateMarsPrefs();
    JOptionPane.showMessageDialog(null,
            mrbLocale.getRBString("mars.gui.client.locale.changed","You have successfully changed the application locale to:") + "\n" +
            mrbLocale.getRBString("mars.gui.client.locale.language","Language") + " - " + newLocale.getDisplayLanguage(newLocale) + "\n" +
            mrbLocale.getRBString("mars.gui.client.locale.country","Country") + " - " + newLocale.getDisplayCountry(newLocale) + "\n" +
            mrbLocale.getRBString("mars.gui.client.locale.restart","You should restart the application for it to take effect.")
            );
  }
}