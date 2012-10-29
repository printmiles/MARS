/* Class name: MarsResourceBundle
 * File name:  MarsResourceBundle.java
 * Created:    13-Mar-2008 12:29:25
 * Modified:   13-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  13-Mar-2008 Initial build
 */

package mars.mars.object.customisation;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class MarsResourceBundle
{
  private Locale thisLocale;
  
  public MarsResourceBundle()
  {
    // Default constructor
  }
  
  public MarsResourceBundle(Locale locHere)
  {
    thisLocale = locHere;
  }
  
  public String getRBString(String key, String defVal)
  {
    ResourceBundle rb = ResourceBundle.getBundle("mars.mars.Mars");
    if (thisLocale != null)
    {
      rb = ResourceBundle.getBundle("mars.mars.Mars", thisLocale);
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