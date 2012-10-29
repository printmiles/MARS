/* Class name: FormatDocWriter
 * File name:  FormatDocWriter.java
 * Created:    21-Apr-2008 11:56:51
 * Modified:   06-May-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  06-May-2008 Added code for the text, queries and alarms tags in the
 *        writeContents() method
 * 0.002  22-Apr-2008 Continuing adding content to writeContents() method
 * 0.001  21-Apr-2008 Initial build
 */

package mars.mars.xml;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import mars.mars.object.*;
import mars.mars.object.customisation.*;
import mars.mars.object.logging.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.*;


/**
 * This class is to be used to write FormatDocs in their defined XML format.
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @todo JDoc and comments
 */
public class FormatDocWriter
{
  private static final String parentClassName = "mars.mars.xml.FormatDocWriter";
  private String sSystem;
  private Document docFD;
  private FormatDoc fdSys;
  private Logger log;
  
  public FormatDocWriter(String aSystem)
  {
    log = LoggerFactory.getLogger(parentClassName);
    sSystem = aSystem;
    log.config("Obtaining FormatDoc for " + sSystem);
    fdSys = FormatDocStore.get(sSystem);
    log.log(Level.CONFIG,"Retrieved FormatDoc",fdSys);
  }
  
  /**
   * Writes the FormatDoc stored in memory to disk. The code uses the writeElements code to iterate
   * through the structure of the FormatDoc and write the required elements into the Document structure.
   * This method is concerned with configuring the environment so that it is possible to write the elements,
   * by setting validation, obtaining the XSD, etc before calling the <code>writeElements</code> method
   * and finally storing all of this to disk using the <b>.fd.xml</b> extension denoting a MARS FormatDoc.
   * @return Whether the method has executed without encountering any problems
   */
  public boolean writeFormatDoc()
  {
    // Obtain preferences for the application
    Preferences xsdPrefs = MarsPreferences.getMarsPrefs();
    // Get the location of the XSD from the preferences. If it can't be obtained then default to the user.dir property
    String strXSDFileDir = xsdPrefs.get("mars.dir.XSD", System.getProperty("user.dir"));
    File fXSD = new File(strXSDFileDir);
    Schema sFD;
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    SchemaFactory xsdFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try
    {
      // Try to create the new schema from the file of the file path
      sFD = xsdFactory.newSchema(fXSD);
    }
    catch (Exception x)
    {
      // If anything happens then log the error and details and make the method return false.
      log.finest("An error occurred while trying to open the FormatDoc schema");
      log.throwing(parentClassName, "writeFormatDoc", x);
      return false;
    }
    // Check to make sure that the schema was correctly configured
    if (sFD != null)
    {
      // Use the schema to validate the documents we're going to create
      domFactory.setSchema(sFD);
      domFactory.setValidating(true);
      try
      {
        DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
        docFD = docBuilder.newDocument();
        // Write the contents of the FormatDoc using the Document (docFD) and FormatDoc (fdSys)
        writeContents();
        TransformerFactory xformFactory = TransformerFactory.newInstance();
        Transformer xFormer = xformFactory.newTransformer();
        // Check to see if the system name ends with .fd.xml it should but if it doesn't then add it into the file name
        FileOutputStream fosFDXML;
        if (sSystem.endsWith(".fd.xml"))
        {
          fosFDXML = new FileOutputStream("Out " + sSystem);
        }
        else
        {
          fosFDXML = new FileOutputStream("Out " + sSystem + ".fd.xml");
        }
        xFormer.transform(new DOMSource(docFD), new StreamResult(fosFDXML));
        // Flush and close the file
        fosFDXML.flush();
        fosFDXML.close();
      }
      catch (TransformerConfigurationException tcX)
      {
        // Transformer error thrown while trying to write the FormatDoc to disk
        log.warning("Transformer Configuration Exception occurred while trying to write the FormatDoc to disk.");
        log.throwing(parentClassName, "writeFormatDoc()", tcX);
      }
      catch (TransformerException tX)
      {
        // Transformer error thrown while trying to write the FormatDoc to disk
        log.warning("Transformer Exception occurred while trying to write the FormatDoc to disk.");
        log.throwing(parentClassName, "writeFormatDoc()", tX);
      }
      catch (ParserConfigurationException pcX)
      {
        // Parser configuration error thrown while working with the DOM representation of the document
        log.warning("Parser Configuration Exception occurred while trying to work with the document");
        log.throwing(parentClassName, "writeFormatDoc()", pcX);
      }
      catch (IOException ioX)
      {
        // IO Exception thrown while trying to write the file to disk
        log.warning("An IO error was thrown while trying to write to disk.");
        log.throwing(parentClassName, "writeFormatDoc()", ioX);
      }
      catch (Exception x)
      {
        // General exception that was generated from a different source from those already caught
        log.warning("General exception caught.");
        log.throwing(parentClassName, "writeFormatDoc()", x);
      }
    }
    else
    {
      // If the code ends up here then something strange happened in that no errors were detected while creating the Schema
      // but it was still set to null.
      log.severe("The schema for validating FormatDoc XML was null yet no errors were thrown.");
      return false;
    }
    // Everything evaluated OK and the document was parsed without any errors
    log.finest("Created FormatDoc with no problems");
    return true;
  }
  
