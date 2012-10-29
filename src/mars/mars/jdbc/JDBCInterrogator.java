/* Class name: JDBCInterrogator
 * File name:  JDBCInterrogator.java
 * Created:    07-May-2008 11:37:19
 * Modified:   09-Jun-2008
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  09-Jun-2008 Altered the way in which information is gathered so that instead of
          using nested ResultSets, only one ResultSet is open at a time.
 * 0.002  12-May-2008 Added support for errors with the connections to a datasource
 * 0.001  07-May-2008 Initial build
 */

package mars.mars.jdbc;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import mars.mars.object.logging.LoggerFactory;
import mars.mars.object.table.*;

/**
 * This class is used to connect to a datasource, given its name, and using queries
 * and metadata, provides a Hashtable that contains all of the tables and their attributes
 * within all schemas and catalogues within the database.
 * @version 0.003
 * @author Alex Harris (W4786241)
 * @todo JDoc and commenting
 */
public class JDBCInterrogator
{
  private Logger log;
  private static final String parentClassName = "mars.mars.jdbc.JDBCInterrogator";
  
  /**
   * Configures a new instance of the class and creates a new logger to be used for monitoring and reporting with it.
   */
  public JDBCInterrogator()
  {
    // Default constructor
    log = LoggerFactory.getLogger(parentClassName);
  }
  
