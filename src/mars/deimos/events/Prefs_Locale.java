/* Class name: Prefs_Locale
 * File name:  Prefs_Locale.java
 * Created:    19-Mar-2008 22:36:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.events;
import java.awt.event.*;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.customisation.DeimosResourceBundle;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002a
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Prefs_Locale implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Prefs_Locale";
  private String strCountry, strLang, strLocale;
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
        log.config("Could not set new locale correctly, there weren't the right amount of tokens within the string." + strTokens);
        break;
      }
    }
    log = LoggerFactory.getLogger(parentClassName, newLocale);
    log.finest("New locale created as: " + newLocale.getDisplayLanguage(new Locale("en")) + newLocale.getDisplayCountry(new Locale("en")));
    DeimosResourceBundle drbLocale = new DeimosResourceBundle(newLocale);
    Preferences prefsLocale = DeimosPreferences.getDeimosPrefs();
    prefsLocale.put("deimos.locale.lang", newLocale.getLanguage());
    prefsLocale.put("deimos.locale.country", newLocale.getCountry());
    DeimosPreferences.updateDeimosPrefs();
    JOptionPane.showMessageDialog(null,
            drbLocale.getRBString("deimos.gui.client.locale.changed","You have successfully changed the application locale to:") + "\n" +
            drbLocale.getRBString("deimos.gui.client.locale.language","Language") + " - " + newLocale.getDisplayLanguage(newLocale) + "\n" +
            drbLocale.getRBString("deimos.gui.client.locale.country","Country") + " - " + newLocale.getDisplayCountry(newLocale) + "\n" +
            drbLocale.getRBString("deimos.gui.client.locale.restart","You should restart the application for it to take effect.")
            );
  }
}