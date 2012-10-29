/* Class name: MarsClient
 * File name:  MarsClient.java
 * Created:    16-Mar-2008 11:15:18
 * Modified:   07-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.009  07-Jul-2008 Altered startApp code to bind to the RMI registry correctly.
 * 0.008  16-Jun-2008 Added code to startApp method to register start the RMI code and load a new Security Manager.
 * 0.007  16-Apr-2008 Added a static method to return the current instance and a method to add a system window 
 * 0.006  02-Apr-2008 Amended the buildGUI method to use the CloseMARSWindow class to close logs correctly.
 * 0.005  31-Mar-2008 Changed File_Open events to pass the current instance of MarsClient.
          Moved background image to its own method
 *        Added Set Log Directory location and Set XSD Location to Preferences menu
 * 0.004  19-Mar-2008 Added 32x32 icon to the setIconImage, finished basic GUI and JDoc/commenting
 * 0.003  18-Mar-2008 Changes to the Preferences menu (to allow future expansion)
          by editing Properties files.
          Also added buildToolbar() code
 * 0.002  17-Mar-2008 Further work to add menu items, events and methods
 * 0.001  16-Mar-2008 Initial build
 */

package mars.mars.gui;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import mars.mars.events.*;
import mars.mars.object.customisation.MarsPreferences;
import mars.mars.object.customisation.MarsResourceBundle;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.remote.impl.ServerFactory;
import mars.mars.remote.intf.MarsServer;

/**
 * This is the central class for MARS that is reponsible for building the GUI, providing means for visual feedback
 * to be provided to the user once the application is running, initiating the core program for remote interaction
 * and ensuring that the application closes correctly.
 * @version 0.008
 * @author Alex Harris (W4786241)
 */
public class MarsClient extends JFrame
{
  private MarsResourceBundle mrbGui;
  private Preferences pGui;
  private Logger log;
  private static JDesktopPane jdMain;
  private JMenuBar jmbMenu;
  private JLabel lblStatus;
  private static final String parentClassName = "mars.mars.gui.MarsClient";
  private static MarsClient mcThis;
  
  /**
   * Used to initiate the application when running on JRE 1.6 or higher and supporting the SplashScreen class
   * @param newSplash The splash screen being used to provide feedback to the user during the application load
   * @param mrbSplash The ResourseBundle used to provide strings to the application. This may have been localised
   */
  public MarsClient(NewSplashScreen newSplash, MarsResourceBundle mrbSplash)
  {
    // Obtain a new logger instance and write progress to it
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Setting ResourceBundle.");
    mrbGui = mrbSplash;
    // Obtain the preferences for the application
    pGui = MarsPreferences.getMarsPrefs();
    // Increase the progress on the status bar using a localised string if possible. If not then default to the English
    newSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildMenus", "Building Menus..."));
    log.entering(parentClassName, "buildMenu()");
    // Build the menubar for the application
    buildMenu();
    newSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildToolbars", "Building Toolbars..."));
    log.entering(parentClassName, "buildToolbar()");
    // Build the toolbar
    buildToolbar();
    newSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildAccessories", "Building Accessories..."));
    log.entering(parentClassName, "buildStatusBar()");
    // Build the status bar at the bottom of the application
    buildStatusBar();
    newSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.finishGUI", "Finishing GUI..."));
    log.entering(parentClassName, "buildGUI()");
    // Build the remainder of the GUI and set the JFrame properties
    buildGUI();
    newSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.startApp", "Starting Application"));
    log.entering(parentClassName, "startApp()");
    // Start anything else in the application
    startApp();
    // Increase the progress bar again to close the SplashScreen
    newSplash.increaseProgress();
  }
  
