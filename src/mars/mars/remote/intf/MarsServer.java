/* Class name: MarsServer
 * File name:  MarsServer.java
 * Created:    15-Jun-2008 07:12:05
 * Modified:   15-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  15-Jun-2008 Initial build
 */

package mars.mars.remote.intf;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

/**
 * This interface is used for Mars to provide a remote object through which all Deimos
 * clients connect.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public interface MarsServer extends Remote
{
  /**
   * Used by Deimos clients after connecting through a RMI lookup to inform Mars that
   * they're connected and pass information identifying themselves to Mars.
   * @param properties The hashtable mentioned in the class description.
   * @return The connection that should be used between the two applications.
   * @throws java.rmi.RemoteException
   */
  public String connectToMars(Hashtable properties) throws RemoteException;
}