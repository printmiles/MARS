/* Class name: comPort_Save
 * File name:  comPort_Save.java
 * Created:    14-Jul-2008 13:50:44
 * Modified:   26-Jul-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  26-Jul-2008 Added ability to amend threads as needed from the UI.
 * 0.003  21-Jul-2008 Added support to write to the file system and close the parent window
 * 0.002  19-Jul-2008 Added code to save settings to the Deimos preferences file
 * 0.001  14-Jul-2008 Initial build
 */

package mars.deimos.events;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import mars.deimos.gui.COMPortConfig;
import mars.deimos.gui.DeimosClient;
import mars.deimos.object.customisation.DeimosPreferences;
import mars.deimos.object.logging.LoggerFactory;
import mars.deimos.object.table.GenericTable;
import mars.deimos.object.thread.COMReaderThread;
import mars.deimos.reporting.DataConnectionManager;

/**
 * This class currently does not have any JavaDoc comments included
 * @version 0.004
 * @author Alex Harris (W4786241)
 */
public class comPort_Save implements ActionListener
{
  private Logger log;
  private GenericTable gt;
  private COMPortConfig cpcWin;
  
  public comPort_Save(GenericTable listOfCOMPorts, COMPortConfig parentWindow)
  {
    log = LoggerFactory.getLogger("mars.deimos.events.comPort_Save");
    log.finest("Initialised");
    gt = listOfCOMPorts;
    cpcWin = parentWindow;
  }
  
  /**
   * The event fired from the File menu to close the application
   * @param ae The triggered ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    DataConnectionManager dcmRunning = DeimosClient.getDataManager();
    log.finest("Building list of running threads");
    Enumeration enumRunningThreads = dcmRunning.getKeys();
    Vector vecThreadNames = new Vector();
    while (enumRunningThreads.hasMoreElements())
    {
      vecThreadNames.add((String) enumRunningThreads.nextElement());
    }
    log.config("Saving configuration details");
    int y = gt.getRowCount();
    int numberOfPortsSaved = 0;
    Preferences prefs = DeimosPreferences.getDeimosPrefs();
    /**************************************************************************/
    /*   WIPE THE PREVIOUSLY STORED VALUES                                    */
    /**************************************************************************/
    int currentlyStored = (new Integer(prefs.get("deimos.comPort.numberToMonitor", "0"))).intValue();
    for (int x = 0; x < currentlyStored; x++)
    {
      log.finest("Removing configuration of COM port: " + prefs.get("deimos.comPort." + x + ".name", "Unknown"));
      prefs.remove("deimos.comPort." + x + ".name");
      prefs.remove("deimos.comPort." + x + ".baud");
      prefs.remove("deimos.comPort." + x + ".data");
      prefs.remove("deimos.comPort." + x + ".parity");
      prefs.remove("deimos.comPort." + x + ".stop");
    }
    /**************************************************************************/
    /*   STORE THE NEW VALUES FROM THE GUI                                    */
    /**************************************************************************/
    for (int x = 0; x < y; x++)
    {
      Object[] objRow = (Object[]) gt.getRow(x);
      Boolean bolMonitor = (Boolean) objRow[0];
      String name = (String) objRow[1];
      if (bolMonitor.booleanValue())
      {
        String type = (String) objRow[2];
        int baud = ((Integer) objRow[4]).intValue();
        int data = ((Integer) objRow[5]).intValue();
        String parity = (String) objRow[6];
        float stop = ((Float) objRow[7]).floatValue();
        if (type.equals("Serial") || type.equals("Test"))
        {
          log.finest("Saving configuration of: " + name);
          prefs.put("deimos.comPort." + numberOfPortsSaved + ".name", name);
          prefs.putInt("deimos.comPort." + numberOfPortsSaved + ".baud", baud);
          prefs.putInt("deimos.comPort." + numberOfPortsSaved + ".data", data);
          prefs.put("deimos.comPort." + numberOfPortsSaved + ".parity", parity);
          prefs.putFloat("deimos.comPort." + numberOfPortsSaved + ".stop", stop);
          numberOfPortsSaved++;
          log.config("Creating new monitoring thread");
          COMReaderThread crtNewPort = new COMReaderThread(name, baud, data, parity, stop);
          dcmRunning.add(name, crtNewPort, type);
        }
        else
        {
          javax.swing.JOptionPane.showMessageDialog(null,"Cannot configure port types other than Serial");
        }
      }
      else
      {
        log.finest("Checking to see if a thread for " + name + " exists.");
        if (dcmRunning.exists(name))
        {
          log.config("Removing thread for: " + name);
          dcmRunning.remove(name);
        }
        else
        {
          log.finest("Thread not found. Continuing execution.");
        }
        
      }
    }
    prefs.putInt("deimos.comPort.numberToMonitor", numberOfPortsSaved);
    log.finest("Saving preferences to file system");
    DeimosPreferences.updateDeimosPrefs();
    cpcWin.setVisible(false);
  }
}