  private void writeContents()
  {
    Element eRoot = docFD.createElement("formatDoc");
    // Obtain preferences for the locale so that I can build a localised time stamp
    Preferences locPrefs = MarsPreferences.getMarsPrefs();
    // Get the locale from the preferences. If it can't be obtained then default to the system property
    String strLang = locPrefs.get("mars.locale.lang", System.getProperty("user.language"));
    String strCountry = locPrefs.get("mars.locale.country", System.getProperty("user.country"));
    Locale locHere = new Locale(strLang, strCountry);
    // Now obtain the resource bundle for the locale
    MarsResourceBundle mrbTime = new MarsResourceBundle(locHere);
    String sTimeStamp = new String("");
    sTimeStamp = sTimeStamp + mrbTime.getRBString("mars.xml.fd.comment1", "Document created by Mars on ");
    Calendar now = Calendar.getInstance(locHere);
    sTimeStamp = sTimeStamp + now.get(Calendar.YEAR) + "-";
    sTimeStamp = sTimeStamp + now.getDisplayName(Calendar.MONTH, Calendar.LONG, locHere) + "-";
    sTimeStamp = sTimeStamp + now.get(Calendar.DATE) + " ";
    sTimeStamp = sTimeStamp + mrbTime.getRBString("mars.xml.fd.comment2", " at ");
    sTimeStamp = sTimeStamp + now.get(Calendar.HOUR_OF_DAY) + ":";
    sTimeStamp = sTimeStamp + now.get(Calendar.MINUTE) + ":";
    sTimeStamp = sTimeStamp + now.get(Calendar.SECOND);
    Comment cComment = docFD.createComment(sTimeStamp);
    eRoot.appendChild(cComment);
    // Release the items we've used to build this
    locPrefs = null;
    strLang = null;
    strCountry = null;
    locHere = null;
    mrbTime = null;
    sTimeStamp = null;
    cComment = null;
    // Add elements to the document
    // Adding the <connect> element to the document
    Element eConnect = docFD.createElement("connect");
    eConnect.setAttribute("using", fdSys.getProtocol());
    eConnect.setAttribute("to", fdSys.getDestination());
    eConnect.setTextContent(fdSys.getDataSourceName());
    eRoot.appendChild(eConnect);
    
    // Adding the <tables> element and sub nodes
    Element eTables = docFD.createElement("tables");
    Enumeration enumTables = fdSys.getTableKeys();
    while (enumTables.hasMoreElements())
    {
      Table aTable = fdSys.getTable((String) enumTables.nextElement());
      Element eTbl = docFD.createElement("table");
      Element eName = docFD.createElement("name");
      eName.setTextContent(aTable.getName());
      eTbl.appendChild(eName);
      Element eRetention = docFD.createElement("retention");
      eRetention.setAttribute("units", aTable.getRetentionUnits());
      eRetention.setTextContent((new Integer(aTable.getRetentionValue())).toString());
      eTbl.appendChild(eRetention);
      Enumeration enumAttributes = aTable.getAttributeKeys();
      while (enumAttributes.hasMoreElements())
      {
        Attribute anAttr = aTable.getAttribute((String) enumAttributes.nextElement());
        Element eAtt = docFD.createElement("attribute");
        eAtt.setAttribute("id", anAttr.getID());
        Element eAttName = docFD.createElement("name");
        eAttName.setTextContent(anAttr.getName());
        Element eAttType = docFD.createElement("type");
        eAttType.setTextContent(anAttr.getType());
        Element eAttReport = docFD.createElement("report");
        if (anAttr.isReported())
        {
          eAttReport.setTextContent("true");
        }
        else
        {
          eAttReport.setTextContent("false");
        }
        eAtt.appendChild(eAttName);
        eAtt.appendChild(eAttType);
        eAtt.appendChild(eAttReport);
        // See if there's any conversions within the attribute to be added
        if (anAttr.numberOfConversions() > 0)
        {
          Element eConvert = docFD.createElement("conversions");
          Enumeration enumConversions = anAttr.getConversionKeys();
          while (enumConversions.hasMoreElements())
          {
            String sKey = (String) enumConversions.nextElement();
            Element eConv = docFD.createElement("convert");
            eConv.setAttribute("raw", sKey);
            eConv.setTextContent((String) anAttr.getConversionValue(sKey));
            eConvert.appendChild(eConv);
          }
          eAtt.appendChild(eConvert);
        }
        eTbl.appendChild(eAtt);
      }
      eTables.appendChild(eTbl);
    }
    eRoot.appendChild(eTables);
    
    Element eText = docFD.createElement("text");
    Enumeration enumColumns = fdSys.getColumnKeys();
    while (enumColumns.hasMoreElements())
    {
      Column aCol = fdSys.getColumn((String) enumColumns.nextElement());
      Element eColumn = docFD.createElement("column");
      eColumn.setAttribute("from", (new Integer(aCol.getFrom())).toString());
      eColumn.setAttribute("to", (new Integer(aCol.getTo())).toString());
      Element eColName = docFD.createElement("name");
      eColName.setTextContent(aCol.getName());
      Element eColType = docFD.createElement("type");
      eColType.setTextContent(aCol.getType());
      eColumn.appendChild(eColName);
      eColumn.appendChild(eColType);
      eText.appendChild(eColumn);
    }
    eRoot.appendChild(eText);
    
    Element eQueries = docFD.createElement("queries");
    Enumeration enumQueries = fdSys.getQueryKeys();
    while (enumQueries.hasMoreElements())
    {
      Query aQry = fdSys.getQuery((String) enumQueries.nextElement());
      Element eQry = docFD.createElement("query");
      eQry.setAttribute("id", aQry.getID());
      Element eQryName = docFD.createElement("name");
      eQryName.setTextContent(aQry.getName());
      Element eQrySQL = docFD.createElement("statement");
      eQrySQL.setTextContent(aQry.getStatement());
      eQry.appendChild(eQryName);
      eQry.appendChild(eQrySQL);
      eQueries.appendChild(eQry);
    }
    eRoot.appendChild(eQueries);
    
    Element eAlarms = docFD.createElement("alarms");
    /** @todo alarms tag for the DOM Writer */
    eRoot.appendChild(eAlarms);
    
    docFD.appendChild(eRoot);
  }
}