  /**
   * Used to initiate the application when running on JRE 1.5 or below and supporting the JWindow class
   * @param oldSplash The splash screen being used to provide feedback to the user during the application load
   * @param mrbSplash The ResourceBundle used to provide strings to the application. This may have been localised
   */
  public MarsClient(OldSplashScreen oldSplash, MarsResourceBundle mrbSplash)
  {
    // For more information see the constructor above
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Setting ResourceBundle.");
    mrbGui = mrbSplash;
    pGui = MarsPreferences.getMarsPrefs();
    oldSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildMenus", "Building Menus..."));
    log.entering(parentClassName, "buildMenu()");
    buildMenu();
    oldSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildToolbars", "Building Toolbars..."));
    log.entering(parentClassName, "buildToolbar()");
    buildToolbar();
    oldSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.buildAccessories", "Building Accessories..."));
    log.entering(parentClassName, "buildStatusBar()");
    buildStatusBar();
    oldSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.finishGUI", "Finishing GUI..."));
    log.entering(parentClassName, "buildGUI()");
    buildGUI();
    oldSplash.increaseProgress(mrbGui.getRBString("mars.gui.client.startApp", "Starting Application"));
    log.entering(parentClassName, "startApp()");
    startApp();
    oldSplash.increaseProgress();
  }
  
