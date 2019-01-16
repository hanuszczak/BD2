package com.example.test.backend.connection;

import com.example.test.backend.rentalAgencyData.Region;
import com.example.test.backend.rentalAgencyData.Station;
import com.example.test.backend.rentalAgencyData.Vehicle;
import com.example.test.backend.rentalAgencyData.VehicleType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalControl {

    JDBCConnection jdbcConnection;

    List<Region> regions;
    List<Station> stations;
    List<Vehicle> vehicles;
    List<VehicleType> vehicleTypes;

    public RentalControl() {
        jdbcConnection = new JDBCConnection();
        regions = new ArrayList<>();
        stations = new ArrayList<>();
        vehicles = new ArrayList<>();
        vehicleTypes = new ArrayList<>();
    }

    public boolean rent(String username, Region region, Station station, VehicleType vehicleType, Vehicle vehicle) {
        boolean ifRentSuccessful = false;
        //       try {
        ifRentSuccessful = jdbcConnection.rentQuery(username, region, station, vehicleType, vehicle);
//        }
//        catch (SQLException e) {
//            System.out.println("Error BasicAccessControl (getPassQuery): " + e.getMessage());
//        }
        return ifRentSuccessful;
    }

    public boolean returnVehicle(String username, Region region, Station station, VehicleType vehicleType, Vehicle vehicle) {
        boolean ifReturnSuccessful = false;
//        try {
        ifReturnSuccessful = jdbcConnection.returnQuery(username, region, station, vehicleType, vehicle);
//        }
//        catch (SQLException e) {
//            System.out.println("Error BasicAccessControl (getPassQuery): " + e.getMessage());
//        }
        return ifReturnSuccessful;
    }

    public void getRegionsFromDatabase() {
        regions = jdbcConnection.getRegionsQuery();
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void getStationsFromDatabseForRegion(Region region) {
        stations = jdbcConnection.getStationsForRegionQuery(region);
    }

    public List<Station> getStations() {
        return stations;
    }

    public void getVehicleTypesFromDatabaseForStation(Station station) {
        vehicleTypes = jdbcConnection.getVehicleTypesForStationQuery(station);
    }

    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void getVehiclesFromDatabaseForStation(Station station, VehicleType vehicleType){
        vehicles = jdbcConnection.getVehiclesForStationQuery(station, vehicleType);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}


