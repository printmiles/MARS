/* Class name: MarsConnection
 * File name:  MarsConnection.java
 * Created:    10-Jun-2008 17:05:02
 * Modified:   16-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  16-Jun-2008 Altered the sendTime method to use the Hashtable rather than just send a Calendar instance
 * 0.002  13-Jun-2008 Added the sendTime method and removed the connectToMars
 *        method to its own interface (MarsServer).
 * 0.001  10-Jun-2008 Initial build
 */

package mars.mars.remote.intf;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

/**
 * This interface is used for initiating and keeping connections from Deimos to Mars alive.
 * <p>NOTE: The information passed through this class should be an instance of Hashtable and
 * implementing classes must verify that the following keys are present. Similarly
 * Deimos clients and classes utilising this interface should ensure that the following
 * have been included in their implementations:
 * <table style="width: 100%">
 * <tr>
 * <td><strong>Key</strong></td>
 * <td><strong>Type</strong></td>
 * <td><strong>Purpose</strong></td>
 * </tr>
 * <tr>
 * <td>ipaddress</td>
 * <td>String</td>
 * <td>Contains the client IP address</td>
 * </tr>
 * <tr>
 * <td>type</td>
 * <td>String</td>
 * <td>Contains the client&#39;s application being used (Deimos or Mars)</td>
 * </tr>
 * <tr>
 * <td>os</td>
 * <td>String</td>
 * <td>Contains the client operating system</td>
 * </tr>
 * <tr>
 * <td>name</td>
 * <td>String</td>
 * <td>Contains the name of the data source being connected to</td>
 * </tr>
 * <tr>
 * <td>time</td>
 * <td>Calendar</td>
 * <td>Contains the time the message was sent</td>
 * </tr>
 * <tr>
 * <td>seq</td>
 * <td>Integer</td>
 * <td>Contains the sequence number of the message</td>
 * </tr>
 * </table>
 * @version 0.003
 * @author Alex Harris (W4786241)
 */
public interface MarsConnection extends Remote
{
  /**
   * Used by Deimos to send keepAlive signal to Mars so that we can keep track of
   * Deimos clients and their status.
   * @param properties The hashtable mentioned in the class description.
   * @throws java.rmi.RemoteException
   */
  public void sendKeepAlive(Hashtable properties) throws RemoteException;
  /**
   * Used by Deimos clients to obtain information about the Mars server they're
   * connected to.
   * @return The hashtable mentioned in the class description.
   * @throws java.rmi.RemoteException
   */
  public Hashtable getInformation() throws RemoteException;
  /**
   * Used by both Mars and Deimos to obtain the round-trip-time and ping between the
   * two machines and to calculate any differences between the two clocks.
   * @param properties The hashtable mentioned in the class description.
   * @param clockStage The stage of clocking negotiations
   * @return The hashtable mentioned in the class description.
   * @throws java.rmi.RemoteException
   */
  public Hashtable sendTime(Hashtable properties, int clockStage) throws RemoteException;
  /**
   * Used for sending data collected from Deimos to Mars for processing.
   * @param properties The hashtable mentioned in the class description.
   * @param data The data string received from the COM port
   * @throws java.rmi.RemoteException
   */
  public void sendCOMData(Hashtable properties, String data) throws RemoteException;
}