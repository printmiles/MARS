/* Class name: MarsSecurityManager
 * File name:  MarsSecurityManager.java
 * Created:    17-Jun-2008 15:49:28
 * Modified:   18-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  18-Jun-2008 Added overridden methods
 * 0.001  17-Jun-2008 Initial build
 */

package mars.mars.object.security;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.002
 * @author Alex Harris (W4786241)
 */
public class MarsSecurityManager extends SecurityManager
{
  public MarsSecurityManager()
  {
    super();
  }
  
  public void checkAccept(String host, int port)
  {
    if ((host.equals("localhost") || host.equals("127.0.0.1")) && (port == 1099))
    {
      return;
    }
    else
    {
      super.checkAccept(host, port);
    }
  }
  
  public void checkConnect(String host, int port)
  {
    if ((host.equals("localhost") || host.equals("127.0.0.1")) && (port == 1099))
    {
      return;
    }
    else
    {
      super.checkConnect(host, port);
    }
  }
  
  public void checkListen(int port)
  {
    if (port == 1099)
    {
      return;
    }
    else
    {
      super.checkListen(port);
    }
  }
  
  public void checkExit(int status)
  {
    if (status == 1)
    {
      return;
    }
    else
    {
      super.checkExit(status);
    }
  }
  
  public void checkRead(String fileName)
  {
    if (fileName.endsWith(".log.xml") ||
        fileName.endsWith(".prefs.xml") ||
        fileName.endsWith(".fd.xml") ||
        fileName.endsWith(".xsd"))
    {
      return;
    }
    else
    {
      super.checkRead(fileName);
    }
  }
  
  public void checkWrite(String fileName)
  {
    if (fileName.endsWith(".log.xml") ||
        fileName.endsWith(".prefs.xml") ||
        fileName.endsWith(".fd.xml"))
    {
      return;
    }
    else
    {
      super.checkWrite(fileName);
    }
  }
}