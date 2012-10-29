/* Class name: DeimosCOMTester
 * File name:  DeimosCOMTester.java
 * Created:    03-Aug-2008 12:57:48
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-Aug-2008 Initial build
 */

package mars.deimos.remote.intf;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by Deimos to receive data from a COM port test application.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public interface DeimosCOMTester extends Remote
{
  public void sendData(String dataString) throws RemoteException;
}