package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class User implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    private String username = "";
    @NotNull
    private String name = "";
    @NotNull
    private String surname = "";
    @NotNull
    private String email = "";
    @NotNull
    private long phone = -1;
    @NotNull
    private Activity isActive = Activity.ACTIVE;
    @NotNull
    private UserType userType = UserType.USER;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Activity getIsActive() {
        return isActive;
    }

    public void setIsActive(Activity isActive) {
        this.isActive = isActive;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String phoneToString() {
        return ""+phone;
    }

    public String userTypeToString() {

        return userType.toString();
    }
    public String idToString() {
        return ""+ id;
    }

    public String isActiveToString() {
        return isActive.toString();
    }
}
