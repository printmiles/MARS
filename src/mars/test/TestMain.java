/* Class name: Main
 * File name:  Main.java
 * Created:    03-Aug-2008 15:25:00
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-Aug-2008 Initial build
 */

package mars.test;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
 * No details have been added to this application yet.
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class TestMain
{
  public static void main(String[] args)
  {
try
    {
      System.out.println("Attempting object to rmi registry bind");
      COMTester ctDummyPort = new COMTester();
      COMInterface ciFakePort = (COMInterface) UnicastRemoteObject.exportObject(ctDummyPort);
      // Finds the RMIRegistry instance on 127.0.0.1:1099 (Port 1099 on localhost, this machine)
      Registry reg = LocateRegistry.getRegistry();
      long time = System.currentTimeMillis();
      System.out.println("Binding with an object name of: COM99Tester");
      reg.rebind("COM99Tester", ciFakePort);
      System.out.println("Binding done");
    }
    catch (RemoteException rX)
    {
      System.out.println("RMI Exception caught");
      rX.printStackTrace();
    }
    catch (Exception x)
    {
      System.out.println("Problems with binding");
      x.printStackTrace();
    } 
  }
}