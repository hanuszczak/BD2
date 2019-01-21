package com.example.test.authentication;

import java.awt.*;
import java.io.Serializable;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl extends Serializable {

    String ADMIN_ROLE_NAME = "admin";
    String ADMIN_USERNAME = "admin";

    public boolean signIn(String username, String password);

    public boolean signUp(String username, String password, String name, String surname, String email, String phone);

    public boolean signUpCheckUsername(String username);

    public boolean signUpCheckMail(String email);

    public boolean isUserSignedIn();

    public boolean isUserInRoleOfAdmin();

    public boolean isUserInRoleOfWorker();

    public String getPrincipalName();
}
