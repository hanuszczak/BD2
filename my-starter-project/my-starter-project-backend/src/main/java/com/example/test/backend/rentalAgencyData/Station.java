package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Station implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    private String address = "address";
    @NotNull
    private int freeVehicles = 0;
    @NotNull
    private int limit = 0;
    @NotNull
    private int regionId = -1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeVehicles() {
        return freeVehicles;
    }

    public void setFreeVehicles(int freeVehicles) {
        this.freeVehicles = freeVehicles;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String name) {
        this.address = name;
    }

//    @Override
//    public String toString() {
//        return getAddress();
//    }
}
