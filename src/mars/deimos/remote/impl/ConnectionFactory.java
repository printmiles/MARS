/* Class name: ConnectionFactory
 * File name:  ConnectionFactory.java
 * Created:    16-Jun-2008 20:32:32
 * Modified:   25-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  25-Jul-2008 Changed the way in which ConnectionFactory is used. Previously this was only attached
 *        to an ActionEvent but I'm now intending to use this with a Thread for data collection as well.
 * 0.003  21-Jul-2008 Added support to save IP address of MARS host in preferences.
 * 0.002  08-Jul-2008 Altered the way in which incoming MarsConnection instances are handled to fetch
 *        them from the RMIRegistry rather than from the MarsServer object.
 * 0.001  16-Jun-2008 Initial build
 */

package mars.deimos.remote.impl;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import mars.deimos.object.customisation.*;
import mars.deimos.object.logging.LoggerFactory;
import mars.mars.remote.intf.*;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.004
 * @author Alex Harris (W4786241)
 */
public class ConnectionFactory
{ 
  private MarsConnection marsXn;
  private Logger log;
  
  public ConnectionFactory()
  {
    log = LoggerFactory.getLogger("mars.deimos.remote.impl.ConnectionFactory");
    if (System.getSecurityManager() == null)
    {
      log.config("SecurityManager was not set. Configuring.");
      System.setSecurityManager(new SecurityManager());
    }
    try
    {
      String rmiName = "MarsServer";
      // Get the host name of the RMI server from the preferences
      log.finest("Obtaining preferences");
      Preferences prefs = DeimosPreferences.getDeimosPrefs();
      String host = prefs.get("deimos.config.preferredMARSHost","localhost");
      log.fine("Attempting to establish RMI connection with: " + host);
      prefs = null;
      // Find the RMI server
      Registry regRMI = LocateRegistry.getRegistry(host, 1099);
      log.config("Found RMI server");
      // Locate and cast the object
      MarsServer marsSvr = (MarsServer) regRMI.lookup(rmiName);
      log.config("Connected to MarsServer");
      // Create the hashtable we'll send to MARS
      Hashtable htDeimos = new Hashtable();
      try
      {
        htDeimos.put("ipaddress", InetAddress.getLocalHost().getHostAddress());
      }
      catch (java.net.UnknownHostException uhX)
      {
        htDeimos.put("ipaddress", "127.0.0.1");
      }      
      htDeimos.put("type", "Deimos");
      htDeimos.put("os", System.getProperty("os.name"));
      htDeimos.put("name", "None");
      htDeimos.put("time", Calendar.getInstance());
      htDeimos.put("seq", new Integer(0));
      String connectionName = marsSvr.connectToMars(htDeimos);
      marsXn = (MarsConnection) regRMI.lookup(connectionName);
      log.config("Connection to MarsConnection using the identifier: " + connectionName);
      Hashtable htServer = marsXn.getInformation();
      log.finest("MarsConnection details are:");
      log.finest("IP Address: " + ((String) htServer.get("ipaddress")));
      log.finest("Type: " + ((String) htServer.get("type")));
      log.finest("OS: " + ((String) htServer.get("os")));
      log.finest("Name: " + ((String) htServer.get("name")));
    }
    catch (Exception x)
    {
      // If a problem is detected then put it in the log files.
      log.throwing("ConnectionFactory", "init()", x);
    }
  }
  
  public void sendCOMData(String originatingPort, String sendData)
  {
    try
    {
      Hashtable htOut = new Hashtable();
      try
      {
        htOut.put("ipaddress", InetAddress.getLocalHost().getHostAddress());
      }
      catch (java.net.UnknownHostException uhX)
      {
        htOut.put("ipaddress", "127.0.0.1");
        log.warning("Unknown Host Exception was raised: " + uhX.getMessage());
      }      
      htOut.put("type", "Deimos");
      htOut.put("os", System.getProperty("os.name"));
      htOut.put("name", originatingPort);
      htOut.put("time", Calendar.getInstance());
      htOut.put("seq", new Integer(0));
      System.out.println("Sending: " + sendData);
      log.config("Sending data message to Mars: " + sendData);
      marsXn.sendCOMData(htOut, sendData);
    }
    catch (RemoteException rX)
    {
      // If a problem is detected then put it in the log files.
      log.throwing("ConnectionFactory", "sendCOMData()", rX);
    }
  }
}