  /**
   * Builds the JMenuBar used by the application. Obtains localised strings for the menuitems, sets hotkeys
   * for the main menus and attaches listeners to all of the items.
   * <p>In the Preferences menu there are three sub menus using JRadioButtonMenuItems these display:
   * <ul>
   *  <li>Look and Feel - Obtained through the UIManager</li>
   *  <li>Locale - Explained below</li>
   *  <li>Logging Level - Not localised and hardcoded to match those used by Java</li>
   * </ul>
   * <p>The locale is obtained through querying the <code>mars.locales.numberOfLocales</code> string in the default locale
   * properties file. This returns the total number of locales stored within the system. By iterating through the property
   * strings <code>mars.locales.<i>n</i></code> and <code>mars.locales.code.<i>n</i></code> the country name and code
   * can be obtained. This allows for new languages to be added through:
   * <ul>
   *  <li>Amending the default locale and the mars.locales.numberOfLocales parameter</li>
   *  <li>Adding the new locale as an additional properties file</li>
   *  <li>Optionally adding a new icon to the images folder named as code.png. Where code is the locale code</li>
   * </ul>
   */
  private void buildMenu()
  {
    jmbMenu = new JMenuBar();
    /**************************************************************************/
    /*  FILE MENU                                                             */
    /**************************************************************************/
    // Build the File menu using localised strings
    JMenu jmFile = new JMenu(mrbGui.getRBString("mars.gui.client.menu.file", "File"));
    JMenuItem jmiConnect = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.connect", "Connect"));
    JMenuItem jmiDisconnect = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.disconnect", "Disconnect"));
    JMenuItem jmiOpen = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.openExisting", "Open Existing"));
    JMenuItem jmiRemove = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.remove", "Remove Existing"));
    JMenuItem jmiPrint = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.print", "Print"));
    JMenuItem jmiSave = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.save", "Save As CSV"));
    JMenuItem jmiExit = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.file.exit", "Exit"));
    // Set the shortcut key
    jmFile.setMnemonic((mrbGui.getRBString("mars.gui.client.menu.file.acc", "f")).charAt(0));
    // Add events to the items
    jmiConnect.addActionListener(new File_New());
    jmiDisconnect.addActionListener(new File_Disconnect());
    jmiOpen.addActionListener(new File_Open(this));
    jmiRemove.addActionListener(new File_Remove());
    jmiPrint.addActionListener(new File_Print());
    jmiSave.addActionListener(new File_Save());
    jmiExit.addActionListener(new File_Exit());
    // Add the items to the menu
    jmFile.add(jmiConnect);
    jmFile.add(jmiDisconnect);
    jmFile.add(jmiOpen);
    jmFile.add(jmiRemove);
    jmFile.addSeparator();
    jmFile.add(jmiPrint);
    jmFile.add(jmiSave);
    jmFile.addSeparator();
    jmFile.add(jmiExit);
    /**************************************************************************/
    /*  VIEW MENU                                                             */
    /**************************************************************************/
    JMenu jmView = new JMenu(mrbGui.getRBString("mars.gui.client.menu.view", "View"));
    JMenuItem jmiConnections = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.", "Connections"));
    JMenuItem jmiClients = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.", "Connected Clients"));
    jmView.setMnemonic((mrbGui.getRBString("mars.gui.client.menu.view.acc", "v")).charAt(0));
    jmiConnections.addActionListener(new View_Connections());
    jmiClients.addActionListener(new View_Clients());
    jmView.add(jmiConnections);
    jmView.add(jmiClients);
    /**************************************************************************/
    /*  PREFERENCES MENU                                                      
    /**************************************************************************/
    JMenu jmPrefs = new JMenu(mrbGui.getRBString("mars.gui.client.menu.preferences", "Preferences"));
    JMenu jmLnF = new JMenu(mrbGui.getRBString("mars.gui.client.menu.prefs.lnf", "Look and Feel"));
    JMenu jmLocale = new JMenu(mrbGui.getRBString("mars.gui.client.menu.prefs.locale", "Locale"));
    JMenu jmLogging = new JMenu(mrbGui.getRBString("mars.gui.client.menu.prefs.logging", "Logging"));
    JMenuItem jmiXSDDir = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.prefs.XSDDir","Set Schema Location"));
    JMenuItem jmiLogDir = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.prefs.LogsDir","Set Log Directory"));
    jmPrefs.setMnemonic((mrbGui.getRBString("mars.gui.client.menu.preferences.acc", "p")).charAt(0));
    // Building the Look and Feel submenu
    // Get the installed LookAndFeel (LnF) installed on the system
    UIManager.LookAndFeelInfo[] lafThisSystem = UIManager.getInstalledLookAndFeels();
    // Create a ButtonGroup to ensure that the RadioButtons work correctly
    ButtonGroup bgLnF = new ButtonGroup();
    // Iterate through the LnF array
    for (int i = 0; i < lafThisSystem.length; i++)
    {
      // Add a radio button with the LnF name to the menu
      JRadioButtonMenuItem jrbMI = new JRadioButtonMenuItem(lafThisSystem[i].getName());
      /* Attach an ActionListener to the button that knows:
       * a) The LnF class name
       * b) The parent GUI to apply the LnF to
       * c) The ResourceBundle to use for any issues that need to be echoed to the user
       */
      jrbMI.addActionListener(new Prefs_LnF(lafThisSystem[i].getClassName(), this, mrbGui.getRBLocale()));
      // Check the current LnF being iterated over to see if it matches the one saved in the preferences
      if (lafThisSystem[i].getClassName().equals(pGui.get("mars.lnf", "javax.swing.plaf.metal.MetalLookAndFeel")))
      {
        // If it matches then set the button as selected
        jrbMI.setSelected(true);
      }
      // Add the button to the ButtonGroup
      bgLnF.add(jrbMI);
      // Add the button to the Menu
      jmLnF.add(jrbMI);
    }
    // Building the Locale submenu
    // Get the number of locales supported from the Properties file. If there's a problem then return "Unknown"
    String strNumOfLangs = mrbGui.getRBString("mars.locales.numberOfLocales", "Unknown");
    Vector vecLangs = new Vector();
    // If the number of Languages within the properties file returns the default value then we can't obtain
    // the properties file and so default to en_US
    if (strNumOfLangs.equals("Unknown"))
    {
      vecLangs.add(new String[]{"US English", "en_US"});
    }
    else
    {
      // If not, then convert the string to an integer and iterate through the properties file to obtain the available locales
      Integer intLangAmount = new Integer(strNumOfLangs);
      for (int i = 1; i <= intLangAmount.intValue(); i++)
      {
        vecLangs.add(new String[]{
          // Get the formal name for the locale
          mrbGui.getRBString("mars.locales." + i, "US English"),
          // Get the locale code
          mrbGui.getRBString("mars.locales.code." + i, "en_US")
        });
      }
    }
    // Obtain the Locale saved in the preferences, if it can't be retrieved then default to en_US
    String savedLocale = new String(pGui.get("mars.locale.lang", "en") + "_" + pGui.get("mars.locale.country", "US"));
    ButtonGroup bgLocale = new ButtonGroup();
    for (int i = 0; i < vecLangs.size(); i++)
    {
      // Iterate through the available locales
      String[] lang = (String[]) vecLangs.get(i);
      // Set the radio button to contain the formal text for the locale and the icon from the images directory
      // Icons should be named in the locale code so UK English would be en_GB.png
      JRadioButtonMenuItem jrbMI = new JRadioButtonMenuItem(lang[0],new ImageIcon("images/" + lang[1] + ".png"));
      // Attach the ActionListener and notify it of the locale it's being attached to
      jrbMI.addActionListener(new Prefs_Locale(lang[1]));
      // Check the current locale against what's saved in the preferences
      if (lang[1].equals(savedLocale))
      {
        // If it's the one saved in the locale then set it as selected
        jrbMI.setSelected(true);
      }
      // Add it to the button group
      bgLocale.add(jrbMI);
      // Add to the menu
      jmLocale.add(jrbMI);
    }
    // Building the Logging submenu
    String[] strLog = new String[] {"Severe","Warning","Info","Config","Fine","Finer","Finest","All","Off"};
    ButtonGroup bgLogs = new ButtonGroup();
    for (int i = 0; i < strLog.length; i++)
    {
      JRadioButtonMenuItem jrbMI = new JRadioButtonMenuItem(strLog[i]);
      jrbMI.addActionListener(new Prefs_Logging(strLog[i]));
      if (pGui.get("mars.logLevel", "All").equals(strLog[i]))
      {
        jrbMI.setSelected(true);
      }
      bgLogs.add(jrbMI);
      jmLogging.add(jrbMI);
    }
    jmiXSDDir.addActionListener(new Prefs_SetDir("XSD", this));
    jmiLogDir.addActionListener(new Prefs_SetDir("Log", this));
    jmPrefs.add(jmLnF);
    jmPrefs.add(jmLocale);
    jmPrefs.add(jmLogging);
    jmPrefs.add(jmiXSDDir);
    jmPrefs.add(jmiLogDir);
    /**************************************************************************/
    /*  WINDOW MENU                                                           */
    /**************************************************************************/
    JMenu jmWindow = new JMenu(mrbGui.getRBString("mars.gui.client.menu.window", "Window"));
    JMenuItem jmiTile = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.window.tile", "Tile All"));
    JMenuItem jmiCascade = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.window.cascade", "Cascade"));
    JMenuItem jmiMinimise = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.window.minimise", "Minimise"));
    jmWindow.setMnemonic((mrbGui.getRBString("mars.gui.client.menu.window.acc", "w")).charAt(0));
    jmiTile.addActionListener(new Window_Tile());
    jmiCascade.addActionListener(new Window_Cascade());
    jmiMinimise.addActionListener(new Window_Minimise());
    jmWindow.add(jmiTile);
    jmWindow.add(jmiCascade);
    jmWindow.add(jmiMinimise);
    /**************************************************************************/
    /*  HELP MENU                                                             */
    /**************************************************************************/
    JMenu jmHelp = new JMenu(mrbGui.getRBString("mars.gui.client.menu.help", "Help"));
    JMenuItem jmiAbout = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.help.about", "About"));
    JMenuItem jmiLicense = new JMenuItem(mrbGui.getRBString("mars.gui.client.menu.help.license", "License"));
    jmHelp.setMnemonic((mrbGui.getRBString("mars.gui.client.menu.help.acc", "h")).charAt(0));
    jmiAbout.addActionListener(new Help_About());
    jmiLicense.addActionListener(new Help_License());
    jmHelp.add(jmiAbout);
    jmHelp.add(jmiLicense);
    /**************************************************************************/
    /*  FINISHING MENU BAR BUILD                                              */
    /**************************************************************************/
    jmbMenu.add(jmFile);
    jmbMenu.add(jmView);
    jmbMenu.add(jmPrefs);
    jmbMenu.add(jmWindow);
    jmbMenu.add(jmHelp);
  }
  
