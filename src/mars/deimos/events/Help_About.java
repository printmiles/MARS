/* Class name: Help_About
 * File name:  Help_About.java
 * Created:    19-Mar-2008 22:34:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.events;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.IOException;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.gui.DeimosClient;

/**
 * Provides a Help JInternalFrame used to display information about the application to the user.
 * @version 0.002a
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Help_About extends JInternalFrame implements ActionListener
{
  private static final String parentClassName = "mars.deimos.events.Help_About";
  
  public Help_About()
  {
    super("Help - About",true, true);
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
      URL urlHelp = new URL("file","localhost","helpAboutDeimos.html");
      jepHelpText.setPage(urlHelp);
      JScrollPane jspJEP = new JScrollPane(jepHelpText);
      this.add(jspJEP, BorderLayout.CENTER);
    }
    catch (IOException ioX)
    {
      log.throwing(parentClassName, "actionPerformed()", ioX);
      System.err.print(ioX);
    }
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setSize(400, 450);
    parent.add(this);
    this.setVisible(true);
  }
}