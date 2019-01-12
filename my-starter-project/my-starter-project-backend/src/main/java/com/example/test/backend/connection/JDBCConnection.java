package com.example.test.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnection {

    private Connection conn;
    private static final String JDBC_URL = "jdbc:oracle:thin:@bazydanych2.oracleapexservices.com:1521/xe";
    private static final String JDBC_USER = "sh1blue103";
    private static final String JDBC_PASS =  "khpVuZlRgj";

    public JDBCConnection() {
       try
       {
           this.conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
       }
       catch (SQLException e)
       {
           System.out.println("Error JDBCConnection constructor: " + e.getMessage());
       }
    }
}
