/* Class name: COMInterface
 * File name:  COMInterface.java
 * Created:    03-Aug-2008 15:25:00
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-Aug-2008 Initial build
 */

package mars.test;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * No details have been added to this application yet.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public interface COMInterface extends Remote
{
  public void start(String dataString) throws RemoteException;
  public void finish() throws RemoteException;
}