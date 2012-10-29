/* Class name: Help_License
 * File name:  Help_License.java
 * Created:    19-Mar-2008 22:36:00
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 09-Jun-2008 Copied from Mars original and changed to Deimos naming
 * 0.001a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.events;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.deimos.gui.DeimosClient;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002a
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Help_License extends JInternalFrame implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Help_License";
  
  public Help_License()
  {
    super("Help - License", true, true);
    super.setLayer(1);
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    Logger log = LoggerFactory.getLogger(parentClassName);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = DeimosClient.getDesktopPane();
    log.finest("Building JInternalFrame");
    try
    {
      JEditorPane jepHelpText = new JEditorPane();
      jepHelpText.setEditable(false);
      Preferences pLicense = DeimosPreferences.getDeimosPrefs();
      URL urlHelp = new URL("file","localhost",pLicense.get("deimos.locale.lang", "en") + "License.html");
      jepHelpText.setPage(urlHelp);
      JScrollPane jspJEP = new JScrollPane(jepHelpText);
      this.add(jspJEP, BorderLayout.CENTER);
    }
    catch (Exception x)
    {
      log.throwing(parentClassName, "actionPerformed()", x);
    }
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(400,450);
    parent.add(this);
    this.setFrameIcon(new ImageIcon("images/help16.png"));
    this.setVisible(true);
  }
}