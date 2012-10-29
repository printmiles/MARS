/* Class name: FormatDocHandler
 * File name:  FormatDocHandler.java
 * Created:    01-Apr-2008 10:51:22
 * Modified:   05-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.008  05-May-2008 Fixed bug in endElement method where a blank section of text is
 *        added to the conversion hashtable.
 * 0.007  24-Apr-2008 Fixes for the following identified bugs:
 *        - <tables>.<table>.<name> is not retrieved from the source document correctly
 *        - <tables>.<table>.<retention> is not recognised at all, units or the value
 *        - <tables>.<table>.<attributes>.<attribute>.<report> isn't recorded properly
 *        - <queries>.<query>.<statement> is not retrieved at all
 * 0.006  16-Apr-2008 Testing and debugging the class
 * 0.005  09-Apr-2008 Adding further details to the startElement and endElement methods
 * 0.004  07-Apr-2008 Adding further details to the startElement and endElement methods
 * 0.003  05-Apr-2008 Adding further details to the startElement and endElement methods
 * 0.002  04-Apr-2008 Adding detail to the methods to populate an instance of FormatDoc
 * 0.001  01-Apr-2008 Initial build
 */

package mars.mars.xml;
import java.awt.Color;
import java.util.logging.Logger;
import mars.mars.object.*;
import mars.mars.object.logging.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class currently does not have any JavaDoc comments included
 * This class is responsible for creating instances of the following classes:
 * <code><ul>
 *   <li>mars.mars.object.FormatDoc</li>
 *   <li>mars.mars.object.Table</li>
 *   <li>mars.mars.object.Attribute</li>
 *   <li>mars.mars.object.Column</li>
 *   <li>mars.mars.object.Query</li>
 *   <li>mars.mars.object.Alarm</li>
 * </ul></code>
 * The structure of the FormatDoc is layered as such (attributes are in brackets) with each
 * node numbered uniquely for internal identification:
 * <p>1 - formatDoc               (UID: 0)
 * <p>  2 - connect (to, using)   (UID: 1)
 * <p>  2 - tables                (UID: 2)
 * <p>    3 - table               (UID: 21)
 * <p>      4 - name              (UID: 211)
 * <p>      4 - retention (units) (UID: 213) <-- This was added 15/04/08. The position is correct but the UID varies-->
 * <p>      4 - attribute (id)    (UID: 212)
 * <p>        5 - name            (UID: 2121)
 * <p>        5 - type            (UID: 2122)
 * <p>        5 - report          (UID: 2123)
 * <p>        5 - conversions     (UID: 2124)
 * <p>          6 - convert (raw) (UID: 21241)
 * <p>  2 - text                  (UID: 3)
 * <p>    3 - column (from, to)   (UID: 31)
 * <p>      4 - name              (UID: 311)
 * <p>      4 - type              (UID: 312)
 * <p>  2 - queries               (UID: 4)
 * <p>    3 - query (id)          (UID: 41)
 * <p>      4 - name              (UID: 411)
 * <p>      4 - statement         (UID: 412)
 * <p>  2 - alarms                (UID: 5)
 * <p>    3 - alarm               (UID: 51)
 * <p>      4 - examine           (UID: 511)
 * <p>      4 - importance        (UID: 512)
 * <p>      4 - background        (UID: 513)
 * <p>        5 - r               (UID: 5131)
 * <p>        5 - g               (UID: 5132)
 * <p>        5 - b               (UID: 5133)
 * <p>      4 - text              (UID: 514)
 * <p>        5 - string          (UID: 5141)
 * <p>        5 - colour          (UID: 5142)
 * <p>          6 - r             (UID: 51421)
 * <p>          6 - g             (UID: 51422)
 * <p>          6 - b             (UID: 51423)
 * <p>      4 - lessThan          (UID: 515)
 * <p>      4 - greaterThan       (UID: 516)
 * <p>      4 - equalTo           (UID: 517)
 * <p>      4 - notEqualTo        (UID: 518)
 * <p>      4 - between           (UID: 519)
 * <p>        5 - start           (UID: 5191)
 * <p>        5 - end             (UID: 5192)
 * @version 0.006
 * @author Alex Harris (W4786241)
 */
