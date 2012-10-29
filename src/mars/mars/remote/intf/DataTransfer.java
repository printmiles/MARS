/* Class name: DataTransfer
 * File name:  DataTransfer.java
 * Created:    11-Jun-2008 22:30:59
 * Modified:   13-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  13-Jun-2008 Added JDoc and acknowledge method
 * 0.001  11-Jun-2008 Initial build
 */

package mars.mars.remote.intf;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;
import mars.mars.object.FormatDoc;

/**
 * This package is used for transmitting information between Deimos and Mars, and
 * contains several methods to aid the transfer of data between the two applications.
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public interface DataTransfer extends Remote
{
  /**
   * Used by Deimos to obtain an instance of FormatDoc from Mars. This does mean
   * that Deimos needs to access the mars.mars.object package to access the FormatDoc
   * class and its associated classes (Table, Column, Alarm etc).
   * @param name The name of the FormatDoc to obtain from the FormatDoc store.
   * @return The requested FormatDoc
   * @throws java.rmi.RemoteException
   */
  public FormatDoc getFormatDoc(String name) throws RemoteException;
  /**
   * Used by Deimos to send a set of data received from a datasource to Mars. The
   * hashtable is also used to carry additional information about the data received:
   * <table style="width: 100%">
   * <tr>
   * <td><strong>Key</strong></td>
   * <td><strong>Type</strong></td>
   * <td><strong>Purpose</strong></td>
   * </tr>
   * <tr>
   * <td>data</td>
   * <td>Hashtable</td>
   * <td>Contains the received data using attribute names as keys</td>
   * </tr>
   * <tr>
   * <td>seq</td>
   * <td>Integer</td>
   * <td>Contains the sequence number of the </td>
   * </tr>
   * <tr>
   * <td>time</td>
   * <td></td>
   * <td>Contains the received data using attribute names as keys</td>
   * </tr>
   * @param values
   * @throws java.rmi.RemoteException
   */
  public void sendData(Hashtable values) throws RemoteException;
  /**
   * Sends information about COM ports from Deimos to Mars. 
   * @param info
   * @throws java.rmi.RemoteException
   */
  public void sendCOMInfo(Vector info) throws RemoteException;
  /**
   * Sends table information fromDeimos to Mars.
   * @param info
   * @throws java.rmi.RemoteException
   */
  public void sendTableInfo(Vector info) throws RemoteException;
}