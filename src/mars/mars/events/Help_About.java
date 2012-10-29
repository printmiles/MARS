/* Class name: Help_About
 * File name:  Help_About.java
 * Created:    16-Mar-2008 15:12:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  19-Mar-2008 Add code to display a non-localised Help About pane for testing
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.events;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.IOException;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.gui.MarsClient;

/**
 * Provides a Help JInternalFrame used to display information about the application to the user.
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class Help_About extends JInternalFrame implements ActionListener
{
  private static final String parentClassName = "mars.mars.events.Help_About";
  
  public Help_About()
  {
    super("Help - About",true, true);
    super.setLayer(1); 
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    Logger log = LoggerFactory.getLogger(parentClassName);
    log.finest("Building JInternalFrame");
    try
    {
      JEditorPane jepHelpText = new JEditorPane();
      jepHelpText.setEditable(false);
      URL urlHelp = new URL("file","localhost","helpAboutMars.html");
      jepHelpText.setPage(urlHelp);
      JScrollPane jspJEP = new JScrollPane(jepHelpText);
      this.add(jspJEP, BorderLayout.CENTER);
    }
    catch (IOException ioX)
    {
      log.throwing(parentClassName, "actionPerformed()", ioX);
      System.err.print(ioX);
    }
    JTextArea jtaEnvironment = new JTextArea();
    jtaEnvironment.setFont(new Font("Sans Serif", Font.PLAIN, 10));
    jtaEnvironment.append("SYSTEM PROPERTIES:");
    jtaEnvironment.append("\n~~~~~~~~~~~~~~~~~~~");
    jtaEnvironment.append("\nJava Vendor:\t" + System.getProperty("java.vendor"));
    jtaEnvironment.append("\nJava Version:\t" + System.getProperty("java.version"));
    jtaEnvironment.append("\nRunning on:\t" + System.getProperty("os.name"));
    jtaEnvironment.append("\nVersion:\t" + System.getProperty("os.version"));
    jtaEnvironment.append("\nArchitecture:\t" + System.getProperty("os.arch"));
    jtaEnvironment.append("\nAs User:\t" + System.getProperty("user.name"));
    jtaEnvironment.append("\nUser's Locale:\t" + System.getProperty("user.language") + "_" + System.getProperty("user.country"));
    jtaEnvironment.setEditable(false);
    jtaEnvironment.setTabSize(4);
    this.add(jtaEnvironment, BorderLayout.SOUTH);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setSize(400, 450);
    log.finest("Obtaining JDesktopPane");
    JDesktopPane parent = MarsClient.getDesktopPane();
    parent.add(this);
    this.setFrameIcon(new ImageIcon("images/help16.png"));
    this.setVisible(true);
  }
}