public class FormatDocHandler extends DefaultHandler
{
  private static final String parentClassName = "mars.mars.xml.FormatDocHandler";
  private static Logger log;
  private FormatDoc fdSys;  // The formatting document (FormatDoc) that represents a system connection
  private String level3; // This is to track the level 3 tag being worked on
  private int currentLevel; // The current level within the document
  private int currentElement; // The UID of the element being worked on
  private String elementString; // The string contained between the start and end elements
  private String miscString; // A general string to be used when needed
  /*******************************************************************************/
  /* The variables below need to be declared so that items can be added to the   */
  /* FormatDoc while processing jumps from startElement to characters etc.       */
  /* Once the endElement method is called then the object that's just been       */
  /* edited can be added to the FormatDoc using its methods. The startElement    */
  /* then initialises the variable again to be reused for the next item.         */
  /*******************************************************************************/
  private Table aTable; // A temporary instance to work with before adding to a FormatDoc
  private Attribute anAttribute; // A temporary instance to work with before adding to a FormatDoc
  private Column aColumn; // A temporary instance to work with before adding to a FormatDoc
  private Query aQuery; // A temporary instance to work with before adding to a FormatDoc
  private Alarm anAlarm; // A temporary instance to work with before adding to a FormatDoc
  
  
  /**
   * Instantiates the class and returns it to the receiver
   */
  public FormatDocHandler()
  {
    log = LoggerFactory.getLogger(parentClassName);
    log.finest("Created logger for FormatDocHandler");
    currentLevel = 1;
    currentElement = 0;
    elementString = "";
  }
  
  /**
   * Executed at the start of the process of parsing the document
   * @throws org.xml.sax.SAXException
   */
  public void startDocument() throws SAXException
  {
    log.finest("Starting to parse XML document");
    fdSys = new FormatDoc();
  }
  
  /**
   * Executed when the document has completed being parsed
   * @throws org.xml.sax.SAXException
   */
  public void endDocument() throws SAXException
  {
    log.finest("Finished parsing XML document");
    FormatDocStore fdStore = new FormatDocStore();
    fdStore.putTempFD(fdSys);
  }
  
  /**
   * Executed when a XML start element is encountered within the Formatting Document (FormatDoc).
   * @param uri The namespace of the element
   * @param localName The local name of the element
   * @param qualifiedName The qualified name of the element
   * @param attributes Any attributes thay are included with the element
   * @throws org.xml.sax.SAXException
   */
  public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException
  {
    qualifiedName = qualifiedName.toLowerCase(); // Just doing this to make absolutely certain that the element is lowercase
    // Keep these lines of code for debugging and logging from here
    log.finest("Found the start of an element <" + qualifiedName + ">");
    for (int i = 0; i < attributes.getLength(); i++)
    {
      log.finest("Found an attribute: " + attributes.getLocalName(i) + " with value: " + attributes.getValue(i));
    }
    // Down to here. It doesn't do any harm to keep them in and can be used for fault finding if required.
    if (currentLevel == 2)
    {
      if (qualifiedName.equals("connect"))
      {
        fdSys.setProtocol(attributes.getValue("using"));
        fdSys.setDestination(attributes.getValue("to"));
        currentElement = 1;
      }
    }
    if (currentLevel == 3)
    {
      level3 = qualifiedName;
      if (qualifiedName.equals("table"))
      {
        aTable = new Table();
        currentElement = 21;
      }
      if (qualifiedName.equals("column"))
      {
        aColumn = new Column();
        aColumn.setFrom(Integer.parseInt(attributes.getValue("from")));
        aColumn.setTo(Integer.parseInt(attributes.getValue("to")));
        currentElement = 31;
      }
      if (qualifiedName.equals("query"))
      {
        aQuery = new Query();
        aQuery.setID(attributes.getValue("id"));
        currentElement = 41;
      }
      if (qualifiedName.equals("alarm"))
      {
        anAlarm = new Alarm();
        currentElement = 51;
      }
    }
    if (currentLevel == 4)
    {
      if (qualifiedName.equals("attribute"))
      {
        anAttribute = new Attribute();
        anAttribute.setID(attributes.getValue("id"));
        currentElement = 212;
      }
      if (qualifiedName.equals("type"))
      {
        currentElement = 312;
      }
      if (qualifiedName.equals("statement"))
      {
        currentElement = 412;
      }
      if (qualifiedName.equals("examine"))
      {
        currentElement = 511;
      }
      if (qualifiedName.equals("importance"))
      {
        currentElement = 512;
      }
      if (qualifiedName.equals("background"))
      {
        currentElement = 513;
      }
      if (qualifiedName.equals("text"))
      {
        currentElement = 514;
      }
      if (qualifiedName.equals("lessThan"))
      {
        currentElement = 515;
      }
      if (qualifiedName.equals("greaterThan"))
      {
        currentElement = 516;
      }
      if (qualifiedName.equals("equalTo"))
      {
        currentElement = 517;
      }
      if (qualifiedName.equals("notEqualTo"))
      {
        currentElement = 518;
      }
      if (qualifiedName.equals("between"))
      {
        currentElement = 519;
      }
      if (qualifiedName.equals("retention"))
      {
        aTable.setRetentionUnits(attributes.getValue("units"));
        currentElement = 213;
      }
      if (qualifiedName.equals("name"))
      {
        if (level3.equals("table"))
        {
          currentElement = 211;
        }
        if (level3.equals("column"))
        {
          currentElement = 311;
        }
        if (level3.equals("query"))
        {
          currentElement = 411;
        }
      }
    }
    if (currentLevel == 5)
    {
      if (qualifiedName.equals("name"))
      {
        currentElement = 2121;
      }
      if (qualifiedName.equals("type"))
      {
        currentElement = 2122;
      }
      if (qualifiedName.equals("report"))
      {
        currentElement = 2123;
      }
      if (qualifiedName.equals("r"))
      {
        currentElement = 5131;
      }
      if (qualifiedName.equals("g"))
      {
        currentElement = 5132;
      }
      if (qualifiedName.equals("b"))
      {
        currentElement = 5133;
      }
      if (qualifiedName.equals("string"))
      {
        currentElement = 5141;
      }
      if (qualifiedName.equals("start"))
      {
        currentElement = 5191;
      }
      if (qualifiedName.equals("end"))
      {
        currentElement = 5192;
      }
    }
    if (currentLevel == 6)
    {
      if (qualifiedName.equals("convert"))
      {
        miscString = attributes.getValue("raw");
        currentElement = 21241;
      }
      if (qualifiedName.equals("r"))
      {
        currentElement = 51421;
      }
      if (qualifiedName.equals("g"))
      {
        currentElement = 51422;
      }
      if (qualifiedName.equals("b"))
      {
        currentElement = 51423;
      }
    }
    currentLevel++;
  }
  
