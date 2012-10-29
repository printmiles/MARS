/* Class name: COMTester
 * File name:  COMTester.java
 * Created:    03-Aug-2008 20:30:29
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-Aug-2008 Initial build
 */

package mars.test;
import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import mars.deimos.remote.intf.DeimosCOMTester;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class COMTester implements COMInterface
{
  private boolean keepRunning;
  
  public COMTester()
  {
    // Default constructor
    keepRunning = true;
  }
  
  public void start(String location)
  {
    try
    {
      String host = "localhost";
      System.out.println("Attempting to establish RMI connection with: " + host);
      // Find the RMI server
      Registry regRMI = LocateRegistry.getRegistry(host, 1099);
      System.out.println("Found RMI server");
      // Locate and cast the object
      DeimosCOMTester dctRx = (DeimosCOMTester) regRMI.lookup(location);
      System.out.println("Connected to Deimos");
      System.out.println("Opening file");
      BufferedReader brSampleFile = new BufferedReader(new FileReader("TestCOMPortFile.txt"));
      System.out.println("Reading file");
      Vector vecFile = new Vector();
      String fileLine = brSampleFile.readLine();
      while (fileLine != null)
      {
        vecFile.add(fileLine);
        fileLine = brSampleFile.readLine();
      }
      while (keepRunning)
      {
        Random r = new Random();
        long duration = 100;
        Thread.sleep(duration); // Sleep for 100ms
        // Get a random line from the vector containing the file lines
        String fileText = new String((String) vecFile.get(r.nextInt(vecFile.size())));
        System.out.println(fileText);
        dctRx.sendData(fileText);
      }
    }
    catch (Exception x)
    {
      System.err.println("Exception detected during execution.");
      x.printStackTrace();
    }
  }
  
  public void finish()
  {
    keepRunning = false;
  }
}