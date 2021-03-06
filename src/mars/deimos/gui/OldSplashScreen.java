/* Class name: OldSplashScreen
 * File name:  OldSplashScreen.java
 * Created:    19-Mar-2008 21:27:00
 * Modified:   19-Mar-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.gui;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import mars.deimos.object.customisation.DeimosResourceBundle;

/**
 * This class is used for providing a splash (loading) screen for machines running JRE 1.5 or earlier.
 * It uses an instance of JWindow and a smaller logo than the equivalent class for machines running
 * JRE 1.6 (NewSplashScreen).
 * @version 0.002a
 * @author Alex Harris (W4786241)
 */
public class OldSplashScreen extends JWindow
{
  private static final Color ProgressBar_COLOUR = new Color(204,51,0);
  private static final Color Text_COLOUR = new Color(128,0,0);
  private JLabel jlText;
  private JProgressBar pbProgress;
  private Locale myLocale;
  
  /**
   * Sets the maximum number of times the progress bar will be increase and the Locale to be used for displayed messages
   * @param maximumValue The maximum number of times the progress bar will be increased
   * @param locHere The locale to be used for displaying messages to the user
   */
  public OldSplashScreen(int maximumValue, Locale locHere)
  {
    myLocale = locHere;
    createSplash(maximumValue);
  }
  
  /**
   * Creates the splash screen using the stored image as a background with a progress bar and area for text.
   * The code also centres the Splash Screen in the desktop.
   * <p>This code was adapted from:
   * <i>Java Swing, 2nd Edition</i>, Cole B., Eckstein R., Elliott J., Loy M. and Wood D.,
   * <i>O'Reilly</i>, 2002, ISBN: 0-596-00408-7, Section: 8.5
   * @param maxVal The maximum number of times the progress bar will be increased
   */
  private void createSplash(int maxVal)
  {
    JPanel content = (JPanel) getContentPane();
    content.setBackground(Color.WHITE);
    // Get the splash screen image
    ImageIcon iiDeimos = new ImageIcon("images/dsplash.1.5.png");

    // Get the current screen size
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    
    // Set the bounds of the window, adjusting for the size of the splash image
    int x = (screen.width - iiDeimos.getIconWidth())/2;
    int y = (screen.height - iiDeimos.getIconHeight())/2;
    // Set the position on screen
    setBounds(x,y,iiDeimos.getIconWidth(), iiDeimos.getIconHeight() + 30);

    // Create the jlSplash with the splash screen image
    JLabel jlSplash = new JLabel(iiDeimos);
    // Create the panel for the bottom of the window
    JPanel jpInfo = new JPanel(new GridLayout(2,1));
    
    // Fetch the localised string for building the SplashScreen
    DeimosResourceBundle drbStrings = new DeimosResourceBundle(myLocale);
    jlText = new JLabel(drbStrings.getRBString("deimos.gui.splash.load", "Loading..."), JLabel.CENTER);
    // Set the text properties
    jlText.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    jlText.setForeground(Text_COLOUR);
    jlText.setBackground(Color.WHITE);
    // Set the progress bar
    pbProgress = new JProgressBar();
    pbProgress.setMaximum(maxVal);
    pbProgress.setForeground(ProgressBar_COLOUR);
    pbProgress.setBorderPainted(false);
    pbProgress.setBackground(Color.WHITE);
    // Add components to the ContentPane
    jpInfo.add(jlText);
    jpInfo.add(pbProgress);
    content.add(jlSplash, BorderLayout.CENTER);
    content.add(jpInfo, BorderLayout.SOUTH);

    // Display the SplashScreen
    setVisible(true);
  }
  
  /**
   * Increase the progress bar value by one but don't change the displayed text
   */
  public void increaseProgress()
  {
    int newVal = pbProgress.getValue() + 1;
    if (newVal <= pbProgress.getMaximum())
    {
      pbProgress.setValue(newVal);
    }
    else
    {
      this.dispose();
    }
  }
  
  /**
   * Increase the progress bar by one and change the text displayed
   * @param newText The new text to be displayed
   */
  public void increaseProgress(String newText)
  {
    jlText.setText(newText);
    increaseProgress();
  } 
}