package com.example.test.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class JDBCConnection {

    private Connection conn;
    private static final String JDBC_URL = "jdbc:oracle:thin:@bazydanych2.oracleapexservices.com:1521/xe";
    private static final String JDBC_USER = "sh1blue103";
    private static final String JDBC_PASS =  "khpVuZlRgj";

    public JDBCConnection() {
           this.conn = null;
    }

    private void getConnection() {
        try
        {
        this.conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        }
        catch (SQLException e)
        {
            System.out.println("Error JDBCConnection getConnection: " + e.getMessage());
        }
    }

    private void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e)
            {
                System.out.println("Error JDBCConnection getConnection: " + e.getMessage());
            }
        }
    }

    public String getPassQuery(String login) throws SQLException{
        getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT PASSWORD FROM USERS WHERE USERNAME = ?");
        stmt.setString(1, login);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        String password = rset.getString(1);
        rset.close();
        stmt.close();
        closeConnection();
        return password;
    }

    public boolean newUserQuery(String username, String password, String name, String surname, String email, String phone)
                throws SQLException {
        getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS " +
                "(USER_ID, NAME, SURNAME, EMAIL, PHONE, IS_ACTIVE, USERNAME, PASSWORD, USER_TYPE_ID)" +
                " VALUES (SEQ_USERS_USER_ID.NEXTVAL, ?, ?, ?, ?, 0, ?, ?, 2)");
        stmt.setString(1, name);
        stmt.setString(2, surname);
        stmt.setString(3, email);
        stmt.setInt(4, stringPhoneNumberToInt(phone));
        stmt.setString(5, username);
        stmt.setString(6, password);
        stmt.executeUpdate();
        closeConnection();
        return true;
    }

    public int stringPhoneNumberToInt (String phone) {
        phone.replace(" ", "");
        phone.replace("-", "");
        phone.replace("(","");
        phone.replace(")","");
        int number = Integer.parseInt(phone);
        return number;
    }
}
