package com.example.test.backend.connection;

import com.example.test.backend.rentalAgencyData.*;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    public String[] getPassAndRoleQuery(String login) throws SQLException{
        String[] data = new String[2];
        getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT PASSWORD, USER_TYPE_ID FROM USERS WHERE USERNAME = ?");
        stmt.setString(1, login);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        data[0] = rset.getString(1);
        int userType = rset.getInt(2);
        switch (userType) {
            case 1:
                data[1] = "admin";
                break;
            case 3:
                data[1] = "worker";
                break;
            default:
                data[1] = "user";
        }
        rset.close();
        stmt.close();
        closeConnection();
        return data;
    }

    public boolean newUserQuery(String username, String password, String name, String surname, String email, String phone)
                throws SQLException {
        getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS " +
                "(USER_ID, NAME, SURNAME, EMAIL, PHONE, IS_ACTIVE, USERNAME, PASSWORD, USER_TYPE_ID)" +
                " VALUES (SEQ_USERS_USER_ID.NEXTVAL, ?, ?, ?, ?, 1, ?, ?, 2)");
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
        phone = phone.replace(" ", "");
        phone = phone.replace("-", "");
        phone = phone.replace("(","");
        phone = phone.replace(")","");
        int number = Integer.parseInt(phone);
        return number;
    }

    public boolean rentQuery(String username, Region region, Station station, VehicleType vehicleType, Vehicle vehicle) {
        getConnection();
        //TODO
        closeConnection();
        return false;
    }

    public boolean returnQuery(String username, Vehicle vehicle, Region region, Station station) {
        getConnection();
        //TODO
        closeConnection();
        return false;
    }

    public List<Region> getRegionsQuery() {
        List<Region> regions = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();

        return regions;
    }

    public List<Station> getStationsForRegionQuery(Region region) {
        List<Station> stations = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();

        return  stations;
    }

    public List<VehicleType> getVehicleTypesForStationQuery(Station station) {
        List<VehicleType> vehicleTypes = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();

        return vehicleTypes;
    }

    public List<Vehicle> getVehiclesForStationQuery(Station station, VehicleType vehicleType){
        List<Vehicle> vehicles = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();

        return vehicles;
    }

    public List<Vehicle> getRentedVehiclesQuery(String username) {
        List<Vehicle> vehicles = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();
        return vehicles;
    }

    public List<User> getAllUsersQuery() {
        List<User> users = new ArrayList<>();
        getConnection();
        //TODO
        closeConnection();
        return users;
    }

}
