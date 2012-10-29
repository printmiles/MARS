/* Class name: ServerFactory
 * File name:  ServerFactory.java
 * Created:    15-Jun-2008 07:23:47
 * Modified:   08-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  08-Jul-2008 Changed the way in which the MarsConnection is obtained. It is now mapped to the
 *        RMIRegistry using the unique identifier and the connectToMars method passes this to Deimos to
 *        retrieve it rather than pass the object directly.
 * 0.002  07-Jul-2008 Removed inheritance from java.rmi.server.UnicastRemoteObject
 * 0.001  15-Jun-2008 Initial build
 */

package mars.mars.remote.impl;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.remote.intf.*;

/**
 * This class is used to handle incoming server connections through RMI and use
 * the ConnectionFactory class to provide a connection for subsequent conversations
 * between Mars and a Deimos class.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class ServerFactory implements MarsServer
{
  private static final String parentClassName = "mars.mars.remote.impl.ServerFactory";
  private Logger log;
  
  public ServerFactory() throws RemoteException
  {
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("ServerFactory instance being initialised.");
  }
  
  public String connectToMars(Hashtable ht) throws RemoteException
  {
    ConnectionFactory cfNew = new ConnectionFactory(ht);
    String ipaddress = (String) ht.get("ipaddress");
    String name = (String) ht.get("name");
    String uid = ipaddress + ":" + name;
    try
    {
      log.finest("Attempting object to rmi registry bind");
      MarsConnection mcXn = (MarsConnection) UnicastRemoteObject.exportObject(cfNew);
      // Finds the RMIRegistry instance on 127.0.0.1:1099 (Port 1099 on localhost, this machine)
      Registry reg = LocateRegistry.getRegistry();
      log.config("Binding object with a name of " + uid);
      reg.rebind(uid, mcXn);
      log.config("Object bound to RMI registry. Ready to accept connections on the default port (1099)");
    }
    catch (RemoteException rX)
    {
      System.out.println("RMI Exception caught");
      rX.printStackTrace();
      log.throwing(parentClassName, "connectToMars(Hashtable)", rX);
    }
    catch (Exception x)
    {
      System.out.println("Problems with binding");
      x.printStackTrace();
      log.severe("Problem starting the application");
      log.throwing(parentClassName, "connectToMars(Hashtable)", x);
    }
    return uid;
  }
}