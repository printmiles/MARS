/* Class name: LoggerFactory
 * File name:  LoggerFactory.java
 * Created:    19-Mar-2008 20:59:00
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.011a 09-Jun-2008 Renamed LoggerFactory from LoggerControl. Copied version 0.011 from MARS and adapted
 * 0.006a 20-Mar-2008 Added setLogLevel method from MARS
 * 0.005a 19-Mar-2008 Copied from Mars original and changed to Deimos naming
 */

package mars.deimos.object.logging;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Enumeration;
import java.util.logging.*;
import java.util.prefs.Preferences;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.customisation.DeimosResourceBundle;

/**
 * This class is used to control instances of <code>Logger</code> used within Deimos.
 * A new instance of <code>Logger</code> can be obtained by passing a class name to the
 * <code>getLogger</code> method. The class name is then compared to the keys stored within
 * a Hashtable object. If the key already exists within the Hashtable then the <code>Logger</code>
 * is returned. If it is not then a new <code>Logger</code> is created.
 * <p>The files are saved using the XML format defined by Sun for log records and are stored
 * relative to the directory the code is executed from, in the Logs directory.
 * <p>Log records are named in the format of <code>classname-u.deimos.log.xml</code> where <code>parentClassName</code> is 
 * the supplied class name, which should be the fully qualified package and class name and <code>u</code> is a unique number
 * <p>The objective of this class is to reduce the overall number of logs kept within the system and to provide
 * a central point for amending settings such as log levels.
 * @see java.util.logging.Logger
 * @version 0.011a
 * @author Alex Harris (W4786241)
 */
public class LoggerFactory
{
  private static Hashtable htDeimosLoggers;
  private static DeimosResourceBundle drbRun;
  private static Logger logSelf;
  
  /**
   * Used to return an existing instance of Logger if one exists for the supplied class name.
   * Or to create a new instance, configure the FileHandler, set the file location and logging level.
   * @see java.util.logging.Logger
   * @param parentClassName The fully qualified class name identifying the Logger
   * @return The instance of Logger to be used
   */
  public static Logger getLogger(String parentClassName)
  {
    // Check to make sure that all variables are initialised, if not then initialise them
    if (htDeimosLoggers == null)
    {
      htDeimosLoggers = new Hashtable();
    }
    // Get the Preferences for Deimos so that we can read the preferred logging level
    Preferences prefLogLevel = DeimosPreferences.getDeimosPrefsQuietly();
    if (logSelf == null)
    {
      logSelf = Logger.getLogger("mars.deimos.object.customisation.LoggerFactory");
      htDeimosLoggers.put("mars.deimos.object.customisation.LoggerFactory", logSelf);
      try
      {
        FileHandler fhXMLLog = new FileHandler(getLogDir() + "mars.deimos.object.customisation.LoggerFactory-%u.deimos.log.xml");
        logSelf.addHandler(fhXMLLog);
        // Get the log level from the preferences. If it can't be obtained then default to All
        logSelf.setLevel(Level.parse((prefLogLevel.get("deimos.logLevel", "All")).toUpperCase()));
        logSelf.finest("Created new logger for the LoggerFactory class.");
        logSelf.config("Started new log");
      }
      catch (IOException ioX)
      {
        logSelf.throwing("LoggerFactory", "getLogger(String)", ioX);
        System.err.println(drbRun.getRBString("deimos.errors.CannotConfigLogger","Unable to create logger for class: ") + parentClassName);
      }
    }
    if (drbRun == null)
    {
      drbRun = new DeimosResourceBundle();
    }
    
    // Does the hashtable already contain a logger for the given classname?
    if(htDeimosLoggers.containsKey(parentClassName))
    {
      logSelf.finest("Found logger for " + parentClassName + " and returning it to the receiver.");
      return (Logger) htDeimosLoggers.get(parentClassName);
    }
    // If it doesn't then set one up using the stored preferences and
    // run-time properties (to obtain directory information).
    else
    {
      Logger logNewClass = Logger.getLogger(parentClassName);
      if (logSelf == null)
      {
        logSelf = logNewClass;
      }
      logSelf.finest("Could not find a logger for " + parentClassName + ", creating a new one.");
      try
      {
        FileHandler fhXMLLog = new FileHandler(getLogDir() + parentClassName + "-%u.deimos.log.xml");
        logNewClass.addHandler(fhXMLLog);
        // Get the log level from the preferences. If it can't be obtained then default to All
        logNewClass.setLevel(Level.parse((prefLogLevel.get("deimos.logLevel", "All")).toUpperCase()));
        logNewClass.config("Started new log");
        logSelf.finest("Created new logger for class " + parentClassName + " at location: " + getLogDir() + parentClassName + ".log.xml");
      }
      catch (IOException ioX)
      {
        logSelf.severe("Unable to create logger for class: " + parentClassName);
        logSelf.throwing("LoggerFactory", "getLogger(String)", ioX);
        System.err.println(drbRun.getRBString("deimos.errors.CannotConfigLogger","Unable to create logger for class: ") + parentClassName);
      }

      logSelf.finest("Adding new logger to the hashtable");
      htDeimosLoggers.put(parentClassName, logNewClass);
      return logNewClass;
    }
  }
  
