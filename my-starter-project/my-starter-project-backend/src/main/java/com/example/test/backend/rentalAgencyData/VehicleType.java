package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class VehicleType implements Serializable {

    @NotNull
    private int id;
    @NotNull
    private int cost;
    @NotNull
    private int min_balance;
    @NotNull
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMin_balance() {
        return min_balance;
    }

    public void setMin_balance(int min_balance) {
        this.min_balance = min_balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

//    @Override
//    public String toString() {
//        return getType();
//    }
}
