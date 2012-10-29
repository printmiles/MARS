/* Class name: Help_License
 * File name:  Help_License.java
 * Created:    16-Mar-2008 15:13:00
 * Modified:   20-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  20-Mar-2008 Add code for actionPerformed method and add constructor
 *        to work as a subclass of JInternalFrame
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.mars.gui.MarsClient;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Help_License extends JInternalFrame implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Help_License";
  
  public Help_License()
  {
    super("Help - License", true, true);
    super.setLayer(1);
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    Logger log = LoggerFactory.getLogger(parentClassName);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    log.finest("Building JInternalFrame");
    try
    {
      JEditorPane jepHelpText = new JEditorPane();
      jepHelpText.setEditable(false);
      Preferences pLicense = MarsPreferences.getMarsPrefs();
      URL urlHelp = new URL("file","localhost",pLicense.get("mars.locale.lang", "en") + "License.html");
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