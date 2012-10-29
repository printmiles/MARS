/* Class name: ODBCReader
 * File name:  ODBCReader.java
 * Created:    11-May-2008 11:47:50
 * Modified:   11-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  11-May-2008 Initial build
 */

package mars.mars.jdbc;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;

/**
 * This class currently does not have any JavaDoc comments included
 * <p>This class adapts the code from the RegQuery class found at:
 * <p>http://www.rgagnon.com/javadetails/java-0480.html <i>[Accessed 11th May 2008]</i>
 * <p>The adapted code can be seen in:
 * <ul>
 *   <li>The 3 (three) private final string declarations</li>
 *   <li>The getRegistryKeyContents() method</li>
 *   <li>The StreamReader inner class</li>
 * </ul>
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class ODBCReader
{
  private final String REGQUERY_UTIL = "reg query ";
  private final String ODBC_SYSTEM_DSN_CMD = REGQUERY_UTIL + "\"HKLM\\SOFTWARE\\ODBC\\ODBC.INI\\ODBC Data Sources\" /s";
  private final String ODBC_USER_DSN_CMD = REGQUERY_UTIL + "\"HKCU\\SOFTWARE\\ODBC\\ODBC.INI\\ODBC Data Sources\" /s";
  private final String parentClassName = "mars.mars.jdbc.ODBCReader";
  private Logger log;
  
  public ODBCReader()
  {
    log = LoggerFactory.getLogger(parentClassName);
  }
  
  /**
   * Returns a Vector containing the DSN names, type (user) and driver name
   * @return The vector containing String[] instances with values of the DSN name, type and driver respectively
   * @see java.util.Vector
   */
  public Vector getODBCUserDSNs()
  {
    Vector vecReturn = new Vector();
    Vector vecTemp = new Vector();
    vecTemp = getRegistryKeyContents(ODBC_USER_DSN_CMD);
    if (vecTemp == null)
    {
      return null;
    }
    for (int i = 0; i < vecTemp.size(); i++)
    {
      String[] strTemp = new String[2];
      String[] strDest = new String[3];
      strTemp = (String[]) vecTemp.get(i);
      strDest[0] = strTemp[0];
      strDest[1] = "User DSN";
      strDest[2] = strTemp[1];
      vecReturn.add(strDest);
      log.finest("Found a User DSN: " + strDest);
    }
    return vecReturn;
  }
  
  /**
   * Returns a Vector containing the DSN names, type (system) and driver name
   * @return The vector containing String[] instances with values of the DSN name, type and driver respectively
   * @see java.util.Vector
   */
  public Vector getODBCSystemDSNs()
  {
    Vector vecReturn = new Vector();
    Vector vecTemp = new Vector();
    vecTemp = getRegistryKeyContents(ODBC_SYSTEM_DSN_CMD);
    if (vecTemp == null)
    {
      return null;
    }
    for (int i = 0; i < vecTemp.size(); i++)
    {
      String[] strTemp = new String[2];
      String[] strDest = new String[3];
      strTemp = (String[]) vecTemp.get(i);
      strDest[0] = strTemp[0];
      strDest[1] = "System DSN";
      strDest[2] = strTemp[1];
      vecReturn.add(strDest);
      log.finest("Found a System DSN: " + strDest);
    }
    return vecReturn;
  }
  
  /**
   * Used as a single method to retrieve both user and system DSNs by calling both
   * methods and merging the returned vectors.
   * @return The combined vector of both user and system DSNs.
   */
  public Vector getAllDSNs()
  {
    Vector vecUser = new Vector();
    Vector vecAll = new Vector();
    
    vecAll = getODBCSystemDSNs();
    vecUser = getODBCUserDSNs();
    
    for (int i = 0; i < vecUser.size(); i++)
    {
      String[] strTemp = new String[3];
      strTemp = (String[]) vecUser.get(i);
      vecAll.add(strTemp);
    }
    
    return vecAll;
  }
  
  /**
   * Obtains the contents of a key within the Windows Registry. Uses the 
   * <code>reg query</code> command to retrieve the data and this then formats
   * the returned string to provide a vector containing names and values of items
   * under the key.
   * <p>This code has been adapted from the named source.
   * @param registryCommand The registry key to explorer. Only works with a key (directory)
   * within the registry with no sub-keys.
   * @return A vector containing instances of String[] containing the key name and value respectively
   * @see java.util.Vector
   */
  private Vector getRegistryKeyContents(String registryCommand)
  {
    try
    {
      // This executes the reg application on a command line
      Process process = Runtime.getRuntime().exec(registryCommand);
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();
      String result = reader.getResult();
      Vector vecResults = new Vector();
      StringTokenizer stRegVals = new StringTokenizer(result,"\r\n");
      while (stRegVals.hasMoreTokens())
      {
        String regKey = stRegVals.nextToken();
        if (regKey.startsWith("    "))
        {
          regKey = regKey.trim();
          String toRemove = new String("    REG_SZ    ");
          String keyName = regKey.substring(0, regKey.indexOf(toRemove));
          String keyValue = regKey.substring(keyName.length() + toRemove.length());
          String[] odbcDSN = new String[2];
          odbcDSN[0] = keyName;
          odbcDSN[1] = keyValue;
          vecResults.add(odbcDSN);
        }
      }
      return vecResults;
    }
    catch (Exception e)
    {
      log.throwing(parentClassName, "getRegistryKeyContents()", e);
      return null;
    }
  }

  /**
   * Inner class used to read from the InputStream associated with the command line.
   * <p>This code has been adapted from the named source.
   */
  class StreamReader extends Thread
  {
    private InputStream is;
    private StringWriter sw;

    StreamReader(InputStream is) {
      this.is = is;
      sw = new StringWriter();
    }

    public void run()
    {
      try
      {
        int c;
        while ((c = is.read()) != -1)
        {
          sw.write(c);
        }
      }
      catch (IOException e)
      {
        log.throwing(parentClassName, "StreamReader.run()", e);
      }
    }

    String getResult()
    {
      return sw.toString();
    }
  }
}