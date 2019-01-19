package com.example.test.authentication;


import com.example.test.backend.connection.JDBCConnection;

import java.sql.SQLException;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

    JDBCConnection jdbcConnection;

    BasicAccessControl() {
        this.jdbcConnection = new JDBCConnection();
    }

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty())
            return false;
        String[] data = new String[2];  // data[0] - pass, data[1] - role
        try {
            data = jdbcConnection.getPassAndRoleQuery(username);
        }
        catch (SQLException e) {
            System.out.println("Error BasicAccessControl (getPassQuery): " + e.getMessage());
        }
        CurrentUser.set(username, data[1]);
        if(password.equals(data[0])){
            return true;
        }
        return false;
    }

    @Override
    public boolean signUp(String username, String password, String name, String surname, String email, String phone){
        boolean ifSigningUpSucceed = false;
        try {
            ifSigningUpSucceed = jdbcConnection.newUserQuery(username, password, name, surname, email, phone);
        }
        catch (SQLException e) {
            System.out.println("Error BasicAccessControl (newUserQuery): " + e.getMessage());
        }
        CurrentUser.set(username, "user");
        return ifSigningUpSucceed;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRoleOfAdmin() {
            return CurrentUser.getRole().equals("admin");

    }
    @Override
    public boolean isUserInRoleOfWorker() {
        return CurrentUser.getRole().equals("worker");

    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

}