  /**
   * Builds the toolbar, adds icons, localised text and tooltips and finally attaches the relevant
   * actions from the <code>mars.mars.events</code> package
   */
  private void buildToolbar()
  {
    log.finest("Building toolbar");
    JToolBar toolBar = new JToolBar();
    JButton jbConnect, jbDisconnect, jbPrint, jbSave;
    
    log.finest("Building connect button");
    // Initialise the button and set the icon
    jbConnect = new JButton(new ImageIcon("images/Connect.png"));
    // Set the localised text for the button
    jbConnect.setText(mrbGui.getRBString("mars.gui.client.toolbar.connect", "Connect"));
    // Set the localised tooltip text and attach it to the button
    jbConnect.setToolTipText(mrbGui.getRBString("mars.gui.client.toolbar.tt.connect", "Connect to a preconfigured data source"));
    // Add the action listener
    jbConnect.addActionListener(new File_Open(this));
    
    log.finest("Building disconnect button");
    jbDisconnect = new JButton(new ImageIcon("images/Disconnect.png"));
    jbDisconnect.setText(mrbGui.getRBString("mars.gui.client.toolbar.disconnect", "Disconnect"));
    jbDisconnect.setToolTipText(mrbGui.getRBString("mars.gui.client.toolbar.tt.disconnect", "Disconnect from a data source"));
    jbDisconnect.addActionListener(new File_Disconnect());
    
    log.finest("Building print button");
    jbPrint = new JButton(new ImageIcon("images/Print.png"));
    jbPrint.setText(mrbGui.getRBString("mars.gui.client.toolbar.print", "Print"));
    jbPrint.setToolTipText(mrbGui.getRBString("mars.gui.client.toolbar.tt.print", "Print the contents of the current application window"));
    jbPrint.addActionListener(new File_Print());
    
    log.finest("Building save button");
    jbSave = new JButton(new ImageIcon("images/Save.png"));
    jbSave.setText(mrbGui.getRBString("mars.gui.client.toolbar.save", "Save"));
    jbSave.setToolTipText(mrbGui.getRBString("mars.gui.client.toolbar.tt.save", "Save the current report"));
    jbSave.addActionListener(new File_Save());
    
    log.finest("Adding buttons to the toolbar");
    toolBar.add(jbConnect);
    toolBar.add(jbDisconnect);
    toolBar.addSeparator();
    toolBar.add(jbSave);
    toolBar.addSeparator();
    toolBar.add(jbPrint);
    // Adding the toolbar to the content pane
    getContentPane().add(toolBar, BorderLayout.NORTH);
  }
  
