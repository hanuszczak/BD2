package com.example.test.backend.connection;

import com.example.test.backend.rentalAgencyData.*;

import java.awt.print.PrinterAbortException;
import java.sql.*;
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

    public String hashPass(String username, String password) throws SQLException{
        getConnection();
        PreparedStatement stmt = conn.prepareCall("SELECT login_func(?,?) from dual");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        String hashpassword = rset.getString(1);
        rset.close();
        stmt.close();
        closeConnection();
        return hashpassword;
    }

    public boolean setNewPassword(String username, String newHashPass) {
        boolean ifSuccess = false;
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE USERS SET PASSWORD = ? WHERE USERNAME = ?");
            stmt.setString(1, newHashPass);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();
            ifSuccess = true;
        }
        catch (SQLException e) {
            System.out.println("Error JDBCConnection getConnection: " + e.getMessage());
        }
        closeConnection();
        return ifSuccess;
    }

    public boolean newUserQuery(String username, String password, String name, String surname, String email, String phone)
                throws SQLException {
        String hashpassword = hashPass(username,password);
        getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS " +
                "(USER_ID, NAME, SURNAME, EMAIL, PHONE, IS_ACTIVE, USERNAME, PASSWORD, USER_TYPE_ID)" +
                " VALUES (SEQ_USERS_USER_ID.NEXTVAL, ?, ?, ?, ?, 1, ?, ?, 2)");
        stmt.setString(1, name);
        stmt.setString(2, surname);
        stmt.setString(3, email);
        stmt.setInt(4, stringPhoneNumberToInt(phone));
        stmt.setString(5, username);
        stmt.setString(6, hashpassword);
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

    public boolean rentQuery(String username, Station station, VehicleType vehicleType, Vehicle vehicle){
        boolean ifSuccess = false;
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id from users where username = ?");
            stmt.setString(1, username);
            ResultSet rset = stmt.executeQuery();
            rset.next();
            int user_id = rset.getInt(1);
            rset.close();
            stmt.close();

            CallableStatement cs = null;
            cs = conn.prepareCall("{call rent_vehicle2(?,?,?,?)}");
            cs.setInt(1, station.getId());
            cs.setInt(2, user_id);
            cs.setInt(3, vehicleType.getId());
            cs.setInt(4, vehicle.getId());
            cs.execute();
            cs.close();
            System.out.println("Call success");
            ifSuccess = true;
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection rentQuery():" + e.getMessage());
        }
        closeConnection();
        return ifSuccess;
    }

    public boolean returnQuery(String username, Vehicle vehicle, Station station){
        boolean ifSuccess = false;
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT rental_id from vehiclerentals " +
                    "INNER JOIN users ON vehiclerentals.user_id = users.user_id " +
                    "where users.username = ? AND vehiclerentals.vehicle_id = ?");
            stmt.setString(1, username);
            stmt.setInt(2, vehicle.getId());
            ResultSet rset = stmt.executeQuery();
            rset.next();
            int rent_id = rset.getInt(1);
            rset.close();
            stmt.close();

            CallableStatement cs = null;
            cs = conn.prepareCall("{call return_bike(?,?)}");
            cs.setInt(1, rent_id);
            cs.setInt(2, station.getId());
            cs.execute();
            cs.close();
            ifSuccess = true;
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection returnQuery():" + e.getMessage());
        }
        closeConnection();
        return ifSuccess;
    }

    public List<Region> getRegionsQuery() {
        List<Region> regions = new ArrayList<>();
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM regions");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Region region = new Region();
                region.setId(rset.getInt(1));
                region.setName(rset.getString(2));
                regions.add(region);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getRegionsQuery():" + e.getMessage());
        }
        closeConnection();
        return regions;
    }

    public List<Station> getStationsForRegionQuery(Region region){
        List<Station> stations = new ArrayList<>();
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM stations WHERE region_id = ?");
            stmt.setInt(1, region.getId());
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Station station = new Station();
                station.setId(rset.getInt(1));
                station.setAddress(rset.getString(2));
                station.setLimit(rset.getInt(3));
                station.setFreeVehicles(rset.getInt(4));
                station.setRegionId(rset.getInt(5));
                stations.add(station);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getStationsForRegionQuery():" + e.getMessage());
        }
        closeConnection();
        return  stations;
    }

    public List<VehicleType> getVehicleTypesForStationQuery(Station station){
        List<VehicleType> vehicleTypes = new ArrayList<>();
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*), vehicletypes.type_id, vehicletypes.type, vehicletypes.cost, vehicletypes.min_balance FROM vehicletypes " +
                    "INNER JOIN vehicles ON vehicletypes.type_id = vehicles.type_id " +
                    "WHERE vehicles.station_id = ? AND vehicles.is_free=1 " +
                    "GROUP BY vehicletypes.type_id, vehicletypes.type, vehicletypes.cost, vehicletypes.min_balance");
            stmt.setInt(1, station.getId());
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                VehicleType vehicleT = new VehicleType();
                vehicleT.setId(rset.getInt(2));
                vehicleT.setType(rset.getString(3));
                vehicleT.setCost(rset.getFloat(4));
                vehicleT.setMin_balance(rset.getFloat(5));
                vehicleTypes.add(vehicleT);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getVehicleTypesForStationQuery():" + e.getMessage());
        }
        closeConnection();
        return vehicleTypes;
    }

    public List<Vehicle> getVehiclesForStationQuery(Station station, VehicleType vehicleType){
        List<Vehicle> vehicles = new ArrayList<>();
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicles WHERE station_id = ? AND type_id = ? AND vehicles.is_free=1");
            stmt.setInt(1, station.getId());
            stmt.setInt(2, vehicleType.getId());
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rset.getInt(1));
                vehicle.setFree(rset.getBoolean(2));
                vehicle.setStationId(rset.getInt(3));
                vehicle.setTypeId(rset.getInt(4));
                vehicles.add(vehicle);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getVehiclesForStationQuery():" + e.getMessage());
        }
        closeConnection();
        return vehicles;
    }

    public List<Vehicle> getRentedVehiclesQuery(String username){
        List<Vehicle> vehicles = new ArrayList<>();
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT vehicles.* FROM vehiclerentals " +
                    "INNER JOIN users ON vehiclerentals.user_id = users.user_id " +
                    "INNER JOIN vehicles ON vehiclerentals.vehicle_id = vehicles.vehicle_id " +
                    "WHERE users.username = ? and vehiclerentals.DATE_TO is NULL");
            stmt.setString(1, username);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rset.getInt(1));
                vehicle.setFree(rset.getBoolean(2));
                vehicle.setStationId(rset.getInt(3));
                vehicle.setTypeId(rset.getInt(4));
                vehicles.add(vehicle);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getRentedVehiclesQuery():" + e.getMessage());
        }
        closeConnection();
        return vehicles;
    }


    public boolean updateUserQuery(User user){
        boolean ifSuccessful = false;
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE USERS SET USER_TYPE_ID = ?," +
                    "NAME = ?, SURNAME = ?, EMAIL = ?, PHONE = ?, IS_ACTIVE = ?" +
                    "WHERE USER_ID = ?");
            stmt.setInt(1, getUerTypeForQuery(user.getUserType()));
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getEmail());
            stmt.setLong(5, user.getPhone());
            stmt.setInt(6, getActivityForQuery(user.getIsActive()));
            stmt.setInt(7, user.getId());
            stmt.executeUpdate();
            stmt.close();
            ifSuccessful = true;
        }
        catch (SQLException e) {
            System.out.println("Error JDBCConnection updateUserQuery():" + e.getMessage());
        }
        closeConnection();
        return ifSuccessful;
    }

    public boolean generateRaport(Station station, int daysNo) {
        boolean ifSuccessful = false;
        getConnection();
        //TODO
        closeConnection();
        return ifSuccessful;
    }

    public int getAccountIDQuery(String username){
        int accountId = 0;
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT account_id FROM users where username=?");
            stmt.setString(1, username);
            ResultSet rset = stmt.executeQuery();
            rset.next();
            accountId = rset.getInt(1);
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getAccountIDQuery():" + e.getMessage());
        }
        closeConnection();
        return accountId;
    }

    public boolean topUpQuery(int accountId, float topUp){
        boolean ifSuccess = false;
        getConnection();
        try{
            CallableStatement cs = null;
            cs = conn.prepareCall("{call charge_or_load_user_account(?, ?)}");
            cs.setInt(1, accountId);
            cs.setFloat(2, topUp);
            ResultSet rset = cs.executeQuery();
            rset.close();
            cs.close();
            ifSuccess = true;
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection topUpQuery{():" + e.getMessage());
        }
        closeConnection();
        return ifSuccess;
    }

    public float getActualBalanceForUserQuery(String username){
        float balance = 100;
        getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT accounts.balance FROM accounts " +
                    "INNER JOIN users ON accounts.account_id = users.account_id " +
                    "where users.username=?");
            stmt.setString(1, username);
            ResultSet rset = stmt.executeQuery();
            rset.next();
            balance = rset.getFloat(1);
            rset.close();
            stmt.close();
            closeConnection();
        } catch (SQLException e) {
            System.out.println("Error JDBCConnection getActualBalance():" + e.getMessage());
        }
        closeConnection();
        return balance;
    }

    public void deleteUserQuery(User user){
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM VERIFICATIONCODES WHERE USER_ID = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println("Error JDBCConnection deleteUserQuery(delete users):" + e.getMessage());
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USERS WHERE USER_ID = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println("Error JDBCConnection deleteUserQuery(delete users):" + e.getMessage());
        }
        closeConnection();
    }

    public List<User> getAllUsersQuery() {
        List<User> users = new ArrayList<>();
        getConnection();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT USER_ID, NAME, SURNAME, EMAIL, PHONE, " +
                    "IS_ACTIVE, USERNAME, USER_TYPE_ID FROM USERS");
            while (rset.next()) {
                User user = new User();
                user.setId(rset.getInt(1));
                user.setName(rset.getString(2));
                user.setSurname(rset.getString(3));
                user.setEmail(rset.getString(4));
                user.setPhone(rset.getLong(5));
                user.setIsActive(getActivityFromQuery(rset.getInt(6)));
                user.setUsername(rset.getString(7));
                user.setUserType(getUserTypeFromQuery(rset.getInt(8)));
                users.add(user);
            }
            rset.close();
            stmt.close();
        }
        catch (SQLException e){
            System.out.println("Error JDBCConnection getAllUsersQuery: " + e.getMessage());
        }
        closeConnection();
        return users;
    }


    private UserType getUserTypeFromQuery(int i) {
        UserType userType;
        switch (i) {
            case 1:
                userType = UserType.ADMIN;
                break;
            case 3:
                userType = UserType.WORKER;
                break;
            default:
                userType = UserType.USER;
        }
        return userType;
    }

    private int getUerTypeForQuery(UserType type) {
        int i = 0;
        switch (type) {
            case ADMIN:
                i = 1;
                break;
            case WORKER:
                i = 3;
                break;
            default:
                i = 2;
        }
        return i;
    }

    private Activity getActivityFromQuery(int i) {
        Activity activity;
        if(i == 0) {
            activity = Activity.BLOCKED;
        }
        else {
            activity = Activity.ACTIVE;
        }
        return activity;
    }

    private int getActivityForQuery(Activity active) {
        int i = 0;
        if(active == Activity.ACTIVE) {
           i = 1;
        }
        return i;
    }
}
