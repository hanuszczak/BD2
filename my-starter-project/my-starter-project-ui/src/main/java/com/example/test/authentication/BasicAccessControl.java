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
        CurrentUser.set(username);
        String pass = new String();
        try {
            pass = jdbcConnection.getPassQuery(username);
        }
        catch (SQLException e) {
            System.out.println("Error BasicAccessControl (getPassQuery): " + e.getMessage());
        }
        if(password.equals(pass)){
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
        CurrentUser.set(username);
        return ifSigningUpSucceed;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return getPrincipalName().equals("admin");
        }

        // All users are in all non-admin roles
        return true;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

}