  /**
   * Used to create the status bar at the bottom of the GUI and add it to the content pane
   */
  private void buildStatusBar()
  {
    log.finest("Building status bar");
    // Get the localised string
    lblStatus = new JLabel(mrbGui.getRBString("mars.gui.client.status.ready", "Ready"));
    // Add to the bottom of the Container
    getContentPane().add(lblStatus, BorderLayout.SOUTH);
  }
  
  /**
   * Used to initialise the remaining GUI components, set the JFrame properties,
   * apply the preferred LnF and set window properties such as size, position and 
   * attaches a customised WindowListener
   */
  private void buildGUI()
  {
    // Settting the JMenuBar
    setJMenuBar(jmbMenu);
    jdMain = new JDesktopPane();
    setBackground();
    JScrollPane jspDesktop = new JScrollPane(jdMain);
    getContentPane().add(jspDesktop,BorderLayout.CENTER);
    // Applying the preferred Look and Feel to the application. If it can't be found then revert to the Metal LnF
    String strLnF = pGui.get("mars.lnf", "javax.swing.plaf.metal.MetalLookAndFeel");
    try
    {
      log.finest("Trying to change Look and Feel for Mars to " + strLnF);
      // Apply the new LnF
      UIManager.setLookAndFeel(strLnF);
      // Refresh all of the components in the JFrame
      SwingUtilities.updateComponentTreeUI(this);
    }
    catch (Exception x)
    {
      // If there's a problem applying the LnF then log the error and notify the user in the status pane
      log.throwing(parentClassName, "buildGUI()", x);
      setStatus(mrbGui.getRBString("mars.errors.LnFCouldNotBeApplied","Look and Feel could not be applied to the GUI."));
    }
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("MARS: Multi-Application Reporting Suite");
    // Add the icons for the application. In Windows the 16px icon is used on the titlebar and
    // the 32px icon is used when Alt-Tabbing
    Vector vecIcons = new Vector();
    vecIcons.add(Toolkit.getDefaultToolkit().createImage("images/MarsIcon16.png")); // Adds a 16x16 icon
    vecIcons.add(Toolkit.getDefaultToolkit().createImage("images/MarsIcon32.png")); // Adds a 32x32 icon
    setIconImages(vecIcons);
    // Set the dimensions of the window to be the same as the current screen size but with a 10px border around the edges
    Dimension dimScreenSize = new Dimension();
    dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(10, 10, dimScreenSize.width - 20,dimScreenSize.height - 20);
    // Add the customised Window listener
    addWindowListener(new CloseMARSWindow());
    // Set the GUI to visible
    setVisible(true);
  }
  
  
  /**
   * Used for initialising the application components other than the GUI such as Threads, RMI Naming etc
   * The RMIRegistry is assumed to be located at: 127.0.0.1:1099
   */
  private void startApp()
  {
    try
    {
      System.out.println("Attempting object to rmi registry bind");
      ServerFactory sfMars = new ServerFactory();
      MarsServer msMars = (MarsServer) UnicastRemoteObject.exportObject(sfMars);
      // Finds the RMIRegistry instance on 127.0.0.1:1099 (Port 1099 on localhost, this machine)
      Registry reg = LocateRegistry.getRegistry();
      log.config("Binding object with a name of MarsServer");
      reg.rebind("MarsServer", msMars);
      log.config("Object bound to RMI registry. Ready to accept connections on the default port (1099)");
      System.out.println("Binding done");
    }
    catch (RemoteException rX)
    {
      System.out.println("RMI Exception caught");
      rX.printStackTrace();
      log.throwing(parentClassName, "startApp()", rX);
    }
    catch (Exception x)
    {
      System.out.println("Problems with binding");
      x.printStackTrace();
      log.severe("Problem starting the application");
      log.throwing(parentClassName, "startApp()", x);
    }
  }
  
