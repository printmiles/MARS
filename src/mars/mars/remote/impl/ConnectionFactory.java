/* Class name: ConnectionFactory
 * File name:  ConnectionFactory.java
 * Created:    13-Jun-2008 16:03:08
 * Modified:   16-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  16-Jun-2008 Added code to sendKeepAlive, sendTime and getInformation methods
 * 0.001  13-Jun-2008 Initial build
 */

package mars.mars.remote.impl;
import java.net.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.remote.intf.MarsConnection;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class ConnectionFactory implements MarsConnection
{
  private static final String parentClassName = "mars.mars.remote.intf.MarsConnection";
  private Logger log;
  /***********************************************************************************/
  /* Hashtable                                                                       */
  /* connection MarsConnection The instance of this class used for communications    */
  /* type       String         The type of application used on the client            */
  /* os         String         The client operating system                           */
  /* name       String         The data source name (identifier)                     */
  /* seq        Integer        The sequence number last received from Deimos         */
  /* time       Calendar       The time the last message was received                */
  /* pingSet    Boolean        Whether the timing differences have been set          */
  /* ping       Integer        The ping between the two applications (ms)            */
  /* clockErr   Integer        The clocking difference between the two apps (ms)     */
  /* T(Mars)1   Calendar       The first Mars time to calculate clocking             */
  /* T(Deimos)1 Calendar       The first Deimos time to calculate clocking           */
  /* T-Stage    Integer        The position of clocking negotiation                  */
  /*            0 - Clocking synchronisation has not been started                    */
  /*            1 - Clocking has been received from Deimos and placed in T(Deimos)1  */
  /*            2 - T(Mars)1 has been set and a message sent to Deimos               */
  /*            3 - Message received back from Deimos, calculations can be performed */
  /*            4 - Message sent back to Deimos. T entries should be deleted.        */
  /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
  /* NOTE: If pingSet is set to true then the ping and clockErr keys will be         */
  /* populated, and the T keys will be removed from the hashtable. If pingSet is     */
  /* set to false then the opposite will hold true.                                  */
  /* NOTE: If T-Stage is present and set to 4 then an error has occurred while       */
  /* trying to tidy up the hashtable.                                                */
  /***********************************************************************************/
  private static Hashtable htConnections;
  
  public ConnectionFactory(Hashtable htDetails)
  {
    log = LoggerFactory.getLogger(parentClassName);
    if (htConnections == null)
    {
      log.finest("Initialising the hashtable");
      htConnections = new Hashtable();
    }
    String ipaddress = (String) htDetails.get("ipaddress");
    String name = (String) htDetails.get("name");
    if (!htConnections.contains(ipaddress + ":" + name))
    {
      Hashtable htXn = new Hashtable(); // Use this hashtable for defining the connection details
      htXn.put("connection", this);
      htXn.put("type", (String) htDetails.get("type"));
      htXn.put("os", (String) htDetails.get("os"));
      htXn.put("name", name);
      htXn.put("seq", new Integer(1));
      htXn.put("time", Calendar.getInstance());
      htXn.put("pingSet", new Boolean(false));
      htXn.put("ping", new Integer(0));
      htXn.put("clockErr", new Integer(0));
      htXn.put("T(Mars)1", Calendar.getInstance());
      htXn.put("T(Deimos)1", (Calendar) htDetails.get("time"));
      htXn.put("T-Stage", new Integer(0));
      log.config("Adding connection for " + ipaddress + ":" + name);
      htConnections.put(ipaddress + ":" + name, htXn);
    }
    
    System.out.println("Received a connection from: " + ipaddress + " at " + name);
  }
  
  public static void removeConnection(String identifier)
  {
    htConnections.remove(identifier);
  }
  
  public static Enumeration getKeys()
  {
    return htConnections.keys();
  }
  
  public static Hashtable getItem(String identifier)
  {
    return (Hashtable) htConnections.get(identifier);
  }
  
  public void sendKeepAlive(Hashtable properties) throws RemoteException
  {
    String ipaddress = (String) properties.get("ipaddress");
    String name = (String) properties.get("name");
    log.finest("Keepalive received for connection (" + ipaddress + ":" + name + ") at: " + Calendar.getInstance().toString());
    Hashtable htEntry = (Hashtable) htConnections.get(ipaddress + ":" + name);
    htEntry.put("seq", (Integer) properties.get("seq"));
    htEntry.put("time", Calendar.getInstance());
  }
  
  public Hashtable getInformation() throws RemoteException
  {
    Hashtable htLocalInfo = new Hashtable();
    try
    {
      htLocalInfo.put("ipaddress", InetAddress.getLocalHost().getHostAddress());
    }
    catch (UnknownHostException uhX)
    {
      htLocalInfo.put("ipaddress", "127.0.0.1");
      log.warning("Exception encountered while trying to read from the network interface.");
      log.throwing(parentClassName, "getInformation()", uhX);
    }      
    htLocalInfo.put("type", "Mars");
    htLocalInfo.put("os", System.getProperty("os.name"));
    htLocalInfo.put("name", "Server");
    htLocalInfo.put("time", Calendar.getInstance());
    htLocalInfo.put("seq", new Integer(0));
    return htLocalInfo;
  }
  
  public Hashtable sendTime(Hashtable properties, int clockStage) throws RemoteException
  {
    String ipaddress = (String) properties.get("ipaddress");
    String name = (String) properties.get("name");
    log.finest("Clocking information received for connection (" + ipaddress + ":" + name + ")");
    Hashtable htEntry = (Hashtable) htConnections.get(ipaddress + ":" + name);
    
    Hashtable htReturn = getInformation();
    switch (clockStage)
    {
      case 1:
      {
        htEntry.put("T(Deimos)1", (Calendar) properties.get("time"));
        htEntry.put("T(Mars)1", Calendar.getInstance());
        htEntry.put("T-Stage", new Integer(2));
        htReturn.put("seq", new Integer(2));
        break;
      }
      case 3:
      {
        Calendar calNow = Calendar.getInstance();
        htEntry.put("pingSet", new Boolean(true));
        Calendar calMars = (Calendar) htEntry.get("T(Mars)1");
        int ping = calNow.compareTo(calMars); // This will give a round-trip time in ms
        ping = ping / 2; // This will give a one-way delay.
        htEntry.put("ping", new Integer(ping));
        Calendar calDeimos = (Calendar) htEntry.get("T(Deimos)1");
        int clockError = calMars.compareTo(calDeimos);
        clockError = clockError - ping;
        htEntry.put("clockErr", new Integer(clockError));
        htEntry.put("T-Stage", new Integer(4));
        // If something happens within the code from here to the break statement then the T-Stage 4 will be left and the
        // rest of the application will now that an error has occurred.
        htEntry.remove("T(Mars)1");
        htEntry.remove("T(Deimos)1");
        htEntry.remove("T-Stage");
        break;
      }
      default:
      {
        log.warning("Received an unrecongnised message from a Deimos client: " + clockStage);
        break;
      }
    }
    htConnections.put(ipaddress + ":" + name, htEntry);
    return htReturn;
  }
  
  public void sendCOMData(Hashtable details, String data) throws RemoteException
  {
    String from = (String) details.get("ipaddress");
    String port = (String) details.get("name");
    System.out.println("Received COM data: " + data + "\nFrom: " + port + " on " + from);
  }
}