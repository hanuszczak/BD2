package com.example.test.backend.rentalAgencyData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Rental implements Serializable {

    @NotNull
    int rentalId = -1;
    @NotNull
    int vehicleId = -1;
    @NotNull
    String stationFrom = "";
    @NotNull
    String stationTo = "";
    @NotNull
    String dateFrom = "";
    @NotNull
    String dateTo = "";
    @NotNull
    float paymentValue = -1;
    @NotNull
    String paymentStatus = "";

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String rentalIdToString() {
        return "" + rentalId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    public String vehicleIdToString() {
        return "" + vehicleId;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }

    public String getStationTo() {
        return stationTo;
    }

    public void setStationTo(String stationTo) {
        this.stationTo = stationTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public float getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(float paymentValue) {
        this.paymentValue = paymentValue;
    }

    public String paymentValueToString() {
        return String.format("%.2f", paymentValue) + " zl";
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }



}
