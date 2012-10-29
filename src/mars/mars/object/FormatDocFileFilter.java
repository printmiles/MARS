/* Class name: FormatDocFileFilter
 * File name:  FormatDocFileFilter.java
 * Created:    31-Mar-2008 11:01:05
 * Modified:   02-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  02-Apr-2008 Added JDoc and comments
 * 0.001  31-Mar-2008 Initial build
 */

package mars.mars.object;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class extends <code>FileFilter</code> and is to be used with instances of the
 * <code>JFileChooser</code> class. This class is designed to filter files displayed
 * within the <code>JFileChooser</code> to directories and FormatDoc (*.fd.xml)
 * files. This is to aid the user in locating FormatDocs to be used with MARS.
 * @version 0.002
 * @author Alex Harris (W4786241)
 * @see javax.swing.JFileChooser
 * @see javax.swing.filechooser.FileFilter
 */
public class FormatDocFileFilter extends FileFilter
{
  /** 
   * Returns the description of the files to be displayed through the filter.
   * @return - The string "MARS FormatDoc"
   */
  public String getDescription()
  {
    return "MARS FormatDoc";
  }
  
  /**
   * Returns a boolean value indicating whether the item should be displayed or not
   * within the <code>JFileChooser</code> using this method. For this class both
   * directories and files ending .fd.xml return true and all other file return false.
   * @param aFile A file within the system directory structure
   * @return Whether the file should be displayed
   * @see javax.swing.JFileChooser
   */
  public boolean accept(File aFile)
  {
    // Show the directories available
    if (aFile.isDirectory())
    {
      return true;
    }
    else
    {
      // Sort files in the directory and show them if they end in .fd.xml
      String fileName = aFile.getName();
      if (fileName.endsWith(".fd.xml"))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }
}