  /**
   * Returns a Hashtable containing an instance of GenericTable containing the following
   * information about attributes from each table within the chosen datasource:
   * <ul>
   *   <li>Ordinal Position</li>
   *   <li>Name</li>
   *   <li>Original SQL Type</li>
   *   <li>MARS SQL Type</li>
   *   <li>Whether the attribute is supported in MARS</li>
   * </ul>
   * <p>NOTE: The returned hashtable containing the instances of GenericTable uses keys
   * containing only the table names rather than a composite of the Table name together with
   * the Schema and/or Catalogue names.
   * @param odbcDSNName The name of the required datasource
   * @return The hashtable containing instances of GenericTable using table names for keys.
   */
  public Hashtable interrogateDataSource(String odbcDSNName)
  {
    Hashtable htTables = new Hashtable();  // This hashtable will be used to store the tables contained within the database
    try
    {
      log.finest("Obtaining Connection for " + odbcDSNName);
      int iResult = javax.swing.JOptionPane.showConfirmDialog(mars.mars.gui.MarsClient.getDesktopPane(),"Does this datasource require a specific login (other than Guest)?","Question:",javax.swing.JOptionPane.YES_NO_OPTION);
      Connection con;
      if (iResult == 0)
      {
        // User said yes so we need to get the login details.
        String userName = javax.swing.JOptionPane.showInputDialog(mars.mars.gui.MarsClient.getDesktopPane(), "Please enter the user name:");
        String password = javax.swing.JOptionPane.showInputDialog(mars.mars.gui.MarsClient.getDesktopPane(), "Please enter the password:");
        con = ConnectionFactory.getConnection(odbcDSNName,userName,password);
      }
      else
      {
        // User said no
        con = ConnectionFactory.getConnection(odbcDSNName);
      }
      // Check that the connection has been setup correctly.
      if (con == null)
      {
        log.warning("Could not connect to the data source as there was an error or it was not responding");
        return null;
      }
      else
      {
        log.finest("Connection to data source has been established. Obtaining tables...");
      }
      
      // Obtain the MetaData for the database
      DatabaseMetaData dbMD = con.getMetaData();
      /*************************************************************************/
      /* Fetch all tables listed within the database:                          */
      /*   Regardless of the catalogue in which the table is stored            */
      /*   Regardless of the schema in which the table is stored               */
      /*   Matching any name                                                   */
      /*   Of the type TABLE                                                   */
      /*************************************************************************/
      ResultSet rs = dbMD.getTables(null, null, "%", new String[] {"TABLE"});
      // Create a Vector to contain the names of the tables, schemas and catalogues so we can close the ResultSet when done
      Vector vecTables = new Vector();
      // Iterate through the list of tables and output to the logger so I can see what's been initially received
      while (rs.next())
      {
        String catalogue, schema, name;
        catalogue = rs.getString(1);
        schema = rs.getString(2);
        name = rs.getString(3);
        log.config("Found a table named: "+ name +" within this schema: "+ schema +" stored within this catalogue: "+ catalogue);
        // Put the name, schema and catalogue in the vector
        String[] strDetails = new String[3];
        strDetails[0] = name;
        strDetails[1] = schema;
        strDetails[2] = catalogue;
        vecTables.add(strDetails);
      }
      // Now that we're finished with the tables rs we can close it.
      rs.close();
      // Iterate through the ResultSet to obtain attribute information
      for (int x = 0; x < vecTables.size(); x++)
      {
        // Fetch the catalogue, schema and name out of the vector
        String[] strTemp = (String[]) vecTables.get(x);
        String catalogue, schema, name;
        name = strTemp[0];
        schema = strTemp[1];
        catalogue = strTemp[2];
        log.finer("Iterating through table "+ name +" in "+ schema +" stored within "+ catalogue);
        /*************************************************************************/
        /* Fetch all attributes listed within the database:                      */
        /*   Within the specified catalogue, defined from the DatabaseMetadata   */
        /*   Within the specified schema, defined from the DatabaseMetadata      */
        /*   Within the specified table name, defined from the Tables ResultSet  */
        /*   Of any name                                                         */
        /*************************************************************************/
        ResultSet result = dbMD.getColumns(catalogue, schema, name, "%");
        // Create the column names vector and the rows vector
        Vector vecColumnNames = new Vector();
        Vector vecRows = new Vector();
        // Add the column names to the appropriate vector
        vecColumnNames.add("Position");
        vecColumnNames.add("Name");
        vecColumnNames.add("SQL Type");
        vecColumnNames.add("MARS Type");
        vecColumnNames.add("Supported");
        // Iterate through the returned ResultSet to obtain the attribute details
        while (result.next())
        {
          Object[] oRow = new Object[5];
          oRow[0] = new Integer(result.getInt(17)); // The ordinal position of the attribute
          oRow[1] = new String(result.getString(4)); // The attribute name
          String sqlType = new String();
          String marsType = new String();
          boolean isValid = false;
          int i = result.getInt(5);
          sqlType = "Unknown";
          marsType = "Unknown";
          switch (i)
          {
            /******************************************************************/
            /* The following SQL types are supported within this method:      */
            /* Boolean                                                        */
            /* DateTime                                                       */
            /* Double                                                         */
            /* Float                                                          */
            /* Int                                                            */
            /* String                                                         */
            /******************************************************************/
            /* SUPPORTED TYPES: (Note the conversion of types)                */
            /******************************************************************/
            case Types.BIGINT:
            {
              sqlType = "BigInt";
              marsType = "Double";
              isValid = true;
              break;
            }
            case Types.BIT: 
            {
              sqlType = "Bit";
              marsType = "Boolean";
              isValid = true;
              break;
            }
            case Types.BOOLEAN:
            {
              sqlType = "Boolean";
              marsType = "Boolean";
              isValid = true;
              break;
            }
            case Types.CHAR:
            {
              sqlType = "Char";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.DATE:
            {
              sqlType = "Date";
              marsType = "DateTime";
              isValid = true;
              break;
            }
            case Types.DECIMAL:
            {
              sqlType = "Decimal";
              marsType = "Float";
              isValid = true;
              break;
            }
            case Types.DOUBLE:
            {
              sqlType = "Double";
              marsType = "Double";
              isValid = true;
              break;
            }
            case Types.FLOAT:
            {
              sqlType = "Float";
              marsType = "Float";
              isValid = true;
              break;
            }
            case Types.INTEGER:
            {
              sqlType = "Integer";
              marsType = "Integer";
              isValid = true;
              break;
            }
            case Types.LONGNVARCHAR: 
            {
              sqlType = "Long nVarChar";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.LONGVARCHAR: 
            {
              sqlType = "Long VarChar";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.NCHAR: 
            {
              sqlType = "nChar";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.NVARCHAR:
            {
              sqlType = "nVarChar";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.NUMERIC:
            {
              sqlType = "Numeric";
              marsType = "Double";
              isValid = true;
              break;
            }
            case Types.SMALLINT: 
            {
              sqlType = "SmallInt";
              marsType = "Integer";
              isValid = true;
              break;
            }
            case Types.SQLXML: 
            {
              sqlType = "SQLXML";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            case Types.TIME:
            {
              sqlType = "Time";
              marsType = "Integer";
              isValid = true;
              break;
            }
            case Types.TIMESTAMP:
            { 
              sqlType = "TimeStamp";
              marsType = "DateTime";
              isValid = true;
              break;
            }
            case Types.TINYINT:
            { 
              sqlType = "TinyInt";
              marsType = "Integer";
              isValid = true;
              break;
            }
            case Types.VARCHAR:
            { 
              sqlType = "VarChar";
              // Check the column size and see whether the string will be too long to store
              if (result.getInt(7) < 256)
              {
                marsType = "String";
                isValid = true;
              }
              else
              {
                marsType = "Unsupported";
                isValid = false;
              }
              break;
            }
            /******************************************************************/
            /* The following are java.sql.types that are unsupported:         */
            /******************************************************************/
            default:
            {
              switch (i)
              {
                case Types.ARRAY: { sqlType = "Array"; break;}
                case Types.BINARY: { sqlType = "Binary"; break;}
                case Types.BLOB: { sqlType = "BLOB"; break;}
                case Types.CLOB: { sqlType = "CLOB"; break;}
                case Types.DATALINK: { sqlType = "Datalink"; break;}
                case Types.DISTINCT: { sqlType = "Distinct"; break;}
                case Types.JAVA_OBJECT: { sqlType = "Java Object"; break;}
                case Types.LONGVARBINARY: { sqlType = "LongVarBinary"; break;}
                case Types.NCLOB: { sqlType = "nCLOB"; break;}
                case Types.NULL: { sqlType = "Null"; break;}
                case Types.OTHER: { sqlType = "Other"; break;}
                case Types.REAL: { sqlType = "Real"; break;}
                case Types.REF: { sqlType = "Ref"; break;}
                case Types.ROWID: { sqlType = "RowID"; break;}
                case Types.STRUCT: { sqlType = "Struct"; break;}
                case Types.VARBINARY: { sqlType = "VarBinary"; break;}
              }
              marsType = "Unsupported";
              isValid = false;
              break;
            }
          }
          oRow[2] = sqlType; // The original attributes SQL Type
          oRow[3] = marsType; // The attribute type mapped into MARS
          Boolean isSupported = new Boolean(isValid);
          oRow[4] = isSupported; // Whether it is compatible with MARS or not
          vecRows.add(oRow);
        }
        // Create a new GenericTable to place within the hashtable
        GenericTable gtTableData = new GenericTable();
        // Set the data within the table
        gtTableData.setData(vecColumnNames, vecRows);
        // Add the table to the hashtable using the table name as the identifier
        htTables.put(name, gtTableData);
      }
    }
    catch (SQLException sqlX)
    {
      log.throwing(parentClassName, "interrogateDataSource(String)", sqlX);
    }
    return htTables;
  }
}