  /**
   * Used to return the JDesktopPane for the application so that new windows can be manipulated within external code
   * @return The JDesktopPane used by the application for display of sub-components
   */
  public static JDesktopPane getDesktopPane()
  {
    return jdMain;
  }
  
  /**
   * Used to place the identifying application logo in the bottom left-hand corner
   */
  private void setBackground()
  {
    Dimension dimScreenSize = new Dimension();
    dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    // Set the background image of the JDesktopPane
    ImageIcon iiB = new ImageIcon("images/mback.png");
    JLabel lblBackground = new JLabel(iiB);
    lblBackground.setSize(iiB.getIconWidth() + 20, dimScreenSize.height-iiB.getIconHeight());
    lblBackground.setVerticalAlignment(SwingConstants.BOTTOM);
    jdMain.add(lblBackground, BorderLayout.WEST, new Integer(Integer.MIN_VALUE));
  }
  
  /**
   * Used to update the status text shown in the bottom left-hand corner of the application. Strings
   * passed to this method should have been localised by the passing method.
   * @param newStatus The new text to be displayed in the status field at the bottom of the screen
   */
  public void setStatus(String newStatus)
  {
    log.finest("Setting status to " + newStatus);
    lblStatus.setText(newStatus);
  }
  
  /**
   * Used for other classes to obtain a copy of the current Mars application window
   * @return The Mars window that is being displayed.
   */
  public static MarsClient getMarsWindow()
  {
    return mcThis;
  }
  
  /**
   * Adds a new system window to the GUI. This method calls the SystemWindow class which
   * is responsible for the window, thread creation and handling of data in and out.
   * @param systemName The identifying string of the system in FormatDocStore so that the relevant FormatDoc can be retrieved
   */
  public void addNewSystemWindow(String systemName)
  {
    log.finest("Creating new system window for " + systemName);
    SystemWindow sWin = new SystemWindow(systemName);
  }
}