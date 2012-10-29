/* Class name: COMTestReader
 * File name:  COMTestReader.java
 * Created:    03-Aug-2008 13:27:55
 * Modified:   03-Aug-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-Aug-2008 Initial build
 */

package mars.deimos.remote.impl;
import mars.deimos.object.thread.COMReaderThread;
import mars.deimos.remote.intf.DeimosCOMTester;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.001
 * @author Alex Harris (W4786241)
 */
public class COMTestReader implements DeimosCOMTester
{
  private COMReaderThread p;
  
  public COMTestReader(COMReaderThread parent)
  {
    // Default constructor
    p = parent;
  }
  
  public void sendData(String aMsg)
  {
    p.forceDataToMars(aMsg);
  }
}