  /**
   * As <code>getLogger(String)</code> but localises any error messages that may be returned
   * during the configuration of log files.
   * @see java.util.logging.Logger
   * @param parentClassName The fully qualified class name identifying the Logger
   * @param locMyLocation The Locale to be used for localisation of thrown error messages
   * @return The instance of Logger to be used
   */
  public static Logger getLogger(String parentClassName, Locale locMyLocation)
  {
    drbRun = new DeimosResourceBundle(locMyLocation);
    logSelf.config("Creating a localised ResourceBundle for " + locMyLocation.getDisplayName());
    return getLogger(parentClassName);
  }
  
  /**
   * Examines the application preferences and returns the localised string of the current log file directory.
   * If one cannot be found then it examines the current location the application is being run in and creates
   * a Logs directory specifically for MARS.
   * <p>The returned value is localised to the OS so Windows would return C:\Logs and Unix C:/Logs
   * @return The current directory used for Log file storage.
   */
  private static String getLogDir()
  {
    String strClassDir, strUserDir;
    strClassDir = System.getProperty("java.class.path"); // Returns the current class path
    strUserDir = System.getProperty("user.dir"); // If the class path doesn't return the directory then this will
    logSelf.finest("Got system parameters: Class path is " + strClassDir + " and User path is " + strUserDir);
    if (strClassDir.indexOf(System.getProperty("file.separator")) > -1)
    {
      // See if the class directory contains the full class path (that the string contains a file seperator character)
      // If it does then manipulate the string so that it returns the file directory but without the class file name
      strUserDir = strClassDir.substring(0, (strClassDir.lastIndexOf(System.getProperty("file.separator"))) + 1);
      strUserDir = strUserDir + "Logs" + System.getProperty("file.separator");
    }
    else
    {
      // If the class path only provide the class file name then it is being run from the same directory
      strUserDir = strUserDir + System.getProperty("file.separator") + "Logs" + System.getProperty("file.separator");
    }
    // Get the Preferences for MARS
    Preferences prefLogDir = DeimosPreferences.getDeimosPrefsQuietly();
    // See if there's already an entry for the Log files to be recorded
    // If one exists then use that, if not then use the string we've just created
    strUserDir = prefLogDir.get("deimos.dir.Log", strUserDir);
    logSelf.finest("Outputting logs to this directory: " + strUserDir);

    if (!(new File(strUserDir).exists()))  // Check to see whether the logs directory exists
    {
      logSelf.finest("Logs directory doesn't exist, trying to create the directory");
      // If it doesn't then try to create the directory
      boolean success = (new File(strUserDir)).mkdirs();
      if (!success)
      {
        // Could not create the directory so throw an exception
        logSelf.finest("Couldn't create logs directory. Notifying System.err");
        System.err.println(drbRun.getRBString("deimos.errors.FolderNotCreated", "Unable to create directory: ") + strUserDir);
      }
    }
    return strUserDir;
  }
  
  /**
   * Changes all of the Loggers stored within the classes hastable to the specified level
   * @param newLevel The new level of logging requested. This must match the string equivalent of the level itself.
   */
  public static void setLogLevel(String newLevel)
  {
    newLevel = newLevel.toUpperCase();
    Level lNew = Level.parse(newLevel.toUpperCase());
    Enumeration eHTKeys = htDeimosLoggers.keys();
    while (eHTKeys.hasMoreElements())
    {
      String strLogName = eHTKeys.nextElement().toString();
      Logger logTemp = (Logger) htDeimosLoggers.get(strLogName);
      logTemp.setLevel(lNew);
      htDeimosLoggers.put(strLogName, logTemp);
    }
  }
  
  /**
   * Close the logs gracefully and ensure that the logs has been closed correctly.
   */
  public static void closeLogs()
  {
    Enumeration eHTKeys = htDeimosLoggers.keys();
    while (eHTKeys.hasMoreElements())
    {
      Logger aLog = (Logger) htDeimosLoggers.get(eHTKeys.nextElement().toString());
      // Write a quick note to the logger before closing it
      aLog.config("Closing logger");
      // Turn the logging off
      aLog.setLevel(Level.OFF);
      // Set the logger to null which closes the file
      aLog = null;
    }
  }
}