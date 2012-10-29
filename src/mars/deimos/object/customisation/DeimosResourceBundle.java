/* Class name: DeimosResourceBundle
 * File name:  DeimosResourceBundle.java
 * Created:    19-Mar-2008 21:34:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.object.customisation;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001a
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class DeimosResourceBundle
{
  private Locale thisLocale;
  
  public DeimosResourceBundle()
  {
    // Default constructor
  }
  
  public DeimosResourceBundle(Locale locHere)
  {
    thisLocale = locHere;
  }
  
  public String getRBString(String key, String defVal)
  {
    ResourceBundle rb = ResourceBundle.getBundle("mars.deimos.Deimos");
    if (thisLocale != null)
    {
      rb = ResourceBundle.getBundle("mars.deimos.Deimos", thisLocale);
    }
    try
    {
      defVal = rb.getString(key);
    }
    catch (Exception x)
    {
      // Do nothing as it should return the default value
    }
    return defVal;
  }
  
  public Locale getRBLocale()
  {
    return thisLocale;
  }
}