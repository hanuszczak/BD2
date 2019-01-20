package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class VehicleType implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    private float cost = 0;
    @NotNull
    private float min_balance = 0;
    @NotNull
    private String type = "type";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getMin_balance() {
        return min_balance;
    }

    public void setMin_balance(float min_balance) {
        this.min_balance = min_balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

}
