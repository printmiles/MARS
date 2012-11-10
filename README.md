MARS
====

Application for monitoring multiple systems for administrative alerting. Currently only performs the following functions:
- COM port monitoring
- JDBC/ODBC Database structure detection

Interesting Code
----------------
- src/mars/mars/jdbc/JDBCInterrogator.java - Lines 89-406 - Uses a DSN name and metadata to display the contects of a database.

Planned functions
-----------------
- Definition of alerts and storage within persistant storage via JAX-B/XML.
- Connection of the application to data sources via COM port or ODBC.
- Comparison of defined fields against rules (as defined by the user) and generation of alerts.

Required libraries
------------------
RxTx (http://rxtx.qbang.org/wiki/index.php/Download) - Used for COM port connection