package com.example.test.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


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

    public String getPassQuery(String login) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT PASSWORD FROM USERS WHERE USERNAME = ?");
        stmt.setString(1, login);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        String password = rset.getString(1);
        rset.close();
        stmt.close();
        return password;
    }
}