  /**
   * Executed when a XML end element (&lt;/&gt;) is encountered within the document
   * @param uri The namespace of the element
   * @param localName The local name of the element
   * @param qualifiedName The fully qualified name of the element
   * @throws org.xml.sax.SAXException
   */
  public void endElement(String uri, String localName, String qualifiedName) throws SAXException
  {
    log.finest("Found the end of an element </" + qualifiedName + ">");
    // This section is used to weed out empty string with only spaces and/or formatting characters
    elementString = elementString.trim();
    if (elementString.equals("\n") || elementString.equals("\t") || elementString.equals("\r"))
    {
      currentLevel--;
      return;
    }
    // This switch statement uses the currentElement variable which is set to the internal UID for each node
    switch (currentElement)
    {
      case 1: // <formatDoc>.<connect>
      {
        fdSys.setDataSourceName(elementString);
        break;
      }
      case 211: // <formatDoc>.<tables>.<table>.<name>
      {
        aTable.setName(elementString);
        break;
      }
      case 213: // <formatDoc>.<tables>.<table>.<retention>
      {
        aTable.setRetentionValue(Integer.parseInt(elementString));
        break;
      }
      case 2121: // <formatDoc>.<tables>.<table>.<attribute>.<name>
      {
        anAttribute.setName(elementString);
        break;
      }
      case 2122: // <formatDoc>.<tables>.<table>.<attribute>.<type>
      {
        anAttribute.setType(elementString);
        break;
      }
      case 2123: // <formatDoc>.<tables>.<table>.<attribute>.<report>
      {
        anAttribute.setReported(Boolean.parseBoolean(elementString));
        break;
      }
      case 21241: // <formatDoc>.<tables>.<table>.<attribute>.<conversions>.<convert>
      {
        // Puts the miscString (raw data value) and element string (value to be converted to) into the attribute conversion table
        anAttribute.putConversion(miscString, elementString);
        break;
      }
      case 311: // <formatDoc>.<text>.<column>.<name>
      {
        aColumn.setName(elementString);
        break;
      }
      case 312: // <formatDoc>.<text>.<column>.<type>
      {
        aColumn.setType(elementString);
        break;
      }
      case 411: // <formatDoc>.<queries>.<query>.<name>
      {
        aQuery.setName(elementString);
        break;
      }
      case 412: // <formatDoc>.<queries>.<query>.<statement>
      {
        aQuery.setStatement(elementString);
        break;
      }
      case 511: // <formatDoc>.<alarms>.<alarm>.<examine>
      {
        anAlarm.setExamine(elementString);
        break;
      }
      case 512: // <formatDoc>.<alarms>.<alarm>.<importance>
      {
        anAlarm.setImportance(elementString);
        break;
      }
      case 5131: // <formatDoc>.<alarms>.<alarm>.<background>.<r>
      {
        anAlarm.setBackgroundColour(Integer.parseInt(elementString), 0, 0);
        break;
      }
      case 5132: // <formatDoc>.<alarms>.<alarm>.<background>.<g>
      {
        Color cBack = anAlarm.getBackgroundColour();
        anAlarm.setBackgroundColour(cBack.getRed(), Integer.parseInt(elementString), 0);
        break;
      }
      case 5133: //<formatDoc>.<alarms>.<alarm>.<background>.<b>
      {
        Color cBack = anAlarm.getBackgroundColour();
        anAlarm.setBackgroundColour(cBack.getRed(), cBack.getGreen(), Integer.parseInt(elementString));
        break;
      }
      case 5141: // <formatDoc>.<alarms>.<alarm>.<text>.<string>
      {
        anAlarm.setText(elementString);
        break;
      }
      case 51421: // <formatDoc>.<alarms>.<alarm>.<text>.<colour>.<r>
      {
        anAlarm.setTextColour(Integer.parseInt(elementString), 0, 0);
        break;
      }
      case 51422: // <formatDoc>.<alarms>.<alarm>.<text>.<colour>.<g>
      {
        Color cText = anAlarm.getTextColour();
        anAlarm.setTextColour(cText.getRed(), Integer.parseInt(elementString), 0);
        break;
      }
      case 51423: // <formatDoc>.<alarms>.<alarm>.<text>.<colour>.<b>
      {
        Color cText = anAlarm.getTextColour();
        anAlarm.setTextColour(cText.getRed(), cText.getGreen(), Integer.parseInt(elementString));
        break;
      }
      case 515: // <formatDoc>.<alarms>.<alarm>.<lessThan>
      {
        anAlarm.setCondition("lessThan");
        anAlarm.setOperand(elementString, 0);
        break;
      }
      case 516: // <formatDoc>.<alarms>.<alarm>.<greaterThan>
      {
        anAlarm.setCondition(elementString);
        break;
      }
      case 517: // <formatDoc>.<alarms>.<alarm>.<equalTo>
      {
        anAlarm.setCondition(elementString);
        break;
      }
      case 518: // <formatDoc>.<alarms>.<alarm>.<notEqualTo>
      {
        anAlarm.setCondition(elementString);
        break;
      }
      case 5191: // <formatDoc>.<alarms>.<alarm>.<between>.<start>
      {
        anAlarm.setOperand(elementString, 0);
        break;
      }
      case 5192: // <formatDoc>.<alarms>.<alarm>.<between>.<end>
      {
        anAlarm.setOperand(elementString, 1);
        break;
      }
    }
    if (qualifiedName.equals("table"))
    {
      // Store the table within the formatDoc
      fdSys.addTable(aTable.getName(), aTable);
    }
    if (qualifiedName.equals("attribute"))
    {
      // Store the table within the formatDoc
      aTable.putAttribute(anAttribute.getID(), anAttribute);
    }
    if (qualifiedName.equals("column"))
    {
      // Store the table within the formatDoc
      fdSys.addColumn(aColumn.getFrom() + ":" + aColumn.getTo(), aColumn);
    }
    if (qualifiedName.equals("query"))
    {
      // Store the table within the formatDoc
      fdSys.addQuery(aQuery.getID(), aQuery);
    }
    if (qualifiedName.equals("alarm"))
    {
      // Store the table within the formatDoc
      fdSys.addAlarm(anAlarm);
    }
    currentLevel--;
  }
  
  /**
   * Used to handle the contents of a XML element.
   * @param ch A character array of the elements contents
   * @param start The starting position within the array of the string
   * @param length The end position of the string within the array
   * @throws org.xml.sax.SAXException
   */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    String sContents = new String (ch, start, length);
    log.finest("Found a character array to parse: " + sContents);
    elementString = sContents;
  }
}