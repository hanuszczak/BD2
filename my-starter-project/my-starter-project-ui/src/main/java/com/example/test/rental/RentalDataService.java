package com.example.test.rental;

import com.example.test.authentication.CurrentUser;
import com.example.test.backend.connection.JDBCConnection;
import com.example.test.backend.rentalAgencyData.Rental;

import java.util.List;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class RentalDataService extends MyDataService2 {

    private static RentalDataService INSTANCE;

    private String username = CurrentUser.get();

    private JDBCConnection jdbcConnection = new JDBCConnection();

    private List<Rental> rentals;

    private RentalDataService() {
        rentals = getRentalsFromDatabase(username);
    }

    public synchronized static RentalDataService getInstance() {
        //if (INSTANCE == null) {
            INSTANCE = new RentalDataService();
        //}
        return INSTANCE;
    }

    public synchronized List<Rental> getAllRentals() {
        return rentals;
    }


    private List<Rental> getRentalsFromDatabase(String username) {
        return jdbcConnection.getRentalsForUserQuery(username);
    }
}
