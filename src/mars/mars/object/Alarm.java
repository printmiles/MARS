/* Class name: Alarm
 * File name:  Alarm.java
 * Created:    01-Apr-2008 21:29:51
 * Modified:   09-Apr-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.005  09-Apr-2008 Added examine property and accessors
 * 0.004  04-Apr-2008 Added equals method
 * 0.003  03-Apr-2008 Added comments and JDoc
 * 0.002  02-Apr-2008 Added class content
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.object;
import java.awt.Color;

/**
 * This class is used to represent one or more alarms within a FormatDoc.
 * As this class only contains accessory pairs and no complex methods, there is
 * no logging provided for instances of this class.
 * @version 0.005
 * @author Alex Harris (W4786241)
 */
public class Alarm
{
  private String sExamine;
  private String sCondition;
  private Object[] oOperands;
  private String sImportance;
  private Color cBackground;
  private String sText;
  private Color cTextColour;
  
  /**
   * Instantiates the class with default values and returns to the receiver.
   */
  public Alarm()
  {
    // Default constructor
    setExamine("");
    setCondition("equal to");
    setOperand("?", 0);
    setImportance("medium");
    setText("This alarm has not been configured correctly");
    setBackgroundColour(255,255,255);
    setTextColour(0,0,0);
  }
  
  /**
   * Evaluates whether two alarms are the same in that all of the variables within the Alarm are the same.
   * Returns true if all of the values are the same and false if not.
   * @param anAlarm The Alarm instance to compare it to
   * @return Whether the two instances match entirely
   */
  public boolean equals(Alarm anAlarm)
  {
    boolean bExamine = false;    // Used for evaluating whether the two Examine strings match
    boolean bCondition = false;  // Used for evaluating whether the two Condition strings match
    boolean bOperand = false;    // Used for evaluating whether the Operator arrays match
    boolean bImportance = false; // Used for evaluating whether the two Importance strings match
    boolean bText = false;       // Used for evaluating whether the two Text strings match
    boolean bBColour = false;    // Used for evaluating whether the two Background Colors match
    boolean bTColour = false;    // Used for evaluating whether the two Text Colors match
    
    // I'm writing in this format purely to save space, it's still an IF structure just minus the extra lines
    if (this.getExamine().equals(anAlarm.getExamine())) { bExamine = true; } // Check the Examine values
    if (this.getCondition().equals(anAlarm.getCondition())) { bCondition = true; } // Check the Condition values
    if (this.getImportance().equals(anAlarm.getImportance())) { bImportance = true; } // Check the Importance values
    if (this.getText().equals(anAlarm.getText())) { bText = true; } // Check the two text values
    
    if (this.getOperand(0).equals(anAlarm.getOperand(0)) &&
        this.getOperand(1).equals(anAlarm.getOperand(1)))
    {
      // Return true if both of the operands are the same value
      bOperand = true;
    }
    
    if (this.getBackgroundColour().getRGB() == anAlarm.getBackgroundColour().getRGB())
    {
      // Return true if the RGB values are the same
      bBColour = true;
    }

    if (this.getTextColour().getRGB() == anAlarm.getTextColour().getRGB())
    {
      // Return true if the RGB values are the same
      bTColour = true;
    }
    
    if (bExamine &&
        bCondition &&
        bOperand &&
        bImportance &&
        bText &&
        bBColour &&
        bTColour)
    {
      // If everything is true then the two match and so return true
      return true;
    }
    else
    {
      // Otherwise they don't match and return false
      return false;
    }
  }
  
  /**
   * Returns the field that the alarm examines.
   * @return The field name that alarm examines
   */
  public String getExamine()
  {
    return sExamine;
  }
  
  /**
   * Sets the field to examine and alarm on.
   * @param newExam The new field to examine
   */
  public void setExamine(String newExam)
  {
    sExamine = newExam;
  }
  
  /**
   * Returns the type of condition that the alarm operates on.
   * @return The condition the alarm activates on
   */
  public String getCondition()
  {
    return sCondition;
  }
  
  /**
   * Sets the condition the alarm should operate on.
   * @param newCondition The new alarm condition
   */
  public void setCondition(String newCondition)
  {
    sCondition = newCondition;
  }
  
  /**
   * Returns the operand for the alarm condition. There may be one or two operands
   * so the position of the required operand should also be supplied.
   * @param pos The position of the required operand within the array
   * @return The operand to be used with the supplied condition
   */
  public Object getOperand(int pos)
  {
    return oOperands[pos];
  }
  
  /**
   * Set the operand for the condition at the given position.
   * @param value The operand to be used with the alarm condition
   * @param pos The position within the array in which to place the operand
   */
  public void setOperand(Object value, int pos)
  {
    oOperands[pos] = value;
  }
  
  /**
   * Returns the importance of the alarm
   * @return The alarms importance
   */
  public String getImportance()
  {
    return sImportance;
  }
  
  /**
   * Sets the alarms importance to the supplied level
   * @param newImportance The new importance level
   */
  public void setImportance(String newImportance)
  {
    sImportance = newImportance;
  }
  
  /**
   * Returns the text to be returned when the alarm evaluates to true
   * @return The text displayed when the alarm is activated
   */
  public String getText()
  {
    return sText;
  }
  
  /**
   * Sets the alarms text to a new string value
   * @param newText The new alarm text
   */
  public void setText(String newText)
  {
    sText = newText;
  }
  
  /**
   * Returns an instance of <code>Color</code> used for the background of the
   * alarm window.
   * @return The alarms background colour
   * @see java.awt.color
   */
  public Color getBackgroundColour()
  {
    return cBackground;
  }
  
  /**
   * Returns an instance of <code>Color</code> used for the foreground or text
   * colour used in an alarm window.
   * @return The alarm text colour
   * @see java.awt.color
   */
  public Color getTextColour()
  {
    return cTextColour;
  }
  
  /**
   * Sets the alarm background colour to the colour represented by the three colour
   * values for the red, green and blue quotient.
   * @param r The red quotient of the required colour. Valid values are 0-255.
   * @param g The green quotient of the required colour. Valid values are 0-255.
   * @param b The blue quotient of the required colour. Valid values are 0-255.
   */
  public void setBackgroundColour(int r, int g, int b)
  {
    cBackground = new Color(r,g,b);
  }
  
  /**
   * Sets the alarm text colour to the colour represented by the three colour
   * values for the red, green and blue quotient.
   * @param r The red quotient of the required colour. Valid values are 0-255.
   * @param g The green quotient of the required colour. Valid values are 0-255.
   * @param b The blue quotient of the required colour. Valid values are 0-255.
   */
  public void setTextColour(int r, int g, int b)
  {
    cTextColour = new Color(r,g,b);
  }
}