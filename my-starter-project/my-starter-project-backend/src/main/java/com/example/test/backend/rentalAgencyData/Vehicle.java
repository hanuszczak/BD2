package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Vehicle implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    private boolean isFree = false;
    @NotNull
    private int stationId = -1;
    @NotNull
    private int typeId = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getStationId() {
        return stationId;
    }


    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return ("" + id);
    }
}
