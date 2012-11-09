MARS
====

Application for monitoring multiple systems for administrative alerting. Currently only performs the following functions:
- COM port monitoring
- JDBC/ODBC Database structure detection

Planned functions:
- Definition of alerts and storage within persistant storage via JAX-B/XML.
- Connection of the application to data sources via COM port or ODBC.
- Comparison of defined fields against rules (as defined by the user) and generation of alerts.

Required libraries:
-------------------
RxTx (http://rxtx.qbang.org/wiki/index.php/Download) - Used for COM port connection